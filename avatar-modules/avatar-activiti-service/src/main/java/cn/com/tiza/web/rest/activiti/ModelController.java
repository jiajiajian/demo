package cn.com.tiza.web.rest.activiti;

import cn.com.tiza.dto.Query;
import cn.com.tiza.service.dto.LockModelQuery;
import cn.com.tiza.service.dto.ModelDto;
import cn.com.tiza.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.activiti.editor.constants.ModelDataJsonConstants.*;

/**
 * @author villas
 */
@RestController
@Slf4j
public class ModelController extends BaseController {


    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("models")
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<Model>> list(LockModelQuery query){
        ModelQuery modelQuery = repositoryService.createModelQuery().orderByLastUpdateTime().desc();
        if(StringUtils.isNotEmpty(query.getName())){
            modelQuery.modelNameLike("%" + query.getName() + "%");
        }
        if(StringUtils.isNotEmpty(query.getCategory())){
            modelQuery.modelCategory(query.getCategory());
        }
        long total = modelQuery.count();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(total));
        List<Model> models = modelQuery.listPage(getFirstResult(query, total), query.getLimit());
        return new ResponseEntity(models, headers, HttpStatus.OK);
    }


    /**
     * 创建模型
     *
     * @param dto 模型名称
     *            模型key
     */
    @GetMapping("/model/create")
    public ResponseEntity<String> create(ModelDto dto) {
        @NotBlank String key = dto.getKey();
        @NotBlank String name = dto.getName();
        log.info("创建模型入参name：{}, key: {}", name, key);

        int v = Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(key).count() + 1));

        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(MODEL_NAME, name);
        modelNode.put(MODEL_REVISION, v);

        modelNode.put(MODEL_DESCRIPTION, dto.getDescription());

        Model model = repositoryService.newModel();
        model.setName(name);
        model.setKey(key);
        model.setVersion(v);
        model.setCategory(dto.getCategory());
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);

        this.createObjectNode(model.getId());
        return ResponseEntity.ok(model.getId());
    }

    /**
     * 创建模型时完善ModelEditorSource
     *
     * @param modelId modelId
     */
    private void createObjectNode(String modelId) {
        log.info("创建模型完善ModelEditorSource入参模型ID：{}", modelId);
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.set("stencilset", stencilSetNode);
        try {
            repositoryService.addModelEditorSource(modelId, editorNode.toString().getBytes(UTF_8));
        } catch (Exception e) {
            log.info("创建模型时完善ModelEditorSource服务异常：{}", e);
        }
        log.info("创建模型完善ModelEditorSource结束");
    }

    /**
     * 保存流程
     *
     * @param modelId 模型ID
     * @param dto     流程模型
     */
    @PutMapping("/service/model/{modelId}/save")
    public ResponseEntity<Void> saveModel(@PathVariable String modelId, ModelDto dto) {
        try {

            Model model = repositoryService.getModel(modelId);

            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelJson.put(MODEL_NAME, dto.getName());
            modelJson.put(MODEL_DESCRIPTION, dto.getDescription());

            model.setMetaInfo(modelJson.toString());
            model.setName(dto.getName());
            repositoryService.saveModel(model);

            repositoryService.addModelEditorSource(model.getId(), dto.getJson_xml().getBytes(UTF_8));

            InputStream svgStream = new ByteArrayInputStream(dto.getSvg_xml().getBytes(UTF_8));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder tc = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            tc.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();
        } catch (Exception e) {
            log.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 发布流程
     *
     * @param modelId 模型ID
     */
    @PutMapping("/model/publish/{modelId}")
    public ResponseEntity publish(@PathVariable String modelId) {
        log.info("流程部署入参modelId：{}", modelId);
        try {
            Model modelData = repositoryService.getModel(modelId);
            byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
            if (bytes == null) {
                log.info("部署ID:{}的模型数据为空，请先设计流程并成功保存，再进行发布", modelId);
                throw new BadRequestAlertException("deploy data is null", modelId, "model.data.is.null");
            }
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            Deployment deployment = repositoryService.createDeployment()
                    .name(modelData.getName())
                    .addBpmnModel(modelData.getKey() + ".bpmn20.xml", model)
                    .deploy();
            modelData.setDeploymentId(deployment.getId());
            repositoryService.saveModel(modelData);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.info("部署modelId:{}模型服务异常：{}", modelId, e);
            throw new BadRequestAlertException("deploy exception", modelId, "");
        }
    }

    /**
     * 撤销流程定义
     *
     * @param modelId 模型ID
     */
    @DeleteMapping("/model/revokePublish/{modelId}")
    public ResponseEntity revokePublish(@PathVariable String modelId) {
        log.info("撤销发布流程入参modelId：{}", modelId);
        Model modelData = repositoryService.getModel(modelId);
        if (null == modelData) {
            throw new BadRequestAlertException("revokePublish model data is null", modelId, "");
        }
        try {
            /*
             * 参数不加true:为普通删除，如果当前规则下有正在执行的流程，则抛异常
             * 参数加true:为级联删除,会删除和当前规则相关的所有信息，包括历史
             */
            repositoryService.deleteDeployment(modelData.getDeploymentId(), true);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("撤销已部署流程服务异常：{}", e);
            throw new BadRequestAlertException("revokePublish exception", modelId, "");
        }

    }

    /**
     * 删除模型
     *
     */
    @DeleteMapping("/model/delete")
    public ResponseEntity<Void> deleteModel(@RequestParam String[] ids){
        for(String id: ids){
            log.info("删除流程实例入参modelId：{}", id);
            repositoryService.deleteModel(id);
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("/service/editor/stencilset")
    public String getTranslationSet() {
        log.info("get translation set-----------");
        // translation.json文件放置的路径匹配,否则进入到在线编辑器页面会是一片空白,没有菜单等显示信息
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/stencilset.json");
        try {
            return IOUtils.toString(inputStream, "utf-8");
        } catch (Exception e) {
            throw new ActivitiException("Error while loading translation set", e);
        }
    }

    @GetMapping("/service/model/{modelId}/json")
    public ObjectNode getEditorJson(@PathVariable String modelId) {
        ObjectNode modelNode = null;

        log.info("---------------------------getEditorJson---------");
        Model model = this.repositoryService.getModel(modelId);

        if (model != null) {
            try {
                if (StringUtils.isNotEmpty(model.getMetaInfo())) {
                    modelNode = (ObjectNode) this.objectMapper.readTree(model.getMetaInfo());
                } else {
                    modelNode = this.objectMapper.createObjectNode();
                    modelNode.put(MODEL_NAME, model.getName());
                }
                modelNode.put(MODEL_ID, model.getId());
                ObjectNode editorJsonNode = (ObjectNode) this.objectMapper.readTree(
                        new String(this.repositoryService.getModelEditorSource(model.getId()), UTF_8));
                modelNode.set("model", editorJsonNode);
            } catch (Exception e) {
                log.error("Error creating model JSON", e);
                throw new ActivitiException("Error creating model JSON", e);
            }
        }
        return modelNode;
    }

    /**
     * 获取 Hibernate FirstResult
     */
    public static int getFirstResult(Query query, Long total){
        int firstResult = (query.getPage() - 1) * query.getLimit();
        if (firstResult >= total) {
            firstResult = 0;
        }
        return firstResult;
    }
}

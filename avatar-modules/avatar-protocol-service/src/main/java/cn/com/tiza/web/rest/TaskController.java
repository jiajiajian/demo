package cn.com.tiza.web.rest;

import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.service.TaskService;
import cn.com.tiza.service.dto.TaskQuery;
import cn.com.tiza.service.dto.VehicleDto;
import cn.com.tiza.vm.ForwardJobVM;
import cn.com.tiza.vm.TaskDto;
import cn.com.tiza.vm.TaskVM;
import cn.com.tiza.web.rest.dto.PubFileDto;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.util.PaginationUtil;
import cn.com.tiza.web.rest.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 转发配置
 *
 * @author villas
 */
@Slf4j
@RestController
@RequestMapping("/forwardConfig")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private VehicleClient vehicleClient;

    @GetMapping
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<TaskVM>> list(TaskQuery query) {
        PageQuery<TaskVM> pageQuery = taskService.findAll(query);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageQuery);
        return new ResponseEntity(pageQuery.getList(), headers, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(taskService.get(id));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid TaskDto dto) {
        TaskVM newObj = taskService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody @Valid TaskDto dto) {
        taskService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(@RequestParam Long[] ids) {
        taskService.delete(ids);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取任务配置里面的用户名密码列表
     *
     * @param taskId 任务配置表id
     * @return 用户集合
     */
    @GetMapping("/{taskId}/jobs")
    public ResponseEntity<List<ForwardJobVM>> userList(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.queryJobs(taskId));
    }

    @PostMapping("/bind/{taskId}/vehicles")
    public ResponseEntity<Integer> bindVehicles(@PathVariable Long taskId, @RequestBody List<String> vinList) {
        return ResponseEntity.ok(taskService.bind(taskId, vinList));
    }

    @DeleteMapping("/unbind/{taskId}/vehicles")
    public ResponseEntity<Integer> unbindVehicles(@PathVariable Long taskId, @RequestParam String[] vinArr) {
        taskService.unbind(taskId, vinArr);
        return ResponseEntity.ok(vinArr.length);
    }


    @PostMapping("/bind/import/{taskId}")
    public ResponseEntity importVehicle(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) {
        int count = 0;
        try (ExcelReader reader = ExcelReader.createInstance(2, VehicleDto::new)) {
            List<VehicleDto> list = reader.create(file.getInputStream()).resolve();
            Set<String> vinSet = new HashSet<>();
            if (!list.isEmpty()) {
                List<String> allVin = taskService.queryAllVin();
                List<String> boundVin = taskService.findBoundVinByTask(taskId);

                list.forEach(dto -> {
                    if (vinSet.contains(dto.getVin())) {
                        reader.addCellError(dto, 0, "VIN重复");
                    }
                    vinSet.add(dto.getVin());
                    if (!allVin.contains(dto.getVin())) {
                        reader.addCellError(dto, 0, "VIN不存在");
                    }
                    if (boundVin.contains(dto.getVin())) {
                        reader.addCellError(dto, 0, "VIN已关联");
                    }
                });
            }
            if (reader.hasError()) {
                String fileId = vehicleClient.saveToDb(PubFileDto.buildTempFile(file.getOriginalFilename(),
                        reader.writeErrorFileToBytes()));
                throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
            }
            if (!list.isEmpty()) {
                count = taskService.bind(taskId, list.stream().map(VehicleDto::getVin).collect(Collectors.toList()));
            }
        } catch (IOException e) {
            log.error("import vehicle exception" + e.getMessage());
            throw new BadRequestException("upload_excel_error");
        }
        return ResponseEntity.ok(count);
    }


    @GetMapping("{taskId}/run")
    public ResponseEntity<Void> run(@PathVariable Long taskId) {
        taskService.run(taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{taskId}/stop")
    public ResponseEntity<Void> stop(@PathVariable Long taskId) {
        taskService.stop(taskId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{taskId}/status")
    public ResponseEntity<Map<String, String>> queryTaskStatus(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.queryTaskStatus(taskId));
    }
}

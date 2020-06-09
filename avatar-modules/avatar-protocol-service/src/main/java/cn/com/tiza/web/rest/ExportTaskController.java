package cn.com.tiza.web.rest;

import cn.com.tiza.domain.ExportTask;
import cn.com.tiza.excel.read.ExcelReader;
import cn.com.tiza.service.ExportTaskService;
import cn.com.tiza.service.TaskService;
import cn.com.tiza.service.dto.ExportTaskDto;
import cn.com.tiza.service.dto.VehicleDto;
import cn.com.tiza.service.jobs.ForwardDataCompressJob;
import cn.com.tiza.service.mapper.ExportTaskMapper;
import cn.com.tiza.web.rest.dto.PubFileDto;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.errors.ErrorConstants;
import cn.com.tiza.web.rest.util.ResponseUtil;
import cn.com.tiza.web.rest.vm.ExportTaskVM;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author villas
 */
@Slf4j
@RestController
@RequestMapping("exportTask")
public class ExportTaskController {

    @Autowired
    private ExportTaskService exportTaskService;

    @Autowired
    private ExportTaskMapper exportTaskMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private VehicleClient vehicleClient;

    @Autowired
    private ForwardDataCompressJob forwardDataCompressJob;

    @GetMapping
    public ResponseEntity<List<ExportTaskVM>> list() {
        List<ExportTask> tasks = exportTaskService.findAll();
        return ResponseEntity.ok(exportTaskMapper.entitiesToVMList(tasks));
    }

    @GetMapping("{id}")
    public ResponseEntity<ExportTaskVM> get(@PathVariable Long id) {
        return ResponseUtil.wrapOrNotFound(exportTaskService.get(id).map(obj -> exportTaskMapper.toVM(obj)));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid ExportTaskDto dto) {
        ExportTask newObj = exportTaskService.create(dto);
        return ResponseEntity.ok(newObj);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody ExportTaskDto dto) {
        Optional<ExportTask> updatedObj = exportTaskService.update(id, dto);
        return ResponseUtil.wrapOrNotFound(updatedObj);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exportTaskService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{taskId}/upload")
    public ResponseEntity upload(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) {
        List<VehicleDto> list = new ArrayList<>();
        try (ExcelReader reader = ExcelReader.createInstance(2, VehicleDto::new)) {
            list = reader.create(file.getInputStream()).resolve();
            Set<String> vinSet = new HashSet<>();
            if (!list.isEmpty()) {
                List<String> boundVin = taskService.findBoundVinByTask(taskId);
                list.forEach(dto -> {
                    if (vinSet.contains(dto.getVin())) {
                        reader.addCellError(dto, 0, "VIN重复");
                    }
                    vinSet.add(dto.getVin());
                    if (!boundVin.contains(dto.getVin())) {
                        reader.addCellError(dto, 0, "该车辆不在您所选择的转发任务中");
                    }
                });
            }
            if (reader.hasError()) {
                String fileId = vehicleClient.saveToDb(PubFileDto.buildTempFile(file.getOriginalFilename(),
                        reader.writeErrorFileToBytes()));
                throw new BadRequestException(ErrorConstants.IMPORT_EXCEL_FILE_ERROR, fileId);
            }
        } catch (IOException e) {
            log.error("import vehicle exception" + e.getMessage());
        }
        // 3.将解析好的合法的vin集合返回给前端
        if (CollectionUtils.isEmpty(list)) {
            return ResponseEntity.ok(Lists.newArrayList());
        }
        return ResponseEntity.ok(list.stream().map(VehicleDto::getVin).collect(Collectors.toList()));
    }

    @GetMapping("download/{id}")
    public ResponseEntity<InputStreamResource> downloadExportReport(@PathVariable Long id) {
        String path = exportTaskService.get(id)
                .map(ExportTask::getFilePath)
                .map(s -> s.substring(s.indexOf('|') + 1))
                .orElse(null);
        return this.getInputStreamResourceResponseEntity(path);
    }

    public ResponseEntity<InputStreamResource> getInputStreamResourceResponseEntity(String path) {
        Objects.requireNonNull(path, "文件路径不能为空！");
        FileSystemResource file = new FileSystemResource(path);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        String filename = file.getFilename();
        Objects.requireNonNull(filename, "文件名不能为空！");
        filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", filename));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        try {
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(file.contentLength())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(file.getInputStream()));
        } catch (IOException e) {
            log.error("downloadExportReport exception is {}, file path is {}", e.getMessage(), path);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/forwardDataCompressJob")
    public ResponseEntity<Void> forwardDataCompressJob() {
        forwardDataCompressJob.executeInternal();
        return ResponseEntity.ok().build();
    }
}

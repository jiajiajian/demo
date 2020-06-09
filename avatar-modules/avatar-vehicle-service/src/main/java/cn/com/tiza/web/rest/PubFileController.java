package cn.com.tiza.web.rest;

import cn.com.tiza.dao.FileContentDao;
import cn.com.tiza.dao.FileDao;
import cn.com.tiza.domain.File;
import cn.com.tiza.domain.FileContent;
import cn.com.tiza.service.PubFileService;
import cn.com.tiza.web.rest.dto.PubFileDto;
import cn.com.tiza.service.mapper.PubFileMapper;
import cn.com.tiza.util.EncodeUtil;
import cn.com.tiza.web.rest.errors.BadRequestException;
import cn.com.tiza.web.rest.vm.PubFileVM;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 文件服务接口
 *
 * @author tiza
 */
@Api("文件服务接口")
@Slf4j
@RestController
@RequestMapping("/pubFiles")
public class PubFileController {

    @Autowired
    private PubFileService pubFileService;

    @Autowired
    private FileDao pubFileDao;

    @Autowired
    private FileContentDao fileContentDao;

    @Autowired
    private PubFileMapper pubFileMapper;

    @PostMapping
    public String create(@RequestBody PubFileDto file) {
        return pubFileService.create(file).toString();
    }

    @DeleteMapping
    public void delete(@RequestParam("id") Long id) {
        pubFileService.delete(id);
    }

    /**
     * 文件上传
     *
     * @param files 上传的文件
     * @param names 文件名通过参数提交上来，避免文件名在不同环境下乱码
     * @return
     */
    @PostMapping("/upload")
    public Long[] upload(@RequestParam("files") MultipartFile[] files, @RequestParam("names") String[] names,
                         @RequestParam("type") String fileType) {

        List<PubFileDto> saveFiles = new ArrayList<>(files.length);
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                if (file != null && file.getBytes().length > 0) {
                    saveFiles.add(new PubFileDto(file.getOriginalFilename(), fileType, file.getBytes()));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BadRequestException(e.getMessage());
            }
        }
        return pubFileService.createFiles(saveFiles);
    }

    @GetMapping("/download/{fileId}")
    public void download(@PathVariable Long fileId, HttpServletRequest request, HttpServletResponse response) {
        if (fileId != null) {
            File file = pubFileDao.single(fileId);
            FileContent content = fileContentDao.single(fileId);
            setDownloadContent(file.getName(), request, response);
            // 重要，需要设置此值，否则下载后打开excel会提示文件需要修复
            response.setContentLength((int) file.getFileSize());
            try (OutputStream out = response.getOutputStream();) {
                out.write(content.getContent());
                out.flush();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new BadRequestException(e.getMessage());
            }
        }
    }

    @GetMapping("/{fileId}")
    public PubFileVM getById(@PathVariable Long fileId) {
        File file = pubFileDao.single(fileId);
        FileContent content = fileContentDao.single(fileId);
        file.setContent(content);
        return pubFileMapper.toVM(file);
    }

    /**
     * 客户端下载文件上response header的设置。
     *
     * @param fileName 文件名
     * @param request  请求
     * @param response 响应
     */
    private void setDownloadContent(String fileName, HttpServletRequest request, HttpServletResponse response) {
        String agent = request.getHeader("User-Agent");
        try {
            if (null != agent && agent.toUpperCase().indexOf("MSIE") > 0) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e1) {
        }
        response.setContentType("application/x-msdownload;");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
    }

    /**
     * 下载图片转base64
     *
     * @param id
     * @return
     */
    @GetMapping("/downloadPicture/base64/{id}")
    public ResponseEntity<Map> downloadMapPicture(@PathVariable Long id) {
        HashMap result = Optional.ofNullable(fileContentDao.single(id)).map(fileContent -> {
                    File file = pubFileDao.single(id);
                    String[] nameSplit = file.getName().split("\\.");
                    String content = String.format("data:image/%s;base64,", nameSplit[nameSplit.length - 1])
                            + EncodeUtil.encodeBase64(fileContent.getContent());
                    HashMap map = new HashMap<>(1);
                    map.put("content", content);
                    return map;
                }

        ).orElseGet(HashMap::new);

        return ResponseEntity.ok(result);
    }

}

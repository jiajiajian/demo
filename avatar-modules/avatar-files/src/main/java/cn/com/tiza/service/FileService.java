package cn.com.tiza.service;


import cn.com.tiza.dao.FileContentDao;
import cn.com.tiza.dao.FileDao;
import cn.com.tiza.domain.File;
import cn.com.tiza.service.dto.FileDto;
import cn.com.tiza.service.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author: TZ0829
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class FileService {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FileContentDao fileContentDao;

    @Autowired
    private FileMapper pubFileMapper;

    public Long create(FileDto command) {
        File file = pubFileMapper.dtoToEntity(command);
        fileDao.insert(file, true);
        file.getContent().setId(file.getId());
        fileContentDao.insert(file.getContent());
        return file.getId();
    }

    public Long[] createFiles(List<FileDto> commands) {
        Long[] ids = new Long[commands.size()];
        for (int i = 0; i < commands.size(); i++) {
            FileDto command = commands.get(i);
            ids[i] = create(command);
        }
        return ids;
    }

    public void delete(Long id) {
        Optional.ofNullable(id).ifPresent(obj -> {
            fileDao.deleteById(obj);
            fileContentDao.deleteById(obj);
        });
    }

    public Long saveFile(MultipartFile file, String objectType) {
        try {
            FileDto pubFileDto = new FileDto(file.getOriginalFilename(), objectType, file.getBytes());
            return this.create(pubFileDto);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}

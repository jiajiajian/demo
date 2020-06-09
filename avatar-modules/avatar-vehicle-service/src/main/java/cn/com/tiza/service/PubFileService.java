package cn.com.tiza.service;


import cn.com.tiza.dao.FileContentDao;
import cn.com.tiza.dao.FileDao;
import cn.com.tiza.domain.File;
import cn.com.tiza.web.rest.dto.PubFileDto;
import cn.com.tiza.service.mapper.PubFileMapper;
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
 * @create: 2018-09-29
 **/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PubFileService {

    @Autowired
    private FileDao fileDao;

    @Autowired
    private FileContentDao fileContentDao;

    @Autowired
    private PubFileMapper pubFileMapper;

    public Long create(PubFileDto command) {
        File file = pubFileMapper.dtoToEntity(command);
        fileDao.insert(file, true);
        file.getContent().setId(file.getId());
        fileContentDao.insert(file.getContent());
        return file.getId();
    }

    public Long[] createFiles(List<PubFileDto> commands) {
        Long[] ids = new Long[commands.size()];
        for (int i = 0; i < commands.size(); i++) {
            PubFileDto command = commands.get(i);
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

    public int removeInvalidFiles() {
        return fileDao.removeInvalidFiles();
    }


    public Long saveFile(MultipartFile file, String objectType) {
        try {
            PubFileDto pubFileDto = new PubFileDto(file.getOriginalFilename(), objectType, file.getBytes());
            return this.create(pubFileDto);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}

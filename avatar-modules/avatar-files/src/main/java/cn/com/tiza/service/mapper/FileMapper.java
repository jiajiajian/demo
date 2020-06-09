package cn.com.tiza.service.mapper;


import cn.com.tiza.domain.File;
import cn.com.tiza.domain.FileContent;
import cn.com.tiza.service.dto.FileDto;
import cn.com.tiza.web.rest.vm.FileVM;
import org.springframework.stereotype.Component;

/**
 * Mapper
 *
 * @author
 */
@Component
public class FileMapper {

    public File dtoToEntity(FileDto dto) {
        if (dto == null) {
            return null;
        }
        File entity = new File();
        entity.setContent(new FileContent());
        entity.getContent().setContent(dto.getContent());
        entity.setDescription(dto.getDescription());
        entity.setFileSize(dto.getFileSize());
        entity.setName(dto.getName());
        entity.setObjectId(dto.getObjectId());
        entity.setObjectType(dto.getObjectType());
        entity.setCreateDate(System.currentTimeMillis());
        return entity;
    }

    public FileVM toVM(File entity) {
        if (entity == null) {
            return null;
        }
        FileVM vm = new FileVM();
        vm.setId(entity.getId());
        vm.setCreateDate(entity.getCreateDate());
        vm.setCreateUser(entity.getCreateUser());
        vm.setDescription(entity.getDescription());
        vm.setFileSize(entity.getFileSize());
        vm.setName(entity.getName());
        vm.setObjectId(entity.getObjectId());
        vm.setObjectType(entity.getObjectType());
        vm.setContent(entity.getContent().getContent());
        return vm;
    }
}

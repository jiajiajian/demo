package cn.com.tiza.service.mapper;


import cn.com.tiza.domain.File;
import cn.com.tiza.domain.FileContent;
import cn.com.tiza.web.rest.dto.PubFileDto;
import cn.com.tiza.web.rest.vm.PubFileVM;
import org.springframework.stereotype.Component;

/**
 * Mapper
 *
 * @author
 */
@Component
public class PubFileMapper {

    public File dtoToEntity(PubFileDto dto) {
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

    public PubFileVM toVM(File entity) {
        if (entity == null) {
            return null;
        }
        PubFileVM vm = new PubFileVM();
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

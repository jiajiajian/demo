package cn.com.tiza.service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author tz0920
 */
@Data
public class FaultDictItemImportCommand {
    private Long rootOrgId;
    private MultipartFile file;
}

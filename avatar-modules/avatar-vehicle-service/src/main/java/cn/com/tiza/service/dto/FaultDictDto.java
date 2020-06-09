package cn.com.tiza.service.dto;

import cn.com.tiza.excel.read.ExcelReader;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class FaultDictDto {
    /**
     * 故障字典文件
     */
    private String fileName;
    /**
     * 故障字典名称
     */
    @NotNull
    private String name;
    /**
     * 机构ID
     */
    @NotNull
    private Long organizationId;
    private MultipartFile file;

    private List<FaultDictItemDto> items;

    public FaultDictDto() {
    }

    /**
     * 验证故障码不能重复
     *
     * @param reader
     */
    public void validate(ExcelReader reader) {
        //CODE 不能重复
        Set<String> codes = new HashSet<>();
        /*items.forEach(item -> {
            if (codes.contains(item.getCode())) {
                reader.addCellError(item, 0, "故障码重复");
            }
            codes.add(item.getCode());
        });*/
        codes.clear();
    }

}

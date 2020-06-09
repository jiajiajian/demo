package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * 文件VM
 *
 * @author
 */
@Data
public class FileVM {

    private Long id;
    private Long createDate;
    private Long createUser;
    private String description;
    private Integer fileSize;
    private String name;
    private Long objectId;
    private String objectType;
    private byte[] content;

    public FileVM() {
    }


}

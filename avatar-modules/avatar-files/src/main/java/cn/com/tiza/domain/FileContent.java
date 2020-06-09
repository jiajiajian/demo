package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * 文件内容表
 *
 * @author TZ0781
 */
@Data
@Table(name = "BASE_FILE_CONTENT")
public class FileContent implements Serializable {

    @AssignID()
    private Long id;
    private byte[] content;

    public FileContent() {
    }

}

package cn.com.tiza.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

/**
 * 文件表
 *
 * @author :
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "BASE_FILE")
public class File {

    @AutoID
    @AssignID("simple")
    Long id;

    /**
     * 文件名称
     */
    String name;

    /**
     * 关联对象ID
     */
    Long objectId;

    /**
     * 关联对象类型
     */
    String objectType;

    /**
     * 文件长度
     */
    Integer fileSize;

    /**
     * 备注
     */
    String description;

    Long createDate;

    Long createUser;

    FileContent content;

}

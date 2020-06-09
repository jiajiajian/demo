package cn.com.tiza.web.rest.dto;


import lombok.Data;

/**
 * gen by beetlsql 2019-04-08
 * @author tiza
 */
@Data
public class PubFileDto {

    private byte[] content;
    private String description;
    private Integer fileSize;
    private String name;
    private Long objectId;
    private String objectType;

    public PubFileDto() {
    }

    /**
     * 构造一个文件对象
     * @param fileName 文件名
     * @param fileType 关联的对象名
     * @param content 文件内容
     */
    public PubFileDto(String fileName, String fileType, byte[] content) {
        //判断是否为IE浏览器的文件名，IE浏览器下文件名会带有盘符信息
        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = Math.max(winSep, unixSep);
        if (pos != -1)  {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        this.name = fileName;
        this.objectType = fileType;
        this.content = content;
        this.fileSize = content.length;
    }

    public static PubFileDto buildTempFile(String fileName, byte[] content) {
        return new PubFileDto(fileName, FileType.TEMP.name(), content);
    }

}

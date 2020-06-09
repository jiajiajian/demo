package cn.com.tiza.web.rest;


import cn.com.tiza.web.Servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 公共Controller
 *
 * @author tiza
 */
public interface BaseController {

    /**
     * 设置excel下载header信息
     *
     * @param request  request
     * @param response response
     * @param fileName file name
     */
    default void beforeExcelDownload(HttpServletRequest request, HttpServletResponse response, String fileName) {
        //输出Excel文件.
        response.setContentType("application/vnd.ms-excel; charset=UTF-8");
        Servlets.setFileDownloadHeader(request, response, fileName + System.currentTimeMillis() + ".xlsx");
    }

    /**
     * get file name without directory
     *
     * @param fileName file name
     * @return file name without directory
     */
    default String getFileName(String fileName) {
        if (fileName.contains(File.separator)) {
            return fileName.substring(fileName.lastIndexOf(File.separator));
        }
        return fileName;
    }

}

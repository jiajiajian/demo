package cn.com.tiza.rest;

import cn.com.tiza.excel.BeanParser;
import cn.com.tiza.excel.ExcelCreator;
import cn.com.tiza.web.rest.BaseController;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 文件下载
 * @author tiza
 */
@Slf4j
public abstract class ExcelController implements BaseController {

    /**
     * 下载excel
     * @param name 文件名
     * @param titles 表格头
     * @param data 数据
     * @param parser 数据转换类
     * @param request http request
     * @param response http response
     * @param <T> 类型
     */
    protected <T> void download(String name, String[] titles, List<T> data, BeanParser<T> parser,
                             HttpServletRequest request, HttpServletResponse response) {
        download(name, null, titles, data, parser, request, response);
    }

    /**
     * 下载excel
     * @param name 文件名
     * @param headers excel 头
     * @param titles 表格头
     * @param data 数据
     * @param parser 数据转换类
     * @param request http request
     * @param response http response
     * @param <T> 类型
     */
    protected <T> void download(String name, List<String[]> headers, String[] titles, List<T> data, BeanParser<T> parser,
                                HttpServletRequest request, HttpServletResponse response) {
        ExcelCreator creator = ExcelCreator.newInstance().init()
                .addSheet(name, headers, titles, parser, null, data);
        download(name, creator, request, response);
    }

    /**
     * 下载excel
     * @param name 文件名
     * @param request http request
     * @param response http response
     */
    protected void download(String name, ExcelCreator creator,
                                HttpServletRequest request, HttpServletResponse response) {
        beforeExcelDownload(request, response, name);
        try (OutputStream out = response.getOutputStream()) {
            creator.create(out);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}

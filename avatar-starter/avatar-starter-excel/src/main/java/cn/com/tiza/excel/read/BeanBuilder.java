package cn.com.tiza.excel.read;

import org.apache.poi.ss.usermodel.Row;

/**
 * bean 构建器接口
 *
 * @author tiza
 * @param <T> 要构建bean的类型
 */
public interface BeanBuilder<T> {

    /**
     * 将Excel的行转换为Bean
     *
     * @param sheetIdx
     * @param row
     * @return
     */
    T build(int sheetIdx, Row row);

}

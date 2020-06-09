package cn.com.tiza.dao;

import cn.com.tiza.domain.Terminal;
import cn.com.tiza.service.dto.GeneralDto;
import cn.com.tiza.service.dto.TerminalDto;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;

public interface TerminalDao extends BaseMapper<Terminal> {

    void pageQuery(PageQuery pageQuery);

    /**
     * 根据终端编号获取 vin码，sim卡号，协议版本号
     *
     * @param code 终端编号
     * @return GeneralDto
     */
    GeneralDto getGeneralDataByCode(@Param("terminalCode") String code);

    /**
     * 根据 机器序列号/终端编号/SIM卡号 获取 vin码，sim卡号，协议版本号
     *
     * @param keyword 机器序列号/终端编号/SIM卡号
     * @return GeneralDto
     */
    GeneralDto getGeneralDataByKeyword(@Param("keyword") String keyword);

    /**
     * 根据vin码获取 vin码，sim卡号，协议版本号
     *
     * @param vin vin码
     * @return GeneralDto
     */
    GeneralDto getGeneralDataByVin(@Param("vin") String vin);

    /**
     * 编辑回显
     *
     * @param id id
     * @return 会显得信息
     */
    TerminalDto getOfUpdate(@Param("id") Long id);
}

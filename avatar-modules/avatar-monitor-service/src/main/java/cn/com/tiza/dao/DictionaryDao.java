package cn.com.tiza.dao;

import cn.com.tiza.domain.BaseDictionary;
import org.beetl.sql.core.mapper.BaseMapper;

/**
 * @author TZ0855
 */
public interface DictionaryDao extends BaseMapper<BaseDictionary> {

    /**
     * 根据code 查询
     *
     * @param code
     * @return
     */
    default BaseDictionary findDictionaryByItemName(String code) {
        return createLambdaQuery().andEq(BaseDictionary::getItemCode, code)
                .single();
    }

}

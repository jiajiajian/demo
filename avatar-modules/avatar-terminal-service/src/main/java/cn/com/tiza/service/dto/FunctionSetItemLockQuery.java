package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Getter
@Setter
public class FunctionSetItemLockQuery extends Query {

    @NotNull
    private Long functionId;

    @Override
    protected void convertParams() {
        add("functionId", this.functionId);
    }

}

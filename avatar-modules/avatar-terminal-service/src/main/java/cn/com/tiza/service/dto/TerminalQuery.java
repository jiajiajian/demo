package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TerminalQuery extends Query {

    private String keyword;
    /**
     * 通信协议
     */
    private Long protocolId;
    /**
     * 软件版本ID
     */
    private Long softVersionId;
    /**
     * 终端型号
     */
    private String terminalModel;

    @Override
    protected void convertParams() {
        add("keyword", this.keyword, true);
        add("protocolId", this.protocolId);
        add("softVersionId", this.softVersionId);
        add("terminalModel", this.terminalModel, true);
    }

}

package cn.com.tiza.web.rest.vm;

import cn.com.tiza.domain.ChargeDetail;
import lombok.Data;

import java.util.List;

/**
 * @author tz0920
 */
@Data
public class ChargeConfigVM {
    private Long id;
    private String terminalModel;
    private List<ChargeDetailVM> chargeDetailList;

}

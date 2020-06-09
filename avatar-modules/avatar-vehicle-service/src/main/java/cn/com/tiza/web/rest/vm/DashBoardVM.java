package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.List;

/**
 * @author tiza
 */
@Data
public class DashBoardVM {
    private String province;
    private Integer count;
    private String city;
    private List<DashBoardVM> list;
    private boolean subShow;
}

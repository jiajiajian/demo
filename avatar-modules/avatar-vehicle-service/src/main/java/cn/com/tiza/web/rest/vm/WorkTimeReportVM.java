package cn.com.tiza.web.rest.vm;

import lombok.Data;

@Data
public class WorkTimeReportVM {
    private String name;
    private double jan;
    private double feb;
    private double mar;
    private double apr;
    private double may;
    private double jun;
    private double jul;
    private double aug;
    private double sep;
    private double oct;
    private double nov;
    private double dec;
    private double total;

    public String[] toRow() {
        String[] arr = new String[14];
        int idx = 0;
        arr[idx] = name ;
        ++idx;
        arr[idx] = String.valueOf(jan);
        ++idx;
        arr[idx] = String.valueOf(feb);;
        ++idx;
        arr[idx] = String.valueOf(mar);;
        ++idx;
        arr[idx] = String.valueOf(apr) ;
        ++idx;
        arr[idx] = String.valueOf(may) ;
        ++idx;
        arr[idx] = String.valueOf(jun) ;
        ++idx;
        arr[idx] = String.valueOf(jul) ;
        ++idx;
        arr[idx] = String.valueOf(aug) ;
        ++idx;
        arr[idx] = String.valueOf(sep) ;
        ++idx;
        arr[idx] = String.valueOf(oct) ;
        ++idx;
        arr[idx] = String.valueOf(nov) ;
        ++idx;
        arr[idx] = String.valueOf(dec) ;
        ++idx;
        arr[idx] = String.valueOf(total) ;
        return arr;
    }
}

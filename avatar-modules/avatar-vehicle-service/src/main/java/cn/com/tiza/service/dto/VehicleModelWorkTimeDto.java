package cn.com.tiza.service.dto;

import lombok.Data;

@Data
public class VehicleModelWorkTimeDto {

    private Long id;

    private String name;

    private int year;

    private int month;

    private double workTime;

}

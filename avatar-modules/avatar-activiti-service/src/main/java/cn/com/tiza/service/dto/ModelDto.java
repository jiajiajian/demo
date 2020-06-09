package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author villas
 */
@Data
public class ModelDto {

    @NotBlank
    private String name;

    @NotBlank
    private String key;

    @NotBlank
    private String category;

    private String description;

    private String json_xml;

    private String svg_xml;


}

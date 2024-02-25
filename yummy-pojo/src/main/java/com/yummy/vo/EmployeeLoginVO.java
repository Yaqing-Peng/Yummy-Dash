package com.yummy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Data returned to front-end after emp login")
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("PK")
    private Long id;

    @ApiModelProperty("user name")
    private String userName;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("jwt token")
    private String token;

}

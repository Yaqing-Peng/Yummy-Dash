package com.yummy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "Emp login data passed by front-end")
public class EmployeeLoginDTO implements Serializable {

    @ApiModelProperty("user name")
    private String username;

    @ApiModelProperty("password")
    private String password;

}

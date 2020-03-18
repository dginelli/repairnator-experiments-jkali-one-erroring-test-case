package com.intive.shopme.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ApiModel
@Builder
public @Data
class User {

    @ApiModelProperty(value = "Represents user's id number")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ApiModelProperty(value = "Represents user's name", required = true)
    private String name;

    @ApiModelProperty(value = "Represents user's email", required = true)
    private String email;

    @ApiModelProperty(value = "Represents user's phone number", required = true)
    private String phoneNumber;

    @ApiModelProperty(value = "Represents additional information typed by user")
    private String additionalInfo;
}
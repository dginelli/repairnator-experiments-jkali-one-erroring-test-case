package com.hedvig.productPricing.service.serviceIntegration.memberService.memberService.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum BankIdStatusType {

    @JsonProperty("OutstandingTransaction")
    OUTSTANDING_TRANSACTION,
    @JsonProperty("NoClient")
    NO_CLIENT,
    @JsonProperty("Started")
    STARTED,
    @JsonProperty("UserSign")
    USER_SIGN,
    @JsonProperty("UserReq")
    USER_REQ,
    @JsonProperty("Complete")
    COMPLETE,
    @JsonProperty("Error")
    ERROR;
}

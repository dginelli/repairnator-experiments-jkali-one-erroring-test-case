package com.hedvig.botService.serviceIntegration.memberService.dto;

import lombok.Value;

@Value
public class BankIdAuthResponse {
    private BankIdStatusType bankIdStatus;
    private String autoStartToken;
    private String referenceToken;
}

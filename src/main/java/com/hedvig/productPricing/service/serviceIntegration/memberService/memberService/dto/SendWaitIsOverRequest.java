package com.hedvig.productPricing.service.serviceIntegration.memberService.memberService.dto;

import lombok.Value;

@Value
public class SendWaitIsOverRequest {
    public String name;
    public String waitlistId;
    public String email;
}

package com.hedvig.botService.serviceIntegration.memberService.dto;

import lombok.Value;

@Value
public class SendOnboardedActiveLaterRequest {
    public String email;
    public String name;
    public String memberId;
}

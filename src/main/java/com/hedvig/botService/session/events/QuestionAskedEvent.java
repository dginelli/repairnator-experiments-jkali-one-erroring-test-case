package com.hedvig.botService.session.events;

import lombok.Value;

@Value
public class QuestionAskedEvent {
    String memberId;
    String question;
}

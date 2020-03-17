package com.hedvig.botService.enteties.message;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("bankidLink")
public class MessageBodyBankIdLink extends MessageBody {
    public String URL;

    public MessageBodyBankIdLink(){}

    public MessageBodyBankIdLink(String URL) {
        super("");
        this.URL = URL;
    }
}

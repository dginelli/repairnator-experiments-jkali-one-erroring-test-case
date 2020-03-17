package com.hedvig.botService.web;

import com.hedvig.botService.enteties.MessageRepository;
import com.hedvig.botService.enteties.message.Message;
import com.hedvig.botService.session.SessionManager;
import com.hedvig.botService.web.dto.BackOfficeAnswerDTO;
import com.hedvig.botService.web.dto.BackOfficeMessageDTO;
import com.hedvig.botService.web.dto.ExpoDeviceInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class InternalMessagesController {

    private final SessionManager sessionManager;

    private final MessageRepository messageRepository;

    @Autowired
    public InternalMessagesController(SessionManager sessions, MessageRepository messageRepository) {
        this.sessionManager = sessions;
        this.messageRepository = messageRepository;
    }

    /**
     * This endpoint is used internally to send messages from back-office personnel to end users
     */
    @RequestMapping(path = {"/_/messages/addmessage", "/addmessage"}, method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity<?> addMessage(@Valid @RequestBody BackOfficeMessageDTO backOfficeMessage) {
        log.info("Message from Hedvig to hid: {} with messageId: {}", backOfficeMessage.userId, backOfficeMessage.msg.globalId);

        sessionManager.addMessageFromHedvig(backOfficeMessage);

        return ResponseEntity.noContent().build();
    }

    /**
     * This endpoint is used internally to question answers from back-office personnel to end users
     */
    @RequestMapping(path = "/_/messages/addanswer", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity<?> addAnswer(@Valid @RequestBody() BackOfficeAnswerDTO backOfficeAnswer) {
        log.info("Received answer from Hedvig to hid: {} with message {}", backOfficeAnswer.getUserId(), backOfficeAnswer.getMsg());

        if(sessionManager.addAnswerFromHedvig(backOfficeAnswer) == true) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @RequestMapping(path = "/_/messages/{from}", method = RequestMethod.GET)
    public List<BackOfficeMessageDTO> messages(@PathVariable Long from) {
        Instant timestamp = Instant.ofEpochMilli(from);
        List<Message> messages = messageRepository.findFromTimestamp(timestamp);

        return messages.stream()
                .map(m -> new BackOfficeMessageDTO(m, m.chat.getMemberId()))
                .collect(Collectors.toList());
    }

    /**
     * Initialize chat with member. The method is used in api-gateway "/helloHedvig" method handler.
     */
    @RequestMapping(path = {"/_/messages/init", "/init"}, method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestHeader(value = "hedvig.token", required = false) String hid, @RequestBody(required = false) ExpoDeviceInfoDTO json) {
        log.info("Init recieved from api-gateway: {}", hid);

        String linkUri = "hedvig://+";
        if (json != null && json.getDeviceInfo() != null) {
            log.info(json.toString());
            linkUri = json.getDeviceInfo().getLinkingUri();
        }
        sessionManager.init(hid, linkUri);

        return ResponseEntity.noContent().build();
    }
}

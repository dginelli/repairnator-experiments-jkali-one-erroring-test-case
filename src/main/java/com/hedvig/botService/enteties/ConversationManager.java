package com.hedvig.botService.enteties;

import com.hedvig.botService.chat.Conversation;
import lombok.Getter;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * All timestamp information is set from here
 * */

@Entity
//@Table(indexes = {
//        @Index(columnList = "id", name = "conversation_manager_id_idx")
//})
public class ConversationManager {

	private static Logger log = LoggerFactory.getLogger(ConversationManager.class);
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    private String memberId;

    @OneToMany(mappedBy="conversationManager", cascade = CascadeType.ALL, orphanRemoval=true)
    public List<ConversationEntity> conversations;

    @OneToOne()
    private ConversationEntity activeConversation;

    public String toString(){	
    	return this.memberId + " " + this.conversations;
    }
    
    public ConversationManager() {
    	//new Exception().printStackTrace(System.out);
        conversations = new ArrayList<>();
    }

    ConversationManager(String memberId) {
    	log.info("Instantiating ConversationManager for member:" + memberId);
        this.memberId = memberId;
        this.conversations = new ArrayList<>();
    }


    
	List<ConversationEntity> getConversations() {
		return conversations;
	}

    boolean startConversation(Class<? extends Conversation> conversationClass) {

        return startConversation(conversationClass, null);
    }

    boolean startConversation(Class<? extends Conversation> conversationClass, String startMessage) {

        for(ConversationEntity c : conversations){
            if(c.getConversationStatus().equals(Conversation.conversationStatus.ONGOING)) {
                if (c.getClassName().equals(conversationClass.getName())) {
                    return false;
                } else {
                    c.setConversationStatus(Conversation.conversationStatus.COMPLETE);
                }
            }
        }



        ConversationEntity conv = new ConversationEntity();
        conv.setClassName(conversationClass.getName());
        conv.setMemberId(getMemberId());
        conv.setConversationStatus(Conversation.conversationStatus.ONGOING);
        if(startMessage != null) {
            conv.setStartMessage(startMessage);
        }

        addConversationAndSetActive(conv);

        return true;
    }

    Optional<ConversationEntity> getActiveConversation() {
        if(this.activeConversation == null) {
            return  conversations.
                    stream().
                    filter(x -> x.getConversationStatus() == Conversation.conversationStatus.ONGOING).
                    findFirst();
        }

        return Optional.of(activeConversation);
    }

    private void addConversationAndSetActive(ConversationEntity c) {
        c.setConversationManager(this);
        conversations.add(c);
        activeConversation = c;
    }
}

package guru.bonacci.oogway.web.events;

import static guru.bonacci.oogway.web.events.WebEventChannels.SPECTRE;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import guru.bonacci.oogway.shareddomain.COMINT;


@MessagingGateway
public interface SpectreGateway {
	
	@Gateway(requestChannel = SPECTRE)
	void send(COMINT comint);
}

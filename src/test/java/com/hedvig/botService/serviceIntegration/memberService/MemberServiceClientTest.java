package com.hedvig.botService.serviceIntegration.memberService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.Fault;
import com.hedvig.botService.BotServiceApplicationTests;
import com.hedvig.botService.serviceIntegration.memberService.dto.APIErrorDTO;
import com.hedvig.botService.serviceIntegration.memberService.dto.BankIdAuthRequest;
import com.hedvig.botService.serviceIntegration.memberService.dto.BankIdAuthResponse;
import com.hedvig.botService.serviceIntegration.memberService.dto.BankIdStatusType;
import feign.FeignException;
import feign.RetryableException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes=BotServiceApplicationTests.class)
@AutoConfigureWireMock(port = 4777)
@TestPropertySource(properties = {
        "hedvig.member-service.url=localhost:4777"

}, locations = "/application-test.yml")
@EnableFeignClients
public class MemberServiceClientTest {

    @Value("${wiremock.server.port}")
    String port;

    //@Autowired
    //RestTemplate template;

    ObjectMapper objectMapper;

    @Autowired
    MemberServiceClient feignClient;

    @Autowired
    ObjectFactory<HttpMessageConverters> feignEncoder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup(){
         /*feignClient = Feign.builder().
                 contract(new SpringMvcContract()).
                 errorDecoder(new MemberServiceErrorDecoder(objectMapper)).
                 encoder(new SpringEncoder(feignEncoder)).
                 decoder(new SpringDecoder(feignEncoder)).
                 target(MemberServiceClient.class, "http://localhost:4777"); */
         objectMapper = new ObjectMapper();
    }

    @Test
    public void auth() throws Exception {

        BankIdAuthResponse response = new BankIdAuthResponse(BankIdStatusType.COMPLETE, "", "");
        stubFor(post(urlEqualTo("/member/bankid/auth")).willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(objectMapper.writeValueAsString(response))));

        //MemberServiceImpl impl = new MemberServiceImpl(template);
        //impl.setMemberServiceLocation("localhost:4777");
        ResponseEntity<BankIdAuthResponse> adadad = feignClient.auth(new BankIdAuthRequest("",""));
        assertThat(adadad.getBody()).isEqualTo(response);
    }

    @Test()
    public void bankIdError() throws Exception {

        APIErrorDTO response = new APIErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, BankIdStatusType.COMPLETE.name(), "");
        stubFor(post(urlEqualTo("/member/bankid/auth")).
                willReturn(aResponse().
                        withHeader("Content-Type", "application/json").
                        withStatus(500)
                        .withBody(objectMapper.writeValueAsString(response))));

        thrown.expect(FeignException.class);
        feignClient.auth(new BankIdAuthRequest("", ""));
    }
    
    @Test()
    public void bankIdError404() throws Exception {

        stubFor(post(urlEqualTo("/member/bankid/auth")).
                willReturn(aResponse().
                        withHeader("Content-Type", "application/json").
                        withStatus(404)
                        .withBody("")));

        thrown.expect(FeignException.class);
        feignClient.auth(new BankIdAuthRequest("", ""));
    }

    @Test()
    public void bankIdErrorConnectionResetByPeer() throws Exception {

        stubFor(post(urlEqualTo("/member/bankid/auth")).
                willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));


        thrown.expect(RetryableException.class);
        feignClient.auth(new BankIdAuthRequest("", ""));

    }

}
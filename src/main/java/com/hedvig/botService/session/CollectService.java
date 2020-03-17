package com.hedvig.botService.session;

import com.hedvig.botService.chat.BankIdChat;
import com.hedvig.botService.enteties.*;
import com.hedvig.botService.serviceIntegration.memberService.MemberProfile;
import com.hedvig.botService.serviceIntegration.memberService.MemberService;
import com.hedvig.botService.serviceIntegration.memberService.dto.BankIdCollectResponse;
import com.hedvig.botService.serviceIntegration.memberService.dto.BankIdProgressStatus;
import com.hedvig.botService.serviceIntegration.memberService.exceptions.BankIdError;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.Duration;
import java.time.Instant;

import static net.logstash.logback.argument.StructuredArguments.value;

public class CollectService {

    private final Logger log = LoggerFactory.getLogger(CollectService.class);

    private final UserContextRepository userContextRepository;
    private final MemberService memberService;

    public CollectService(
            UserContextRepository userContextRepository,
            MemberService memberService) {
        this.userContextRepository = userContextRepository;
        this.memberService = memberService;
    }

    public BankIdCollectResponse collect(String hid, String referenceToken, BankIdChat chat) {
        UserContext uc = userContextRepository.findByMemberId(hid).orElseThrow(() -> new ResourceNotFoundException("Could not find usercontext."));


        try {

            BankIdSessionImpl bankIdSession = uc.getBankIdCollectStatus(referenceToken);
            if(bankIdSession == null) {
                log.error("Could not find referenceToken: {}", value("referenceToken", referenceToken));
                chat.bankIdAuthGeneralCollectError(uc);

                return
                        new BankIdCollectResponse(
                                BankIdProgressStatus.COMPLETE,
                                "",
                                null);

            } else if(bankIdSession.isDone()) {
                log.info("This referenceToken has allready been handled!");
                return
                        new BankIdCollectResponse(
                                BankIdProgressStatus.COMPLETE,
                                referenceToken,
                                null);
            } else if(!allowedToCall(bankIdSession)) {
                log.error("Not allowed to call bankId yet, less than 1s passed since last call: {}", value("referenceToken", referenceToken));

                return
                        new BankIdCollectResponse(
                                BankIdProgressStatus.OUTSTANDING_TRANSACTION,
                                bankIdSession.getReferenceToken(),
                                null);
            }
            BankIdCollectResponse collect = new BankIdCollectResponse(BankIdProgressStatus.OUTSTANDING_TRANSACTION, "", null);
            try{
                collect = memberService.collect(referenceToken, hid);
                BankIdProgressStatus bankIdStatus = collect.getBankIdStatus();
                log.info(
                        "BankIdStatus after collect:{}, memberId:{}, lastCollectionStatus: {}",
                        bankIdStatus.name(),
                        hid,
                        bankIdSession.getLastStatus());

                if (bankIdStatus == BankIdProgressStatus.COMPLETE) {
                    if(bankIdSession.getCollectionType().equals(BankIdSessionImpl.CollectionType.AUTH)) {

                        if (collect.getNewMemberId() != null && !collect.getNewMemberId().equals(hid)) {
                            log.info("Found in memberId in response from memberService. Loading other userContext.");
                            uc = userContextRepository.findByMemberId(collect.getNewMemberId()).
                                    orElseThrow(() -> new RuntimeException("Could not find usercontext fo new memberId."));

                            bankIdSession.setUserContext(uc);
                            chat.bankIdAuthComplete(uc);
                        } else {
                            try {
                                MemberProfile member = memberService.getProfile(collect.getNewMemberId());

                                uc.fillMemberData(member);
                                if(member.getAddress().isPresent()) {
                                    chat.bankIdAuthComplete(uc);
                                }
                                else {
                                    chat.bankIdAuthCompleteNoAddress(uc);
                                }
                            }catch (Exception ex) {
                                log.error("Error loading memberProfile from memberService", ex);
                                chat.couldNotLoadMemberProfile(uc);
                            }
                        }
                        uc.getOnBoardingData().setUserHasAuthedWithBankId(referenceToken);

                    }else if(bankIdSession.getCollectionType().equals(BankIdSessionImpl.CollectionType.SIGN)) {
                        chat.memberSigned(referenceToken, uc);
                    }

                    bankIdSession.setDone();

                }else if(bankIdStatus == BankIdProgressStatus.STARTED){
                    chat.started(uc);
                }else if(bankIdStatus == BankIdProgressStatus.NO_CLIENT){
                    chat.noClient(uc);
                }else if(bankIdStatus == BankIdProgressStatus.USER_SIGN){
                    chat.userSign(uc);
                }
                else if (bankIdStatus == BankIdProgressStatus.OUTSTANDING_TRANSACTION) {
                    chat.oustandingTransaction(uc);
                }
                bankIdSession.update(bankIdStatus);
                return collect;
            }
            catch(BankIdError e) {//Handle error
                log.error("Got bankIderror {} response from member service with reference token: {}",
                        value("referenceToken", referenceToken), e.getErrorType());

                if(bankIdSession.getCollectionType() == BankIdSession.CollectionType.SIGN) {
                    chat.signalSignFailure(e.getErrorType(), e.getMessage(), uc);
                }else if(bankIdSession.getCollectionType() == BankIdSession.CollectionType.AUTH) {
                    chat.signalAuthFailiure(e.getErrorType(), e.getMessage(), uc);
                }

                return createCOMPLETEResponse();
            }catch( FeignException ex) {
                log.error("Error collecting result from member-service ", ex);
                bankIdSession.addError();

                if(bankIdSession.shouldAbort()) {
                    if(bankIdSession.getCollectionType() == BankIdSession.CollectionType.SIGN) {
                        chat.bankIdSignError(uc);
                    }else {
                        chat.bankIdAuthGeneralCollectError(uc);
                    }
                    collect = createCOMPLETEResponse();
                    bankIdSession.setDone();
                }
                //Have hedvig respond with error
                return collect;
            }finally {
                userContextRepository.saveAndFlush(uc);
            }
        }catch( ObjectOptimisticLockingFailureException ex) {
            log.error("Could not save user context: ", ex);
            return new BankIdCollectResponse(BankIdProgressStatus.OUTSTANDING_TRANSACTION, "", "");
        }
    }

    private BankIdCollectResponse createCOMPLETEResponse() {
        return new BankIdCollectResponse(
                BankIdProgressStatus.COMPLETE,
                "",
                null);
    }

    private boolean allowedToCall(BankIdSessionImpl bankIdSessionImpl) {
        Instant now = Instant.now();
        //log.debug("Last call time: {}, currentTime: {}", getLastCallTime(), now);
        return Duration.between(bankIdSessionImpl.getLastCallTime(), now).toMillis() > 1000;
    }
}

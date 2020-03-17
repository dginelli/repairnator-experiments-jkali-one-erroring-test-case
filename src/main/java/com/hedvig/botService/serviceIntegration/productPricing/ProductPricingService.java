package com.hedvig.botService.serviceIntegration.productPricing;

import com.hedvig.botService.enteties.userContextHelpers.UserData;
import com.hedvig.botService.serviceIntegration.productPricing.dto.*;
import com.hedvig.botService.web.dto.InsuranceStatusDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductPricingService {

    private final ProductPricingClient client;
    private static Logger log = LoggerFactory.getLogger(ProductPricingService.class);
    
    @Autowired
    ProductPricingService(ProductPricingClient client) {

        this.client = client;
    }

    public String createProduct(String memberId, UserData data) {

        CalculateQuoteRequest request = new CalculateQuoteRequest();

        request.setMemberId(memberId);
        request.setSsn(data.getSSN());
        request.setBirthDate(data.getBirthDate());
        request.setFirstName(data.getFirstName());
        request.setLastName(data.getFamilyName());
        request.setHouseType(data.getHouseType());
        request.setLivingSpace(data.getLivingSpace());
        request.setPersonsInHouseHold(data.getPersonsInHouseHold());
        request.setStudent(data.isStudent());

        List<SafetyIncreaserType> increasers = new ArrayList<>();
        for(String s :data.getSecurityItems()) {
            SafetyIncreaserType increaser;
            switch (s) {
                case "safety.alarm":
                    increaser = SafetyIncreaserType.SMOKE_ALARM;
                    break;
                case "safety.extinguisher":
                    increaser = SafetyIncreaserType.FIRE_EXTINGUISHER;
                    break;
                case "safety.door":
                    increaser = SafetyIncreaserType.SAFETY_DOOR;
                    break;
                case "safety.gate":
                    increaser = SafetyIncreaserType.GATE;
                    break;
                case "safety.burglaralarm":
                    increaser = SafetyIncreaserType.BURGLAR_ALARM;
                    break;
                case "safety.none":
                    increaser = SafetyIncreaserType.NONE;
                    break;
                default:
                    throw new RuntimeException(String.format("Unknown safety increaser: %s", s));
            }
            increasers.add(increaser);
        }

        request.setSafetyIncreasers(increasers);

        request.setCurrentInsurer(data.getCurrentInsurer());

        Address address = new Address();
        address.setStreet(data.getAddressStreet());
        address.setCity(data.getAddressCity());
        address.setZipCode(data.getAddressZipCode());
        address.setFloor(data.getFloor());
        request.setAddress(address);

        Created result = this.client.createQuote(request).getBody();
        return result.id;
    }

    public void quoteAccepted(String hid) {
        this.client.quoteAccepted(hid);
    }

    public void contractSigned(String memberId, String referenceToken) {
        this.client.contractSigned(new ContractSignedRequest(memberId, referenceToken));
    }

    public void setInsuranceStatus(String hid, String status) {
        this.client.setInsuranceStatus(hid, status);
    }
    
    public String getInsuranceStatus(String hid) {
    	ResponseEntity<InsuranceStatusDTO> isd = this.client.getInsuranceStatus(hid);
    	log.info("Getting insurance status:" + (isd==null?null:isd.getStatusCodeValue()));  	
    	if(isd!=null){
    		return isd.getBody().getInsuranceStatus();
    	}
    	return null;
    }
}

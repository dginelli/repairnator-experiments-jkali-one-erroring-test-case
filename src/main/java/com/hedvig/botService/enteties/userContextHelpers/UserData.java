package com.hedvig.botService.enteties.userContextHelpers;

import com.hedvig.botService.enteties.UserContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserData {

    public static final String MEMBER_BIRTH_DATE = "{MEMBER_BIRTH_DATE}";
    public static final String FIRST_NAME = "{NAME}";
    public static final String EMAIL = "{EMAIL}";
    public static final String FAMILY_NAME = "{FAMILY_NAME}";
    public static final String ADDRESS = "{ADDRESS}";
    public static final String SECURE_ITEM_ = "{SECURE_ITEM_%s}";
    private static final String ADDRESS_CITY = "{ADDRESS_CITY}";
    private static final String ADDRESS_ZIP = "{ADDRESS_ZIP}";
    public static final String SECURE_ITEMS_NO = "{SECURE_ITEMS_NO}";
    public static final String HOUSE = "{HOUSE}";
    public static final String EMAIL_NEWS_LETTER = "{EMAIL_NEWS_LETTER}";
    public static final String RECOMMEND_FRIEND_EMAIL = "{RECOMMEND_FRIEND_EMAIL}";
    public static final String NR_PERSONS = "{NR_PERSONS}";
    public static final String KVM = "{KVM}";
    public static final String SSN = "{SSN}";
    public static final String INSURANCE_COMPANY_TODAY = "{INSURANCE_COMPANY_TODAY}";
    public static final String PRODUCT_ID = "{PRODUCT_ID}";
    public static final String USER_HAS_SIGNED = "{USER_HAS_SIGNED}";
    public static final String USER_AUTHED_BANKID = "USER_AUTHED_BANKID";
    public static final String IS_STUDENT = "{STUDENT}";
    public static final String BANK_ID_MESSAGE = "{BANK_ID_MESSAGE}";
    public static final String FLOOR = "{FLOOR}";
    private final UserContext ctx;

    public UserData(UserContext ctx) {
        this.ctx = ctx;
    }

    public int getAge() {
        String dateString = ctx.getDataEntry(MEMBER_BIRTH_DATE);
        if(dateString==null)return -1;
        LocalDate date = LocalDate.parse(dateString);

        return date.until(LocalDate.now()).getYears();
    }

    public void setBirthDate(LocalDate birthDate) {
        ctx.putUserData(MEMBER_BIRTH_DATE, birthDate.toString());// = birthDate;
    }

    public LocalDate getBirthDate() {
    	String bDate = ctx.getDataEntry(MEMBER_BIRTH_DATE);
    	if(bDate == null) return null;
        return LocalDate.parse(bDate);// = birthDate;
    }

    public void setFirstName(String firstName) {
        ctx.putUserData(FIRST_NAME, firstName);
    }

    public String getFirstName() {
        return ctx.getDataEntry(FIRST_NAME);
    }

    public void setStudent(String student) {
        ctx.putUserData(IS_STUDENT,student);
    }

    public Boolean isStudent() {
        return (ctx.getDataEntry(IS_STUDENT)!=null && ctx.getDataEntry(IS_STUDENT)=="1");
    }
    
    public void setEmail(String email) {
        ctx.putUserData(EMAIL,email);
    }

    public String getEmail() {
        return ctx.getDataEntry(EMAIL);
    }


    public void setFamilyName(String familyName) {
        ctx.putUserData(FAMILY_NAME, familyName);
    }

    public String getFamilyName() {
        return ctx.getDataEntry(FAMILY_NAME);
    }

    public void setAddressStreet(String address) {
        ctx.putUserData(ADDRESS, address);
    }

    public String getAddressStreet() {
        return ctx.getDataEntry(ADDRESS);
    }

    public void setHouseType(String value) {
        ctx.putUserData(HOUSE, value);
    }

    public String getHouseType() {
        return ctx.getDataEntry(HOUSE);
    }

    public void setNewsLetterEmail(String text) {
        ctx.putUserData(EMAIL_NEWS_LETTER, text);
    }

    public String getNewsLetterEmail() {
        return ctx.getDataEntry(EMAIL_NEWS_LETTER);
    }

    public void setRecommendFriendEmail(String text) {
        ctx.putUserData(RECOMMEND_FRIEND_EMAIL, text);
    }

    public void setPersonInHouseHold(int nrPersons) {
        ctx.putUserData(NR_PERSONS, Objects.toString(nrPersons));
    }

    public int getPersonsInHouseHold() {
        return Integer.parseInt(ctx.getDataEntry(NR_PERSONS));
    }

    public void setLivingSpace(Float v) {
        ctx.putUserData(KVM, Objects.toString(Math.round(v)));
    }

    public Float getLivingSpace() {
        return Float.parseFloat(ctx.getDataEntry(KVM));
    }

    public void setSSN(String text) {
        ctx.putUserData(SSN, text);
    }

    public String getSSN() {
        return ctx.getDataEntry(SSN);
    }

    public void setCurrentInsurer(String comp) {
        ctx.putUserData(INSURANCE_COMPANY_TODAY, comp);
    }

    public String getCurrentInsurer() {
        return ctx.getDataEntry(INSURANCE_COMPANY_TODAY);
    }

    public void addSecurityItem(String value) {
        String items = ctx.getDataEntry(SECURE_ITEMS_NO);

        int nrItems = items != null ? Integer.parseInt(items): 0;
        ctx.putUserData(String.format(SECURE_ITEM_, nrItems), value);
        ctx.putUserData(SECURE_ITEMS_NO, Objects.toString(nrItems+1));
    }

    public List<String> getSecurityItems() {
        String nrOfItemsString = ctx.getDataEntry(SECURE_ITEMS_NO);
        int nrOfItems = nrOfItemsString != null ? Integer.parseInt(nrOfItemsString) : 0;
        List<String> retlist = new ArrayList<>();
        for(int i = 0; i < nrOfItems; i++) {
            retlist.add(ctx.getDataEntry(String.format(SECURE_ITEM_, i)));
        }

        return retlist;
    }

    public void setAddressCity(String city) {
        ctx.putUserData(ADDRESS_CITY, city);
    }

    public String getAddressCity() {
        return ctx.getDataEntry(ADDRESS_CITY);
    }

    public void setAddressZipCode(String zipCode) {
        ctx.putUserData(ADDRESS_ZIP, zipCode);
    }

    public String getAddressZipCode() {
        return ctx.getDataEntry(ADDRESS_ZIP);
    }

    public String getProductId() {
        return ctx.getDataEntry(PRODUCT_ID);
    }

    public void setProductId(String productId) {
        ctx.putUserData(PRODUCT_ID, productId);
    }

    public Boolean getUserHasSigned() {
        String value = ctx.getDataEntry(USER_HAS_SIGNED);
        if(value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    public void setUserHasSigned(boolean b) {
        ctx.putUserData(USER_HAS_SIGNED, Objects.toString(b));
    }

    public boolean userHasAuthedWithBankId(String referenceId) {
        String b = ctx.getDataEntry(USER_AUTHED_BANKID);
        return b != null && b.equals(referenceId);
    }

    public void setBankIdMessage(String bankIdMessage) {
        ctx.putUserData(BANK_ID_MESSAGE, bankIdMessage);
    }

    public String getBankIdMessage() {
        return ctx.getDataEntry(BANK_ID_MESSAGE);
    }

    public void setUserHasAuthedWithBankId(String referenceId) {
        ctx.putUserData(USER_AUTHED_BANKID, referenceId);
    }

    public void clear() {
        ctx.removeDataEntry(MEMBER_BIRTH_DATE);
        ctx.removeDataEntry(FIRST_NAME);
        ctx.removeDataEntry(FAMILY_NAME);
        ctx.removeDataEntry(EMAIL);
        ctx.removeDataEntry(ADDRESS);
        ctx.removeDataEntry(ADDRESS_CITY);
        ctx.removeDataEntry(ADDRESS_ZIP);
        ctx.removeDataEntry(HOUSE);
        ctx.removeDataEntry(EMAIL_NEWS_LETTER);
        ctx.removeDataEntry(RECOMMEND_FRIEND_EMAIL);
        ctx.removeDataEntry(NR_PERSONS);
        ctx.removeDataEntry(KVM);
        ctx.removeDataEntry(SSN);
        ctx.removeDataEntry(INSURANCE_COMPANY_TODAY);
        ctx.removeDataEntry(PRODUCT_ID);
        ctx.removeDataEntry(RECOMMEND_FRIEND_EMAIL);
        ctx.removeDataEntry(USER_HAS_SIGNED);
        ctx.removeDataEntry(USER_AUTHED_BANKID);

        String nrOfItemsString = ctx.getDataEntry(SECURE_ITEMS_NO);
        int nrOfItems = nrOfItemsString != null ? Integer.parseInt(nrOfItemsString) : 0;
        for(int i = 0; i < nrOfItems; i++) {
            ctx.removeDataEntry(String.format(SECURE_ITEM_, i));
        }

        ctx.removeDataEntry(SECURE_ITEMS_NO);
    }


    public void setFloor(int floor) {
        ctx.putUserData(FLOOR, Integer.toString(floor));
    }

    public Integer getFloor(){
        try {
            return Integer.parseInt(ctx.getDataEntry(FLOOR));
        }catch (Exception ex){
            return 0;
        }
    }
}

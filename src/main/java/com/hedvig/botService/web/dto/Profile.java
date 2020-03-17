package com.hedvig.botService.web.dto;

import lombok.Value;

import java.util.List;


@Value
public class Profile {

    private String name;
    private String firstName;
    private String lastName;
    private List<String> familyMembers;
    private Integer age;
    private String email;
    private String address;
    private Integer livingAreaSqm;
    private String maskedBankAccountNumber;
    private String selectedCashback;

}

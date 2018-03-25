package com.example.troll.contacts;

import java.io.Serializable;

/**
 * Created by troll on 22-03-2018.
 */

public class contactNumber implements Serializable{
    public String countryCode,contactType;
    String number;

    public contactNumber(String countryCode, String contactType, String number) {
        this.countryCode = countryCode;
        this.contactType = contactType;
        this.number = number;
    }

    public contactNumber()
    {

    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getContactType() {
        return contactType;
    }

    public String getNumber() {
        return number;
    }
}

package com.example.troll.contacts;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by troll on 22-03-2018.
 */

public class nContact implements Serializable{
    String firstName,lastName,address;
    ArrayList<contactNumber> contactNumbers;

    public nContact(){
        contactNumbers=new ArrayList<>();
    }

    public nContact(String firstName, String lastName, String address,contactNumber cn) {
        contactNumbers=new ArrayList<>();
        contactNumbers.add(cn);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public void addNumner(contactNumber cn)
    {
        if(contactNumbers==null)
        {

        }
        contactNumbers.add(cn);
    }
    public ArrayList<contactNumber> getContactNumbers() {
        return contactNumbers;
    }
}

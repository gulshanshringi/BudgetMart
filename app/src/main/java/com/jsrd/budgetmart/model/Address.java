package com.jsrd.budgetmart.model;

public class Address {
    private String addressId;
    private String name;
    private String address;
    private String pincode;
    private String city;
    private String state;
    private String country;
    private String mobileNo;

    public Address(String addressId, String name, String address, String pincode,
                   String city, String state, String country, String mobileNo) {
        this.addressId = addressId;
        this.name = name;
        this.address = address;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.mobileNo = mobileNo;
    }


    public String getAddressId() {
        return addressId;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPincode() {
        return pincode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String toString() {
        return address + " " + pincode + " " + city + " " + state + " " + country + " \n" + name + " \n" + mobileNo;
    }
}

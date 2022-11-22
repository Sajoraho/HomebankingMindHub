package com.mindhub.homebanking.utils;

public class AccountUtils {

    private AccountUtils(){

    }
    public static String getNumberAccount(){
        return "VIN"+getRandomNumber();
    }

    public static long getRandomNumber() {
        return (long) ((Math.random() * (100000001 - 1)) + 1);
    }
}
package com.mindhub.homebanking.utils;

public final class CardUtils {

    private CardUtils() {

    }

    public static Integer getCVV() {
        return getRandomNumber(100, 999);
    }
    public static String getCardNumber() {
        return getRandomNumber(1000, 9999) +
                "" + getRandomNumber(1000, 9999) +
                "" + getRandomNumber(1000, 9999) +
                "" + getRandomNumber(1000, 9999);
    }
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

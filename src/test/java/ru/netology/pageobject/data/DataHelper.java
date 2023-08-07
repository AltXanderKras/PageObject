package ru.netology.pageobject.data;

import lombok.Value;

public class DataHelper {

    private DataHelper() {
    }

    @Value
    public static class VerificationCode {
        private String testCode;
    }

    public static VerificationCode getVerificationTestCode() {
        return new VerificationCode("12345");
    }

    @Value
    public static class UserInfo {
        private String userLogin;
        private String userPassword;
    }

    public static UserInfo getAuthInfo() {
        return new UserInfo("vasya", "qwerty123");
    }

    @Value
    public static class CardInfo {
        private String cardNumber;
    }

    public static CardInfo getCard1() {
        return new CardInfo("5559 0000 0000 0001");
    }

    public static CardInfo getCard2() {
        return new CardInfo("5559 0000 0000 0002");
    }

}

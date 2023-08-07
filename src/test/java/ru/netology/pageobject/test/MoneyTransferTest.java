package ru.netology.pageobject.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.pageobject.data.DataHelper;
import ru.netology.pageobject.data.DataHelper.UserInfo;
import ru.netology.pageobject.data.DataHelper.CardInfo;
import ru.netology.pageobject.data.DataHelper.VerificationCode;
import ru.netology.pageobject.page.DashboardPage;
import ru.netology.pageobject.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    UserInfo validUser1 = DataHelper.getAuthInfo();
    CardInfo card1 = DataHelper.getCard1();
    CardInfo card2 = DataHelper.getCard2();
    VerificationCode verificationCode = DataHelper.getVerificationTestCode();

    DashboardPage dashboardPage;
    int startBalanceOfCard1;
    int startBalanceOfCard2;

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        new LoginPage()
                .login(validUser1)
                .acceptCode(verificationCode.getTestCode());
        dashboardPage = new DashboardPage();

        int difference =
                dashboardPage.getCardBalanceOnPage(card1) - dashboardPage.getCardBalanceOnPage(card2);
        if (difference > 0) {
            dashboardPage
                    .makeTransferTo(card2)
                    .makeTransferFromAndAmount(card1, difference / 2);
        } else if (difference < 0) {
            dashboardPage
                    .makeTransferTo(card1)
                    .makeTransferFromAndAmount(card2, difference / 2);
        }
        startBalanceOfCard1 = dashboardPage.getCardBalanceOnPage(card1);
        startBalanceOfCard2 = dashboardPage.getCardBalanceOnPage(card2);
    }

    @Test
    public void testTransferBetweenCards() {
        //// Перевод межну картами
        int transactionAmount = 8888;

        dashboardPage = new DashboardPage();
        dashboardPage
                .makeTransferTo(card1)
                .makeTransferFromAndAmount(card2, transactionAmount);

        assertEquals(startBalanceOfCard1 + transactionAmount, dashboardPage.getCardBalanceOnPage(card1));
        assertEquals(startBalanceOfCard2 - transactionAmount, dashboardPage.getCardBalanceOnPage(card2));
    }

    /// Попытка пополнения на сумму превышающую баланс
    //// @Test
    public void testTransferAmountGreaterThanBalance() {
        int transactionAmount = 10100;

        dashboardPage = new DashboardPage();
        dashboardPage
                .makeTransferTo(card1)
                .makeTransferFromAndAmount(card2, transactionAmount);

        assertEquals(startBalanceOfCard1, dashboardPage.getCardBalanceOnPage(card1));
        assertEquals(startBalanceOfCard2, dashboardPage.getCardBalanceOnPage(card2));
    }
}

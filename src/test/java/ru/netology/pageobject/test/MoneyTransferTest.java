package ru.netology.pageobject.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netology.pageobject.data.DataHelper;
import ru.netology.pageobject.data.DataHelper.UserInfo;
import ru.netology.pageobject.data.DataHelper.CardInfo;
import ru.netology.pageobject.data.DataHelper.VerificationCode;
import ru.netology.pageobject.page.DashboardPage;
import ru.netology.pageobject.page.LoginPage;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    private static final Logger logger = LoggerFactory.getLogger(MoneyTransferTest.class);

    UserInfo validUser1 = DataHelper.getAuthInfo();
    CardInfo card1 = DataHelper.getCard1();
    CardInfo card2 = DataHelper.getCard2();
    VerificationCode verificationCode = DataHelper.getVerificationTestCode();

    DashboardPage dashboardPage;
    int balanceCard1;
    int balanceCard2;

    @BeforeEach
    void setup() {
        initializeDriver();
        initializeDashboard();
    }

    private void initializeDriver() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
    }

    private void initializeDashboard() {
        open("http://localhost:9999");
        new LoginPage()
                .login(validUser1)
                .acceptCode(verificationCode.getTestCode());
        dashboardPage = new DashboardPage();
        calculateDifference();
    }

    private void calculateDifference() {
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
        balanceCard1 = dashboardPage.getCardBalanceOnPage(card1);
        balanceCard2 = dashboardPage.getCardBalanceOnPage(card2);
    }

    @Test
    public void testTransferBetweenCards() {
        int transactionAmount = 8888;

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage
                .makeTransferTo(card1)
                .makeTransferFromAndAmount(card2, transactionAmount);

        assertEquals(balanceCard1 + transactionAmount, dashboardPage.getCardBalanceOnPage(card1));
        assertEquals(balanceCard2 - transactionAmount, dashboardPage.getCardBalanceOnPage(card2));
    }

    @Test
    public void testTransferAmountGreaterThanBalance() {
        int transactionAmount = 10001;

        DashboardPage dashboardPage = new DashboardPage();

        int startBalanceCard1 = dashboardPage.getCardBalanceOnPage(card1);
        int startBalanceCard2 = dashboardPage.getCardBalanceOnPage(card2);

        logger.info("Изначальный баланс карты 1: {}", startBalanceCard1);
        logger.info("Изначальный баланс карты 2: {}", startBalanceCard2);

        dashboardPage
                .makeTransferTo(card1)
                .makeTransferFromAndAmount(card2, transactionAmount);

        int newBalanceCard1 = dashboardPage.getCardBalanceOnPage(card1);
        int newBalanceCard2 = dashboardPage.getCardBalanceOnPage(card2);

        logger.info("Новый баланс карты 1: {}", newBalanceCard1);
        logger.info("Новый баланс карты 2: {}", newBalanceCard2);

        assertEquals(startBalanceCard1, newBalanceCard1);
        assertEquals(startBalanceCard2, newBalanceCard2);
    }
}

package ru.netology.pageobject.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.pageobject.data.DataHelper.CardInfo;

import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private ElementsCollection cardsList = $$x("//ul[contains(@class,'list')]//div[@data-test-id]");
    private String cssLocatorForReplenishmentButton = "button[data-test-id='action-deposit']";

    public DashboardPage() {
        cardsList.get(0).shouldBe(Condition.appear);
    }

    public CardReplenishmentPage makeTransferTo(CardInfo card) {
        findCardInList(card).$(cssLocatorForReplenishmentButton).click();

        return new CardReplenishmentPage();
    }

    public SelenideElement findCardInList(CardInfo card) {
        String last4DigitOfCardNumber = card.getCardNumber().substring(15);

        return cardsList.findBy(Condition.text(last4DigitOfCardNumber));
    }

    public int getCardBalanceOnPage(CardInfo card) {
        String textInElement = findCardInList(card).text();
        return extractBalance(textInElement);
    }

    private int extractBalance(String text) {
        int start = text.indexOf(balanceStart);
        int finish = text.indexOf(balanceFinish);
        String value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}

package ru.netology.pageobject.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.pageobject.data.DataHelper.CardInfo;

import static com.codeborne.selenide.Selenide.$;

public class CardReplenishmentPage {
    private final SelenideElement amountInput = $("span[data-test-id='amount'] input");
    private final SelenideElement fromCardNumberInput = $("span[data-test-id='from'] input");
    private final SelenideElement okButton = $("button[data-test-id='action-transfer']");

    private void inputCardInfoAndAmount(CardInfo card, int amount) {
        amountInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        amountInput.setValue(String.valueOf(amount));
        fromCardNumberInput.sendKeys(Keys.LEFT_SHIFT, Keys.HOME, Keys.BACK_SPACE);
        fromCardNumberInput.setValue(card.getCardNumber());
        okButton.click();
    }
    public DashboardPage makeTransferFromAndAmount(CardInfo card, int amount) {
        inputCardInfoAndAmount(card, amount);

        return new DashboardPage();
    }
    public CardReplenishmentPage() {
        amountInput.shouldBe(Condition.appear);
    }
}

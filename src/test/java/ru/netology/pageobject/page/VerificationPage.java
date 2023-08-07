package ru.netology.pageobject.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement codeInput = $("span[data-test-id='code'] input");
    private final SelenideElement okButton = $("button[data-test-id='action-verify']");

    public DashboardPage acceptCode(String code) {
        codeInput.setValue(code);
        okButton.click();

        return new DashboardPage();
    }

    public VerificationPage() {
        codeInput.shouldBe(Condition.appear);
    }
}

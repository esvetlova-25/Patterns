package test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static ru.netology.delivery.data.DataGenerator.generateDate;

public class TestFormSending {

    @BeforeEach
    void setup(){open("http://localhost:9999");}

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = ru.netology.delivery.data.DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 9;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 15;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification].notification__content").shouldBe(Condition.visible);
        $("[data-test-id=success-notification].notification__content").shouldHave(Condition.exactText("Встреча успешно забронирована на " + firstMeetingDate));
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id=replan-notification].notification__content").shouldBe(Condition.visible);
        $("[data-test-id=replan-notification].notification__content").shouldHave(Condition.exactText("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(byText("[data-test-id=replan-notification] button")).click();
        $("[data-test-id=success-notification].notification__content").shouldBe(Condition.visible);
        $("[data-test-id=success-notification].notification__content").shouldHave(Condition.exactText("Встреча успешно забронирована на " + secondMeetingDate));
    }
}

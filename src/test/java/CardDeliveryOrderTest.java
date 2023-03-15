import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTest {
    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }


    @Test
    void shouldOrderCardDelivery(){
        Configuration.holdBrowserOpen = true;
        String planningDate = generateDate(3, "dd.MM.yyyy");

        open("http://localhost:9999/");
        $x("//*[@data-test-id = 'city']//input").setValue("Санкт-Петербург");
        $x("//*[@data-test-id = 'date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//*[@data-test-id = 'date']//input").setValue(planningDate);
        $x("//*[@data-test-id = 'name']//input").setValue("Поттер Гарри");
        $x("//*[@data-test-id = 'phone']//input").setValue("+78005553535");
        $x("//*[@data-test-id = 'agreement']").click();
        $x("//*[text() = 'Забронировать']").click();

        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldOrderCardDeliveryUseAutocompleteForms(){
        Configuration.holdBrowserOpen = true;

        long setDay = 7;
        LocalDate currentDate = LocalDate.now();
        String planningDate = generateDate(setDay, "dd.MM.yyyy");

        open("http://localhost:9999/");
        $x("//*[@data-test-id = 'city']//input").setValue("Са");
        $x("//span[text() = 'Санкт-Петербург']").click();

        $x("//*[@data-test-id = 'date']//button").click();
        for (int i = currentDate.getMonthValue(); i < currentDate.plusDays(setDay).getMonthValue(); i++) {     //сделал более универсальным, теперь вне зависимости
            $x("//div[@data-step = '1']").click();                                               //сколько прибавится дней, месяц накликается правильно
        }
        $x("//td[text() = '" + currentDate.plusDays(setDay).getDayOfMonth() +"']").click();

        $x("//*[@data-test-id = 'name']//input").setValue("Поттер Гарри");
        $x("//*[@data-test-id = 'phone']//input").setValue("+78005553535");
        $x("//*[@data-test-id = 'agreement']").click();
        $x("//*[text() = 'Забронировать']").click();

        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15))
                .shouldBe(Condition.visible);
    }



}

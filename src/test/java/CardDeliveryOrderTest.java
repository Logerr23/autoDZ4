import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryOrderTest {


    @Test
    void shouldOrderCardDelivery(){
        Configuration.holdBrowserOpen = true;
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, 3);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy");
        String date = sdf.format(cal.getTime());

        open("http://localhost:9999/");
        $x("//*[@data-test-id = 'city']//input").setValue("Санкт-Петербург");
        $x("//*[@data-test-id = 'date']//input").sendKeys("\b\b\b\b\b\b\b\b");
        $x("//*[@data-test-id = 'date']//input").setValue(date);
        $x("//*[@data-test-id = 'name']//input").setValue("Поттер Гарри");
        $x("//*[@data-test-id = 'phone']//input").setValue("+78005553535");
        $x("//*[@data-test-id = 'agreement']").click();
        $x("//*[text() = 'Забронировать']").click();

        $x("//*[@data-test-id = 'notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldOrderCardDeliveryUseAutocompleteForms(){
        Configuration.holdBrowserOpen = true;

        Calendar calWithCurrentDate = new GregorianCalendar();      //календарь с текущей датой
        Calendar calWithSetDate = new GregorianCalendar();          // календарь с выбранной дата
        calWithSetDate.add(Calendar.DAY_OF_YEAR, 7);
        int currentMonth = calWithCurrentDate.get(Calendar.MONTH);  // текущий месяц
        int setMonth = calWithSetDate.get(Calendar.MONTH);          // выбранный месяц
        int setDay =calWithSetDate.get(Calendar.DAY_OF_MONTH);      //выбранный день


        open("http://localhost:9999/");                                 //открытие страницы
        $x("//*[@data-test-id = 'city']//input").setValue("Са");            //ввод в поле город "Са"
        $x("//span[text() = 'Санкт-Петербург']").click();                   //выбор из всплывающего списка 'Санкт-Петербург'
        $x("//*[@data-test-id = 'date']//button").click();                  //клик по календарю
        if(currentMonth < setMonth ){
            $x("//div[@data-step = '1']").click();           // выбор даты с перелистыванием месяца
            $x("//td[text() = '" + setDay +"']").click();
        }else{
            $x("//td[text() = '" + setDay +"']").click();    // выбор даты без перелистывания месяца
        }

        $x("//*[@data-test-id = 'name']//input").setValue("Поттер Гарри");   //ввод в поле фамилия и имя
        $x("//*[@data-test-id = 'phone']//input").setValue("+78005553535");  //ввод в поле телефон
        $x("//*[@data-test-id = 'agreement']").click();                      //клик на согласие с обработкой данных
        $x("//*[text() = 'Забронировать']").click();                         //клик на кнопку "Забронировать"

        $x("//*[@data-test-id = 'notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));  //ожидание подтверждения
    }



}

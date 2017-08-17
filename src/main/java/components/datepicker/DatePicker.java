package components.datepicker;

import components.keyboard.IKeyboard;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import util.ClearMessage;
import util.Json;
import util.database.DataRec;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatePicker {

    private DateTime selectedDate;
    private DateTime date;
    private int startDay;
    private List<String> designate;

    public DatePicker(DataRec queryData){
        this.designate = new ArrayList<>();
        calculateDates(queryData);
    }

    public DateTime getDate(TelegramLongPollingBot bot, String messageText, long chatId) throws TelegramApiException {

        if (selectedDate == null){

            Message message = bot.sendMessage(
                    new SendMessage()
                            .setText(messageText)
                            .setChatId(chatId)
                            .setReplyMarkup(generate())
            );
            ClearMessage.set(message.getChatId(), message.getMessageId());
            throw new RuntimeException("ignore");

        } else {
            return selectedDate;
        }
    }

    public void setDesignate(List<String> designate) {
        this.designate = designate;
    }

    private void calculateDates(DataRec queryData) {


        if (queryData.containsKey("dp_sel")){
            selectedDate = new DateTime(queryData.getDate("dp_sel"));
            queryData.remove("dp_sel");
        }

        if (queryData.containsKey("dp_dt")){
            date = new DateTime(queryData.getDate("dp_dt"));
            queryData.remove("dp_dt");
        } else {
            date = new DateTime();
        }

        if (queryData.containsKey("dp_next")){

            if (date.getDayOfMonth() <= 16){
                date = date.dayOfMonth().withMaximumValue();
                startDay = date.getDayOfMonth() - 16;
            } else {
                date = date.dayOfMonth().withMinimumValue().plusMonths(1);
                startDay = 1;
            }
            queryData.remove("dp_next");

        } else if (queryData.containsKey("dp_prev")){

            if (date.getDayOfMonth() <= 16){
                date = date.minusMonths(1).dayOfMonth().withMaximumValue();
                startDay = date.getDayOfMonth() - 16;
            } else {
                date = date.dayOfMonth().withMinimumValue();
                startDay = 1;
            }
            queryData.remove("dp_prev");

        } else {

            if (date.getDayOfMonth() <= 16){
                date = date.dayOfMonth().withMinimumValue();
                startDay = 1;
            } else {
                date = date.dayOfMonth().withMaximumValue();
                startDay = date.getDayOfMonth() - 16;
            }
        }

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        designate.add(df.format(new Date()));
    }

    private InlineKeyboardMarkup generate() {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String dateStr = df.format(date.toDate());

        IKeyboard keyboard = new IKeyboard();
        setHeader(keyboard, dateStr);
        setBody(keyboard, dateStr.substring(2));
        return keyboard.generate();
    }

    private void setHeader(IKeyboard keyboard, String dateStr){

        String monthName = "";
        switch (date.getMonthOfYear()){
            case 1: monthName = "Январь"; break;
            case 2: monthName = "Февраль"; break;
            case 3: monthName = "Март"; break;
            case 4: monthName = "Апрель"; break;
            case 5: monthName = "Май"; break;
            case 6: monthName = "Июнь"; break;
            case 7: monthName = "Июль"; break;
            case 8: monthName = "Август"; break;
            case 9: monthName = "Сентябрь"; break;
            case 10: monthName = "Октябрь"; break;
            case 11: monthName = "Ноябрь"; break;
            case 12: monthName = "Декабрь"; break;
        }
        monthName += " " + date.getYear();

        keyboard.next(3);
        keyboard.add("<", Json.set("dp_dt", dateStr).set("dp_prev", "p"));
        keyboard.add(monthName, Json.set("dp_dt", dateStr));
        keyboard.add(">", Json.set("dp_dt", dateStr).set("dp_next", "p"));
    }

    private void setBody(IKeyboard keyboard, String monthYear) {

        keyboard.next(4,4,4,4);
        for (int a = 1; a <= 4; a++){
            for (int i = 1; i <= 4; i++) {

                String day = String.valueOf(startDay);
                String startDayStr = String.valueOf(startDay);

                if (startDay < 10) day = "0" + day;
                if (designate.contains(day + monthYear)) {
                    startDayStr = "• " + startDay + " •";
                }

                keyboard.add(startDayStr, Json.set("dp_sel", day + monthYear));
                startDay++;
            }
        }
    }
}

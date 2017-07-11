package components.datepicker;

import com.google.gson.Gson;
import database.utils.DataRec;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

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
    private String step = null;
    private TelegramLongPollingBot bot;
    private int messageId;
    private long chatId;

    public DatePicker(
            TelegramLongPollingBot bot,
            int messageId, long chatId,
            DataRec queryData
    ){
        this.bot = bot;
        this.messageId = messageId;
        this.chatId = chatId;
        this.designate = new ArrayList<>();
        calculateDates(queryData);
    }

    public DatePicker(
            TelegramLongPollingBot bot,
            int messageId, long chatId,
            DataRec queryData,
            String step
    ){
        this.bot = bot;
        this.messageId = messageId;
        this.chatId = chatId;
        this.designate = new ArrayList<>();
        this.step = step;
        calculateDates(queryData);
    }


    public DateTime getSelectedDate() {
        return selectedDate;
    }

    public String getSelectedDateStr() {
        if (selectedDate == null){
            return null;
        }else {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            return df.format(selectedDate.toDate());
        }
    }

    public void setDesignate(List<String> designate) {
        this.designate = designate;
    }

    private void calculateDates(DataRec queryData) {

        try {
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(String.valueOf(chatId));
            deleteMessage.setMessageId(messageId);
            bot.deleteMessage(deleteMessage);
        } catch (Exception ignore){}

        if (queryData.containsKey("dp_selected")){
            selectedDate = new DateTime(queryData.getDate("dp_selected"));
        }

        if (queryData.containsKey("dp_date")){
            date = new DateTime(queryData.getDate("dp_date"));
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

        } else if (queryData.containsKey("dp_prev")){

            if (date.getDayOfMonth() <= 16){
                date = date.minusMonths(1).dayOfMonth().withMaximumValue();
                startDay = date.getDayOfMonth() - 16;
            } else {
                date = date.dayOfMonth().withMinimumValue();
                startDay = 1;
            }

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


    public void execute() throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите дату");
        message.setReplyMarkup(generateKeyboard());
        bot.sendMessage(message);
    }


    private InlineKeyboardMarkup generateKeyboard() {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String dateStr = df.format(date.toDate());
        DataRec rec = new DataRec();
        rec.put("dp_date", dateStr);

        if (step != null){
            rec.put("step", step);
        }

        String monthName = "";
        switch (date.getMonthOfYear()){
            case 1: monthName = "Январь " + date.getYear(); break;
            case 2: monthName = "Февраль " + date.getYear(); break;
            case 3: monthName = "Март " + date.getYear(); break;
            case 4: monthName = "Апрель " + date.getYear(); break;
            case 5: monthName = "Май " + date.getYear(); break;
            case 6: monthName = "Июнь " + date.getYear(); break;
            case 7: monthName = "Июль " + date.getYear(); break;
            case 8: monthName = "Август " + date.getYear(); break;
            case 9: monthName = "Сентябрь " + date.getYear(); break;
            case 10: monthName = "Октябрь " + date.getYear(); break;
            case 11: monthName = "Ноябрь " + date.getYear(); break;
            case 12: monthName = "Декабрь " + date.getYear(); break;
        }

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        setHeader(keyboard, rec, monthName);
        setBody(keyboard, dateStr.substring(2));
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    private void setHeader(
            List<List<InlineKeyboardButton>> keyboard,
            DataRec rec, String monthName
    ){

        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

        rec.put("dp_prev", "dp_prev");
        InlineKeyboardButton buttonPrevMonth = new InlineKeyboardButton();
        buttonPrevMonth.setText("<");
        buttonPrevMonth.setCallbackData(new Gson().toJson(rec));
        keyboardRow.add(buttonPrevMonth);
        rec.remove("dp_prev");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(monthName);
        button2.setCallbackData(new Gson().toJson(rec));
        keyboardRow.add(button2);

        rec.put("dp_next", "dp_next");
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText(">");
        button3.setCallbackData(new Gson().toJson(rec));
        keyboardRow.add(button3);

        keyboard.add(keyboardRow);
    }

    private void setBody(
            List<List<InlineKeyboardButton>> keyboard,
            String monthYear
    ){

        for (int a = 1; a <= 4; a++){
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();

            for (int i = 1; i <= 4; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton();

                String day = String.valueOf(startDay);

                if (startDay < 10) day = "0" + day;
                if (designate.contains(day + monthYear)) {
                    button.setText("• " + startDay + " •");
                } else {
                    button.setText(String.valueOf(startDay));
                }

                if (step != null){
                    button.setCallbackData("{\"dp_selected\":" + day + monthYear
                            + ", \"step\":" + step + "}");
                } else {
                    button.setCallbackData("{\"dp_selected\":" + day + monthYear + "}");
                }
                keyboardRow.add(button);
                startDay++;
            }
            keyboard.add(keyboardRow);
        }
    }
}

package pro.nextbit.telegramconstructor.components.datepicker;

import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageReplyMarkup;
import pro.nextbit.telegramconstructor.StepParam;
import pro.nextbit.telegramconstructor.components.keyboard.IKeyboard;
import org.joda.time.DateTime;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import pro.nextbit.telegramconstructor.ClearMessage;
import pro.nextbit.telegramconstructor.Json;
import pro.nextbit.telegramconstructor.database.DataRec;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class FullDatePicker {

    private DataRec queryData = null;
    private DateTime tempDate = null;
    private DateTime selectedDate = null;
    private String step = null;
    private LinkedList<LinkedList<Integer>> monthDates = null;
    private boolean isEdit = false;

    public FullDatePicker(DataRec queryData, String step) {
        this.step = step;
        this.queryData = queryData;
        this.monthDates = new LinkedList<>();

        if (queryData.containsKey("dp_dt")) {
            this.isEdit = true;
        }

        if (!queryData.containsKey("dp_w")){
            calculateDates();
        }
    }

    public DateTime getDate(
            TelegramLongPollingBot bot,
            String messageText, Message message
    ) throws Exception {

        DataRec param = new StepParam(message.getChatId(), step + "_dr").get();
        if (param.containsKey("dp_sel")){
            selectedDate = new DateTime(param.getDate("dp_sel"));
        }

        if (selectedDate == null) {

            if (isEdit) {

                bot.editMessageReplyMarkup(
                        new EditMessageReplyMarkup()
                                .setReplyMarkup(generate())
                                .setMessageId(message.getMessageId())
                                .setChatId(message.getChatId())
                );

            } else  {

                Message msg = bot.sendMessage(new SendMessage()
                        .setChatId(message.getChatId())
                        .setText(messageText)
                        .setReplyMarkup(generate())
                );

                new ClearMessage().clearLater(msg);
                throw new RuntimeException("ignore");

            }

        }

        return selectedDate;
    }

    public InlineKeyboardMarkup generate() {

        Date prevMonth = tempDate.minusMonths(1).dayOfMonth().withMinimumValue().toDate();
        Date nextMonth = tempDate.plusMonths(1).dayOfMonth().withMinimumValue().toDate();

        IKeyboard keyboard = new IKeyboard();
        generateHeader(keyboard, prevMonth, nextMonth);
        generateBody(keyboard, prevMonth, nextMonth);

        return keyboard.generate();
    }

    private void generateHeader(IKeyboard keyboard, Date prevMonth, Date nextMonth) {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat df2 = new SimpleDateFormat("MMM");
        String currentDate = df.format(tempDate.toDate());
        String monthName = df2.format(tempDate) + " " + tempDate.getYear();

        keyboard.next(3, 7);
        keyboard.addButton("<", Json.set("dp_dt", df.format(prevMonth)).set("step", step));
        keyboard.addButton(monthName, Json.set("dp_dt", df.format(tempDate.toDate())).set("step", step));
        keyboard.addButton(">", Json.set("dp_dt", df.format(nextMonth)).set("step", step));
        keyboard.addButton("Пн", Json.set("dp_dt", currentDate).set("dp_w", 1).set("step", step));
        keyboard.addButton("Вт", Json.set("dp_dt", currentDate).set("dp_w", 2).set("step", step));
        keyboard.addButton("Ср", Json.set("dp_dt", currentDate).set("dp_w", 3).set("step", step));
        keyboard.addButton("Чт", Json.set("dp_dt", currentDate).set("dp_w", 4).set("step", step));
        keyboard.addButton("Пт", Json.set("dp_dt", currentDate).set("dp_w", 5).set("step", step));
        keyboard.addButton("Сб", Json.set("dp_dt", currentDate).set("dp_w", 6).set("step", step));
        keyboard.addButton("Вс", Json.set("dp_dt", currentDate).set("dp_w", 7).set("step", step));
    }

    private void generateBody(IKeyboard keyboard, Date prevMonth, Date nextMonth){

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String monthYear = df.format(tempDate.toDate());

        for (LinkedList<Integer> weekDates : monthDates) {
            keyboard.next(7);

            for (int day : weekDates) {

                if (day == -1) {
                    keyboard.addButton(
                            ".",
                            Json.set("dp_dt", df.format(prevMonth))
                                .set("step", step)
                    );
                } else if (day == -2){
                    keyboard.addButton(
                            ".",
                            Json.set("dp_dt", df.format(nextMonth))
                                .set("step", step)
                    );
                } else {
                    keyboard.addButton(
                            String.valueOf(day),
                            Json.set("dp_sel", day + monthYear)
                                .set("step", step)
                    );
                }
            }
        }
    }

    private void calculateDates() {

        if (queryData.containsKey("dp_sel")) {
            selectedDate = new DateTime(queryData.getDate("dp_sel"));
        }

        if (queryData.containsKey("dp_dt")) {
            tempDate = new DateTime(queryData.getDate("dp_dt"));
        } else {
            tempDate = new DateTime();
        }

        DateTime firstDay = tempDate.dayOfMonth().withMinimumValue();
        DateTime lastDay = tempDate.dayOfMonth().withMaximumValue();
        LinkedList<Integer> weekDates = new LinkedList<>();

        int firstWeek = firstDay.getDayOfWeek() - 1;
        int lastWeek = lastDay.getDayOfWeek() + 1;

        while (firstWeek > 0) {
            weekDates.add(-1);
            firstWeek--;
        }


        for (int day = 1; day <= lastDay.getDayOfMonth(); day++) {
            weekDates.add(day);
            if (weekDates.size() >= 7) {
                monthDates.add(weekDates);
                weekDates = new LinkedList<>();
            }
        }

        while (lastWeek < 8) {
            weekDates.add(-2);
            lastWeek++;
        }

        if (weekDates.size() == 7) {
            monthDates.add(weekDates);
        }

    }
}

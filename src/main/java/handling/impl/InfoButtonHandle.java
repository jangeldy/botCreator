package handling.impl;

import components.keyboard.Keyboard;
import handling.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import util.stepmapping.Step;

import java.util.ArrayList;
import java.util.List;

public class InfoButtonHandle extends AbstractHandle {

    @Step(value = "I_info_menu", commandText = "\uD83D\uDCCD Информация")
    public void mainMenu() throws Exception {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton();
        button1.setText("Lean 💡");
        KeyboardButton button2 = new KeyboardButton();
        button2.setText("О проекте 🏗");
        KeyboardButton button3 = new KeyboardButton();
        button3.setText("О BI Group 🚀");
        KeyboardButton button4 = new KeyboardButton();
        button4.setText("Назад");
        ;
        row1.add(button1);
        row1.add(button2);
        row2.add(button3);
        row2.add(button4);
        list.add(row1);
        list.add(row2);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(list);


        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(keyboardMarkup));
    }




    @Step(value = "I_info_lean", commandText = "Lean 💡")
    public void I_info_lean() throws Exception {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton();
        button1.setText("Подать Lean-идею 🎯");
        KeyboardButton button2 = new KeyboardButton();
        button2.setText("Lean Стандарты 🔰");
        KeyboardButton button3 = new KeyboardButton();
        button3.setText("Центр Компетенци Lean 🏬");
        KeyboardButton button4 = new KeyboardButton();
        button4.setText("LEAN видео");
        KeyboardButton button5 = new KeyboardButton();
        button5.setText("Библиотека 📚");
        KeyboardButton button6 = new KeyboardButton();
        button6.setText("Назад");
        row1.add(button1);
        row1.add(button2);
        row2.add(button3);
        row2.add(button4);
        row3.add(button5);
        row3.add(button6);
        list.add(row1);
        list.add(row2);
        list.add(row3);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(list);

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(keyboardMarkup));
    }


    @Step(value = "I_info_bi", commandText = "О BI Group 🚀")
    public void I_info_bi() throws Exception {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardRow row4 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton();
        button1.setText("Наш обьект 💎");
        KeyboardButton button2 = new KeyboardButton();
        button2.setText("Ход строительства 📝");
        KeyboardButton button3 = new KeyboardButton();
        button3.setText("Таймер");
        KeyboardButton button4 = new KeyboardButton();
        button4.setText("Назад");

        row1.add(button1);
        row2.add(button2);
        row3.add(button3);
        row4.add(button4);
        list.add(row1);
        list.add(row2);
        list.add(row3);
        list.add(row4);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(list);

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(keyboardMarkup));
    }


    @Step(value = "I_info_project", commandText = "О проекте 🏗")
    public void I_info_project() throws Exception {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> list = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton();
        button1.setText("Миссия");
        KeyboardButton button2 = new KeyboardButton();
        button2.setText("История 🔥");
        KeyboardButton button3 = new KeyboardButton();
        button3.setText("Новости 🎙");
        KeyboardButton button4 = new KeyboardButton();
        button4.setText("Мероприятия 🎉");
        KeyboardButton button5 = new KeyboardButton();
        button5.setText("Назад");

        row1.add(button1);
        row1.add(button2);
        row2.add(button3);
        row2.add(button4);
        row3.add(button5);
        list.add(row1);
        list.add(row2);
        list.add(row3);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setKeyboard(list);

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(keyboardMarkup));
    }




}






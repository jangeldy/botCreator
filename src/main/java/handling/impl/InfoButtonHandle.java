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

    @Step("\uD83D\uDCCD Информация")
    public void I_info_menu() throws Exception {

        Keyboard kb = new Keyboard();
        kb.next();
        kb.addButton("Lean 💡");
        kb.addButton("О проекте 🏗");
        kb.addButton("О BI Group 🚀");
        kb.addButton("Назад");

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate()));
    }




    @Step("Lean 💡")
    public void I_info_lean() throws Exception {

        Keyboard kb = new Keyboard();
        kb.next(2,2,2);
        kb.addButton("Подать Lean-идею 🎯");
        kb.addButton("Lean Стандарты 🔰");
        kb.addButton("Центр Компетенци Lean 🏬");
        kb.addButton("LEAN видео");
        kb.addButton("Библиотека 📚");
        kb.addButton("Назад");

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate()));
    }


    @Step("О BI Group 🚀")
    public void I_info_bi() throws Exception {

        Keyboard kb = new Keyboard();
        kb.next();
        kb.addButton("Наш обьект 💎");
        kb.addButton("Ход строительства 📝");
        kb.addButton("Таймер");
        kb.addButton("Назад");

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate()));
    }


    @Step("О проекте 🏗")
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






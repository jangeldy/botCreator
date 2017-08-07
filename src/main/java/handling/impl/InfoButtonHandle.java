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
        kb.add("Lean 💡");
        kb.add("О проекте 🏗");
        kb.add("О BI Group 🚀");
        kb.add("Назад");

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate()));
    }


    @Step("Lean 💡")
    public void I_info_lean() throws Exception {

        Keyboard kb = new Keyboard();
        kb.next(2,2,2);
        kb.add("Подать Lean-идею 🎯");
        kb.add("Lean Стандарты 🔰");
        kb.add("Центр Компетенци Lean 🏬");
        kb.add("LEAN видео");
        kb.add("Библиотека 📚");
        kb.add("Назад");

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate()));
    }


    @Step("О BI Group 🚀")
    public void I_info_bi() throws Exception {

        Keyboard kb = new Keyboard();
        kb.next();
        kb.add("Наш обьект 💎");
        kb.add("Ход строительства 📝");
        kb.add("Таймер");
        kb.add("Назад");

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate()));
    }


    @Step("О проекте 🏗")
    public void I_info_project() throws Exception {

        Keyboard kb = new Keyboard();
        kb.next(2,2,1);
        kb.add("Миссия");
        kb.add("История 🔥");
        kb.add("Новости 🎙");
        kb.add("Мероприятия 🎉");
        kb.add("Назад");

        bot.sendMessage(new SendMessage()
                .setText("Меню ИНФО")
                .setChatId(chatId)
                .setReplyMarkup(kb.generate()));
    }
}

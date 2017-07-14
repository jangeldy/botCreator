package components.keyboard;

import com.google.gson.Gson;
import database.utils.DataRec;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

    private int[] buttonCounts = null;
    private List<List<InlineKeyboardButton>> inlineList;
    private List<KeyboardRow> list;
    private boolean inline;

    public Keyboard(int ...buttonCounts){
        this.buttonCounts = buttonCounts;
        this.inline = false;
        list = new ArrayList<>();
    }

    public Keyboard(boolean inline, int ...buttonCounts){
        this.inline = inline;
        this.buttonCounts = buttonCounts;
        if (inline)inlineList = new ArrayList<>();
        else list = new ArrayList<>();
    }


    public ReplyKeyboard get() {

        if (inline){
            return new InlineKeyboardMarkup().setKeyboard(inlineList);
        } else {
            return new ReplyKeyboardMarkup().setKeyboard(list)
                    .setResizeKeyboard(true);
        }

    }


    public InlineKeyboardButton addButton(String text, DataRec json) {

        if (!inline) {
            throw new RuntimeException("Неверно использован метод");
        }

        List<InlineKeyboardButton> buttonList;
        InlineKeyboardButton button = new InlineKeyboardButton()
                .setText(text)
                .setCallbackData(new Gson().toJson(json));

        if (buttonCounts != null && buttonCounts.length > 0){

            if (inlineList.size() == 0){

                buttonList = new ArrayList<>();
                buttonList.add(button);
                inlineList.add(buttonList);

            } else {

                int buttonCount = buttonCounts[inlineList.size() - 1];
                buttonList = inlineList.get(inlineList.size() - 1);

                if (buttonList.size() == buttonCount){

                    if (buttonCounts.length == inlineList.size()){
                        throw new RuntimeException("Количество добавленных кнопок больше указанной");
                    } else {

                        buttonList = new ArrayList<>();
                        buttonList.add(button);
                        inlineList.add(buttonList);
                    }

                } else {
                    buttonList.add(button);
                }

            }

        } else {

            buttonList = new ArrayList<>();
            buttonList.add(button);
            inlineList.add(buttonList);

        }


        return button;
    }


    public KeyboardButton addButton(String text) {

        if (inline) {
            throw new RuntimeException("Неверно использован метод");
        }

        KeyboardRow buttonList;
        KeyboardButton button = new KeyboardButton().setText(text);

        if (buttonCounts != null && buttonCounts.length > 0){

            if (list.size() == 0){

                buttonList = new KeyboardRow();
                buttonList.add(button);
                list.add(buttonList);

            } else {

                int buttonCount = buttonCounts[list.size() - 1];
                buttonList = list.get(list.size() - 1);

                if (buttonList.size() == buttonCount){

                    if (buttonCounts.length == list.size()){
                        throw new RuntimeException("Количество добавленных кнопок больше указанной");
                    } else {

                        buttonList = new KeyboardRow();
                        buttonList.add(button);
                        list.add(buttonList);
                    }

                } else {
                    buttonList.add(button);
                }

            }

        } else {

            buttonList = new KeyboardRow();
            buttonList.add(button);
            list.add(buttonList);

        }


        return button;
    }

}

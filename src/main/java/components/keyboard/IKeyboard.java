package components.keyboard;

import com.google.gson.Gson;
import database.utils.DataRec;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class IKeyboard {

    private int[] buttonCounts = null;
    private List<List<InlineKeyboardButton>> inlineList;
    private List<List<List<InlineKeyboardButton>>> inlineTables;

    public void next(int ...buttonCounts){
        this.buttonCounts = buttonCounts;
        setRows();
    }

    public void next(){
        this.buttonCounts = null;
        setRows();
    }

    private void setRows(){
        if (inlineTables == null) {
            inlineTables = new ArrayList<>();
            inlineList = new ArrayList<>();
        }
        else {
            if (inlineList.size() == 0){
                throw new RuntimeException("Вы не добавили кнопки");
            }
            else {
                inlineTables.add(inlineList);
                inlineList = new ArrayList<>();
            }
        }
    }

    public InlineKeyboardMarkup generate() {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (List<List<InlineKeyboardButton>> list:inlineTables){
            keyboard.addAll(list);
        }
        keyboard.addAll(inlineList);
        return new InlineKeyboardMarkup().setKeyboard(keyboard);
    }


    public InlineKeyboardButton addButton(String text, DataRec json) {

        if (inlineTables == null){
            throw new RuntimeException("Метод next не был вызван");
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

}

package components.keyboard;

import database.utils.DataRec;
import database.utils.DataTable;
import database.utils.DbUtils;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {


    /**
     * Создает ReplyKeyboard (клавиатуру) по переданным данным
     * @param dataTables - данные (список keyboard_button по id keyboard_row)
     * @param inline - внутри чата
     * @return - ReplyKeyboard
     */
    public ReplyKeyboard create(List<DataTable> dataTables, boolean inline) {

        if (inline) {


            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> list = new ArrayList<>();

            for (DataTable dataTable : dataTables) {
                list.add(inlineKeyboardRow(dataTable));
            }

            keyboardMarkup.setKeyboard(list);
            return keyboardMarkup;

        } else {

            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> list = new ArrayList<>();

            for (DataTable dataTable : dataTables) {
                list.add(keyboardRow(dataTable));
            }

            keyboardMarkup.setResizeKeyboard(true);
            keyboardMarkup.setKeyboard(list);
            return keyboardMarkup;

        }

    }


    /**
     * Создает ReplyKeyboard (клавиатуру) по id
     * @param keyboardId - id таблицы keyboard
     * @return - ReplyKeyboard
     */
    public ReplyKeyboard create(DbUtils dbUtils, int keyboardId) {

        DataRec keyboard = dbUtils.queryDataRec("SELECT * FROM keyboard WHERE id = ?", keyboardId);
        DataTable keyboardRows = dbUtils.query("SELECT * FROM keyboard_row WHERE id_keyboard = ?", keyboardId);
        List<DataTable> dataTableList = new ArrayList<>();

        for (DataRec keyboardRow : keyboardRows) {
            DataTable buttonList = dbUtils.query(
                    "SELECT * FROM keyboard_button WHERE id_keyboard_row = ?", keyboardRow.getInt("id")
            );
            dataTableList.add(buttonList);
        }

        return create(dataTableList, keyboard.getBoolean("inline"));
    }


    private InlineKeyboardButton inlineButton(DataRec buttonData) {

        InlineKeyboardButton button = new InlineKeyboardButton();

        if (buttonData.hasValue("text"))
            button.setText(buttonData.getString("text"));

        if (buttonData.hasValue("url"))
            button.setUrl(buttonData.getString("url"));

        if (buttonData.hasValue("json"))
            button.setCallbackData(buttonData.getString("json"));

        if (buttonData.hasValue("query"))
            button.setSwitchInlineQuery(buttonData.getString("query"));

        if (buttonData.hasValue("query_chat"))
            button.setSwitchInlineQueryCurrentChat(buttonData.getString("query_chat"));

        if (buttonData.hasValue("pay"))
            button.setPay(buttonData.getBoolean("pay"));

        return button;
    }


    private KeyboardButton button(DataRec buttonData) {

        KeyboardButton button = new KeyboardButton();

        if (buttonData.hasValue("text"))
            button.setText(buttonData.getString("text"));

        if (buttonData.hasValue("rq_contact"))
            button.setRequestContact(buttonData.getBoolean("url"));

        if (buttonData.hasValue("rq_location"))
            button.setRequestContact(buttonData.getBoolean("rq_location"));

        return button;
    }


    private KeyboardRow keyboardRow(DataTable buttonList) {
        KeyboardRow keyboardRow = new KeyboardRow();
        for (DataRec rec : buttonList) keyboardRow.add(button(rec));
        return keyboardRow;
    }


    private List<InlineKeyboardButton> inlineKeyboardRow(DataTable dataTable) {
        List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
        for (DataRec rec : dataTable) keyboardRow.add(inlineButton(rec));
        return keyboardRow;
    }

}

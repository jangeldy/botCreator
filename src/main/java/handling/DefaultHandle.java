package handling;

import database.DataBaseConfig;
import util.handle.AbstractHandle;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import util.database.DataBaseUtils;
import util.database.DataRec;
import util.stepmapping.Step;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultHandle extends AbstractHandle {

    @Step("defaultStep")
    public void defaultStep() throws Exception {

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        DataBaseUtils utils = new DataBaseUtils(DataBaseConfig.dataSource());
        DataRec rec = utils.queryDataRec("select * from access_info");
        Date date = rec.getDate("new_column");

        Timestamp timestamp = new Timestamp(date.getTime());

        bot.sendMessage(new SendMessage()
                .setChatId(chatId)
                .setText(rec.getString("new_column"))
        );
    }
}

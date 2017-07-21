package util;

import util.database.ut.DataRec;

import java.util.HashMap;
import java.util.Map;

public class StepParam {

    private static Map<String, DataRec> paramsMap = new HashMap<>();
    private DataRec params;
    private long chatId;
    private String step;

    public StepParam(long chatId, String step) {

        this.chatId = chatId;
        this.step = step;

        if (paramsMap.containsKey(step + chatId)){
            params = paramsMap.get(step + chatId);
        } else {
            params = new DataRec();
            paramsMap.put(step + chatId, params);
        }
    }

    public DataRec set(String key, Object value) {
        return params.set(key, value);
    }

    public Object get(String key) {
        return params.get(key);
    }

    public void remove() {
        paramsMap.remove(step + chatId);
    }
}
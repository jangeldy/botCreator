package util;

import util.databaseconfig.ut.DataRec;

import java.util.Date;
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

    public boolean containsKey(String key){
        return params.containsKey(key);
    }

    public Object get(String key) {
        return params.get(key);
    }

    public int getInt(String key) {
        return params.getInt(key);
    }

    public long getLong(String key) {
        return params.getLong(key);
    }

    public String getString(String key) {
        return params.getString(key);
    }

    public Date getDate(String key) {
        return params.getDate(key);
    }

    public Double getDouble(String key) {
        return params.getDouble(key);
    }

    public boolean getBoolean(String key) {
        return params.getBoolean(key);
    }

    public void remove() {
        paramsMap.remove(step + chatId);
    }
}

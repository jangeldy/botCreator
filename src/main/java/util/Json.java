package util;

import util.database.DataRec;

public class Json {

    public static DataRec set(String key, Object value) {
        return new DataRec().set(key, value);
    }

}

package util;

import util.databaseconfig.ut.DataRec;

public class Json {

    public static DataRec set(String key, Object value) {
        return new DataRec().set(key, value);
    }

}

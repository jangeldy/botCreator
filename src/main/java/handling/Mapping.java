package handling;

import java.lang.reflect.Constructor;

public class Mapping {

    public static AbstractHandle getHandleClass(String className) throws Exception {

        Class<?> clazz = Class.forName("handling.impl." + className);
        Constructor<?> ctor = clazz.getConstructor();
        Object object = ctor.newInstance();
        return (AbstractHandle) object;
    }
}

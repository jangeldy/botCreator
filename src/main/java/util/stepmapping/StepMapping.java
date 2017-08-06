package util.stepmapping;

import handling.AbstractHandle;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StepMapping {

    private static Map<String, Mapping> stepMappingMap = new HashMap<>();
    private static Map<String, Mapping> commandMappingMap = new HashMap<>();

    public static void initializeMapping() throws Exception {

        Reflections reflections = new Reflections("handling.impl");
        Set<Class<? extends AbstractHandle>> classes = reflections.getSubTypesOf(AbstractHandle.class);

        for (Class clazz : classes){

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {

                if (method.isAnnotationPresent(Step.class)) {
                    Step step = method.getAnnotation(Step.class);
                    String stepName = method.getName();

                    if (!stepMappingMap.containsKey(stepName)){

                        Mapping mapping = new Mapping();
                        mapping.setHandleClassName(clazz.getSimpleName());
                        mapping.setHandleMethod(method.getName());
                        mapping.setStep(stepName);
                        mapping.setCommandText(step.value());
                        stepMappingMap.put(stepName, mapping);

                    }
                    else {
                        throw new Exception(
                                "Error when reading a step \"" + stepName + "\" in class "
                                + clazz.getSimpleName()
                                + "; Step already exists in class "
                                + stepMappingMap.get(stepName).getHandleClassName()
                        );
                    }

                    if (!step.value().equals("")){
                        if (!commandMappingMap.containsKey(step.value())){

                            Mapping mapping = new Mapping();
                            mapping.setHandleClassName(clazz.getSimpleName());
                            mapping.setHandleMethod(method.getName());
                            mapping.setStep(stepName);
                            mapping.setCommandText(step.value());
                            commandMappingMap.put(step.value(), mapping);

                        }
                        else {
                            throw new Exception(
                                    "Error when reading a commandText \"" + step.value() + "\" in class "
                                            + clazz.getSimpleName()
                                            + "; CommandText already exists in class "
                                            + commandMappingMap.get(step.value()).getHandleClassName()
                            );
                        }
                    }

                }
            }
        }
    }


    public static boolean containsStep(String step){
        return stepMappingMap.containsKey(step);
    }

    public static Mapping getMappingByStep(String step){
        return stepMappingMap.get(step);
    }


    public static boolean containsCommandText(String step){
        return commandMappingMap.containsKey(step);
    }

    public static Mapping getMappingByCommandText(String commandText){
        return commandMappingMap.get(commandText);
    }
}

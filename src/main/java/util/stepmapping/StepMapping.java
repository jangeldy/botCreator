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

                    if (step.value().equals("")){
                        throw new Exception("There is no step for the class " + clazz.getSimpleName());
                    }

                    checkStepName(clazz.getSimpleName(), step.value());

                    if (!stepMappingMap.containsKey(step.value())){

                        Mapping mapping = new Mapping();
                        mapping.setHandleClassName(clazz.getSimpleName());
                        mapping.setHandleMethod(method.getName());
                        mapping.setStep(step.value());
                        mapping.setCommandText(step.commandText());
                        stepMappingMap.put(step.value(), mapping);

                    }
                    else {
                        throw new Exception(
                                "Error when reading a step in class "
                                + clazz.getSimpleName()
                                + "; Step already exists in class "
                                + stepMappingMap.get(step.value()).getHandleClassName()
                        );
                    }

                    if (!step.commandText().equals("")){
                        if (!commandMappingMap.containsKey(step.commandText())){

                            Mapping mapping = new Mapping();
                            mapping.setHandleClassName(clazz.getSimpleName());
                            mapping.setHandleMethod(method.getName());
                            mapping.setStep(step.value());
                            mapping.setCommandText(step.commandText());
                            commandMappingMap.put(step.commandText(), mapping);

                        }
                        else {
                            throw new Exception(
                                    "Error when reading a commandText in class "
                                            + clazz.getSimpleName()
                                            + "; CommandText already exists in class "
                                            + commandMappingMap.get(step.commandText()).getHandleClassName()
                            );
                        }
                    }

                }
            }
        }
    }

    private static void checkStepName(String className, String stepName) throws Exception {

        char[] stepNameChar = stepName.toCharArray();
        char[] classNameChar = className.toCharArray();

        if (stepNameChar[0] != classNameChar[0]){
            throw new Exception("The step name is set incorrectly in class " + className + "; \n" +
                    "The step name must begin with the first letter of the class name");
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

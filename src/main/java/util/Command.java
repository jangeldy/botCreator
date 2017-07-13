package util;

import database.utils.DataRec;

public class Command {

    private String handlingClass = null;
    private String step = null;
    private String commandText = null;
    private DataRec queryData;
    private AccessLevel accessLevel;
    private int messageId = -1;
    private long chatId = -1;
    private boolean isRedirect = false;

    public Command() {
        this.queryData = new DataRec();
        this.accessLevel = AccessLevel.READ;
        this.messageId = -1;
        this.chatId = -1;
    }

    public String getHandlingClass() {
        return handlingClass;
    }

    public void setHandlingClass(String handlingClass) {
        this.handlingClass = handlingClass;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getCommandText() {
        return commandText;
    }

    public void setCommandText(String commandText) {
        this.commandText = commandText;
    }

    public DataRec getQueryData() {
        return queryData;
    }

    public void setQueryData(DataRec queryData) {
        this.queryData = queryData;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public void setRedirect(boolean redirect) {
        isRedirect = redirect;
    }
}

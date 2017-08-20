package handling;

import database.DaoFactory;
import pro.nextbit.telegramconstructor.handle.AbsHandle;

public abstract class AbstractHandle extends AbsHandle {

    protected DaoFactory daoFactory = DaoFactory.getInstance();
}

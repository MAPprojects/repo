package Utils;

import java.sql.SQLException;

public interface Observer<E> {
    void notifyEvent(ListEvent<E> e) throws SQLException;
}

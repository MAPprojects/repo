package utils;

import Entities.User;

public class UserEvent extends Event<CRUDEventType, User> {

    public UserEvent(CRUDEventType type, User data) {
        super(type, data);
    }

    @Override
    public CRUDEventType getType() {
        return super.getType();
    }

    @Override
    public User getData() {
        return super.getData();
    }
}

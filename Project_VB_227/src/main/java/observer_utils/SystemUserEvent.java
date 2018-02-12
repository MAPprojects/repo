package observer_utils;

import entities.SystemUser;

public class SystemUserEvent extends Event<SystemUserEventType, SystemUser> {
    public SystemUserEvent(SystemUserEventType tipEvent, SystemUser data) {
        super(tipEvent, data);
    }
}

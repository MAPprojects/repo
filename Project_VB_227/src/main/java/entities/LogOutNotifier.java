package entities;

import java.util.ArrayList;

public class LogOutNotifier {
    ArrayList<LogOutListener> listeners = new ArrayList<>();

    public void addLogOutListener(LogOutListener logOutListener) {
        listeners.add(logOutListener);
    }

    public void deleteLogOutListener(LogOutListener logOutListener) {
        listeners.remove(logOutListener);
    }

    public void notifyListenersOnLogOut() {
        listeners.forEach(listener -> {
            listener.updateOnLogOut();
        });
    }
}

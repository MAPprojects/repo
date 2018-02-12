package entities;

import java.util.ArrayList;

public class LogInNotifier {
    ArrayList<LogInListener> listeners = new ArrayList<>();

    public void addListener(LogInListener logInListener) {
        listeners.add(logInListener);
    }

    public void deleteListener(LogInListener logInListener) {
        listeners.remove(logInListener);
    }

    public void notifyListenersOnLogIn(Role roleOfLogInUser) {
        listeners.forEach(listener -> {
            listener.updateOfLogIn(roleOfLogInUser);
        });
    }
}

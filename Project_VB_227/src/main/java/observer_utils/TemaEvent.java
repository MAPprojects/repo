package observer_utils;

import entities.Tema;

public class TemaEvent extends Event<TemaEventType, Tema> {
    public TemaEvent(TemaEventType tipEvent, Tema data) {
        super(tipEvent, data);
    }
}

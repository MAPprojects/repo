package Utils;

import Domain.Tema;

public class TemaEvent extends Event<Tema, EventType> {
    public TemaEvent(Tema tema, EventType type) {
        super(tema, type);
    }
}
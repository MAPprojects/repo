package Utils;

import Domain.Nota;

public class NotaEvent extends Event<Nota, EventType> {
    public NotaEvent(Nota nota, EventType type) {
        super(nota, type);
    }
}
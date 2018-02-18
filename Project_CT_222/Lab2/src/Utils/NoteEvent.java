package Utils;

import Domain.Nota;
import Domain.Studenti;

public class NoteEvent extends ListEvent<ListEventType, Nota> {
    public NoteEvent(ListEventType type,Nota note) {
        super(type,note);
    }

}

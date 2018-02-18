package Utils;

import Domain.Studenti;

public class StudentiEvent extends ListEvent<ListEventType, Studenti> {
    public StudentiEvent(ListEventType type,Studenti studenti) {
        super(type,studenti);
    }

}

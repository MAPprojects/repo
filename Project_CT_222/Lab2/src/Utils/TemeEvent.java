package Utils;

import Domain.Teme;


public class TemeEvent extends ListEvent<ListEventType, Teme> {
    public TemeEvent(ListEventType type, Teme teme) {
        super(type,teme);
    }

}
package observer_utils;

import entities.Nota;
import vos.NotaVO;

public class NotaEvent extends Event<NotaEventType, NotaVO> {
    public NotaEvent(NotaEventType tipEvent, NotaVO data) {
        super(tipEvent, data);
    }
}

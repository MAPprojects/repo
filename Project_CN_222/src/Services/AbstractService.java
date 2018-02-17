package Services;

import Domain.HasId;
import Domain.Student;
import Utilities.ListEvent;
import Utilities.ListEventType;
import Utilities.Observable;
import Utilities.Observer;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractService<E extends HasId> implements Observable<E> {
    protected static int startingWeek = 39;

    ArrayList<Observer<E>> observers = new ArrayList<>();

    //public abstract E remove(int id);

    protected  <E> ListEvent<E> createEvent(ListEventType type, final E elem, final List<E> l) {
        return new ListEvent<E>(type) {
            @Override
            public List<E> getList() {
                return l;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }

    //returns current week of year
    protected static int getCurrentWeek() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        //System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));

        if (calendar.get(Calendar.WEEK_OF_YEAR) < startingWeek) {
            int w = calendar.get(Calendar.WEEK_OF_YEAR) + 12 - 1;
            return calendar.get(Calendar.WEEK_OF_YEAR) + 12 - 1;
        } else {
            int w = calendar.get(Calendar.WEEK_OF_YEAR) - startingWeek;
            return calendar.get(Calendar.WEEK_OF_YEAR) - startingWeek;
        }
    }

    public <E> List<E> fillter(List<E> lista, Predicate<E> pred, Comparator<E> comp) {
        return lista
                .stream()
                .filter(x -> pred.test(x))
                .sorted(comp::compare)
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(Observer<E> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<E> o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<E> ev) {
        observers.forEach(x->x.notifyEvent(ev));
    }

}

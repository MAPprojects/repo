package Statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Filter {
    public static<E> List<E> filter(List<E> list, Predicate<E> predicate){
        List<E> filteredList = new ArrayList<>();
        list.forEach(elem->{if (predicate.test(elem))filteredList.add(elem);});
        return filteredList;
    }
}

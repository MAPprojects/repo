package Statistics;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class FilterAndSorter {
    public static<E> List<E> filterAndSort(List<E> list, Predicate<E> predicate, Comparator<E> comparator){
        return list.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }
}

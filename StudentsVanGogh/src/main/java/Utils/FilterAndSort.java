package Utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterAndSort<E> {
    public static <E> List<E> filterAndSorter(List<E> list, Predicate<E> p, Comparator<E> c) {
        return list.stream().filter(p).sorted(c).collect(Collectors.toList());
    }
}

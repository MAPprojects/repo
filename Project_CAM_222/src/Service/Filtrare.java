package Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filtrare {
    public static <E> List<E> filtrare(List<E> lista, Predicate<E> predicat) {
        return lista.stream().filter(predicat).collect(Collectors.toList());
    }

    public static <E> List<E> filtrareSortare(List<E> lista, Predicate<E> predicat, Comparator<E> comparator) {
        return lista.stream().filter(predicat).sorted(comparator).collect(Collectors.toList());
    }
}

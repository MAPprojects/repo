package entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@FunctionalInterface
public interface Filter {

    <E> List<E> filter(List<E> elements, Predicate<E> predicate);

    default <E> List<E> defaultFilter(List<E> elements, Predicate<E> predicate) {
        ArrayList<E> result = new ArrayList<>();
        for (E element : elements) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }

    default <E> List<E> defaultFilterAndSort(List<E> elements, Predicate<E> predicate, Comparator<E> comparator) {
        ArrayList<E> result = new ArrayList<>();
        for (E element : elements) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        result.sort(comparator);
        return result;
    }
}

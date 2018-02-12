package entities;

import java.util.List;
import java.util.function.Predicate;

public class FilterImpl implements Filter {
    @Override
    public <E> List<E> filter(List<E> elements, Predicate<E> predicate) {
        return null;
    }
}

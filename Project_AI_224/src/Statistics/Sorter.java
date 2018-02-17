package Statistics;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Sorter {
    public static<E> List<E> sort(List<E> list, Comparator<E> comparator){
        return list.stream().sorted(comparator).collect(Collectors.toList());
    }
}

package repository;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filtre {
    public <E> List<E> filterAndSorter(List<E> lista, Predicate<E> p, Comparator<E> c){
        return lista.stream().filter(p).sorted(c).collect(Collectors.toList());
    }

}

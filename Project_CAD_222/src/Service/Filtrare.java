package Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filtrare {
    public static <E> List<E> filtrare(List<E> lista, Predicate<E> predicat){
        return lista.stream().filter(predicat).collect(Collectors.toList());
    }
}

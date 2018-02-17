package Entites;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterSorter<E> {

    public FilterSorter(){}

    public <E> ArrayList<E> filterAndSorter(List<E> myList, Predicate<E> p, Comparator<E> c) {
        List<E> list = new ArrayList<>();
        if (p == null) {
            //sort
            list.addAll(myList);
            list.sort(c);
        }
        if (c == null) {
            //filtrare
            list = myList.stream().filter(p).collect(Collectors.toList());
        }
        ArrayList<E> newList = new ArrayList<>();
        newList.addAll(list);
        return newList;
    }
}

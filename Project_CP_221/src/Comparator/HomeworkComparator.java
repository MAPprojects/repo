package Comparator;

import Domain.LabHomework;

import java.util.Comparator;

public class HomeworkComparator implements Comparator<LabHomework> {
    @Override
    public int compare(LabHomework o1, LabHomework o2) {
        return o1.compareTo(o2);
    }
}
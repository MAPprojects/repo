package domain;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Tuplu<E> {
    private E entity1;
    private E entity2;

    public Tuplu(E entity1, E entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }

    public E getEntity1() {
        return entity1;
    }

    public E getEntity2() {
        return entity2;
    }

    public void setEntity1(E entity1) {
        this.entity1 = entity1;
    }

    public void setEntity2(E entity2) {
        this.entity2 = entity2;
    }

    public ArrayList<E> getTuplu(){
        ArrayList<E> list=new ArrayList<E>();
        list.add(entity1);
        list.add(entity2);
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuplu<?> tuplu = (Tuplu<?>) o;

        if (entity1 != null ? !entity1.equals(tuplu.entity1) : tuplu.entity1 != null) return false;
        return entity2 != null ? entity2.equals(tuplu.entity2) : tuplu.entity2 == null;
    }

    @Override
    public int hashCode() {
        int result = entity1 != null ? entity1.hashCode() : 0;
        result = 31 * result + (entity2 != null ? entity2.hashCode() : 0);
        return result;
    }
}

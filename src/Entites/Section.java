package Entites;

import Utils.HasID;

public class Section implements HasID<Integer> {
    private static int index = 0;
    private int id;
    private String name;
    private int number;

    public Section(String name, int number) {
        index++;
        this.id = index;
        this.name = name;
        this.number = number;
    }

    public Section(Integer id, String name, int number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    @Override
    public Integer getID() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {
        this.id = integer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public static void setIndex(int index) {
        Section.index = index;
    }

    @Override
    public String toString() {

        return id +
                "\t" + name  +
                "\t" + number;
    }
}

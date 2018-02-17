package Entites;

import Utils.HasID;

public class Candidate implements HasID<Integer> {
    private static int index = 0;
    private int id;
    private String name;
    private String phoneNumber;

    public Candidate(String name, String phoneNumber) {
        index++;
        this.id = index;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public Candidate(Integer id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static void setIndex(int index) {
        Candidate.index = index;
    }

    @Override
    public String toString() {
        return id +
                "\t" + name  +
                "\t" + phoneNumber ;
    }
}


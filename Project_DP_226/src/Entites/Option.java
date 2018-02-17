package Entites;

import Utils.HasID;

public class Option implements HasID<Integer> {
    private int idOption;
    private int idCandidate;
    private int idSection;
    private static int index = 0;
    private int priority;

    public Option(int idCandidate, int idSection, int priority) {
        index++;
        this.idOption = index;
        this.idCandidate = idCandidate;
        this.idSection = idSection;
        this.priority = priority;

    }

    public Option(Integer idOption, Integer idCandidate, Integer idSection, Integer priority) {
        this.idOption = idOption;
        this.idCandidate = idCandidate;
        this.idSection = idSection;
        this.priority = priority;

    }

    public int getIdSection() {
        return idSection;
    }

    public void setIdSection(int idSection) {
        this.idSection = idSection;
    }

    @Override
    public Integer getID() {
        return idOption;
    }

    @Override
    public void setID(Integer integer) {
        idOption = integer;
    }

    public int getIdCandidate() {
        return idCandidate;
    }

    public void setIdCandidate(int idCandidate) {
        this.idCandidate = idCandidate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public static void setIndex(int index) {
        Option.index = index;
    }

    @Override
    public String toString() {
        return idOption +
                "\tCandidate:" + idCandidate +
                "\tSection:" + idSection +
                "\tPriority:" + priority
                ;
    }
}


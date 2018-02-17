package domain;

public class TemaLaborator implements HasID<Integer> {
    private int nr_tema_de_laborator;
    private String cerinta;
    private int deadline;

    public TemaLaborator() {
    }

    /**
     * Constructor
     * @param nr_tema_de_laborator int
     * @param cerinta String
     * @param deadline int (reprezinta 1-14 sapatamna limita de predare a laboratorul pentru obtinerea notei maxime
     */
    public TemaLaborator(int nr_tema_de_laborator, String cerinta, int deadline) {
        this.nr_tema_de_laborator = nr_tema_de_laborator;
        this.cerinta = cerinta;
        this.deadline = deadline;
    }

    /**
     *
     * @return String
     */
    @Override
    public String toString() {
        return nr_tema_de_laborator+";"+cerinta+";"+deadline+"\n";
    }

    /**
     * Getter
     * @return intteger the number of the homework for the lab
     */
    public int getNr_tema_de_laborator() {
        return nr_tema_de_laborator;
    }

    /**
     * Getter
     * @return String the description of the homework
     */
    public String getCerinta() {
        return cerinta;
    }

    /**
     * Getter
     * @return integer-the week, the deadline for the lab homework
     */
    public int getDeadline() {
        return deadline;
    }

    /**
     * Setter
     * @param nr_tema_de_laborator int
     */
    public void setNr_tema_de_laborator(int nr_tema_de_laborator) {
        this.nr_tema_de_laborator = nr_tema_de_laborator;
    }

    /**
     * Setter
     * @param cerinta String
     */
    public void setCerinta(String cerinta) {
        this.cerinta = cerinta;
    }

    /**
     * Setter
     * @param deadline int
     */
    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    @Override
    public Integer getId() {
        return getNr_tema_de_laborator();
    }

    @Override
    public void setId(Integer integer) {
        setNr_tema_de_laborator(integer);
    }
}

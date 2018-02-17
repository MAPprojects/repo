package domain;

public class DetaliiNota {
    private Nota nota;
    private Integer deadline;
    private Integer saptamana_predarii;
    private Boolean intarzieri;
    private Boolean greseli;

    /**
     * Constructor
     * @param nota Nota
     * @param deadline Integer
     * @param saptamana_predarii Integer
     * @param intarzieri Boolean
     * @param greseli Boolean
     */
    public DetaliiNota(Nota nota, Integer deadline, Integer saptamana_predarii, Boolean intarzieri, Boolean greseli) {
        this.nota = nota;
        this.deadline = deadline;
        this.saptamana_predarii = saptamana_predarii;
        this.intarzieri = intarzieri;
        this.greseli = greseli;
    }

    /**
     * Getter
     * @return Nota
     */
    public Nota getNota() {
        return nota;
    }

    /**
     * Getter
     * @return Integer
     */
    public Integer getDeadline() {
        return deadline;
    }

    /**
     * Getter
     * @return Integer
     */
    public Integer getSaptamana_predarii() {
        return saptamana_predarii;
    }

    /**
     * Getter
     * @return Boolean
     */
    public Boolean getIntarzieri() {
        return intarzieri;
    }

    /**
     * Getter
     * @return Boolean
     */
    public Boolean getGreseli() {
        return greseli;
    }

    /**
     * Setter
     * @param nota Nota
     */
    public void setNota(Nota nota) {
        this.nota = nota;
    }

    /**
     * Setter
     * @param deadline Integer
     */
    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    /**
     * Setter
     * @param saptamana_predarii Integer
     */
    public void setSaptamana_predarii(Integer saptamana_predarii) {
        this.saptamana_predarii = saptamana_predarii;
    }

    /**
     * Setter
     * @param intarzieri Boolean
     */
    public void setIntarzieri(Boolean intarzieri) {
        this.intarzieri = intarzieri;
    }

    /**
     * Setter
     * @param greseli Boolean
     */
    public void setGreseli(Boolean greseli) {
        this.greseli = greseli;
    }
}

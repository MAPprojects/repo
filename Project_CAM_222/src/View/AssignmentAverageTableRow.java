package View;

import Domain.Tema;

public class AssignmentAverageTableRow {
    private Tema tema;
    private Float average;

    public AssignmentAverageTableRow(Tema tema, Float average) {
        this.tema = tema;
        this.average = average;
    }

    public Integer getIdTema() {
        return tema.getId();
    }

    public String getDescriereTema() {
        return tema.getDescriere();
    }

    public Float getMedie() {
        return average;
    }
}

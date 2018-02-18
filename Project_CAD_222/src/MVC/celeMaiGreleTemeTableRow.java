package MVC;

import Domain.Tema;

public class celeMaiGreleTemeTableRow {
    private Tema tema;
    private Integer intarziere;

    public celeMaiGreleTemeTableRow(Tema tema, Integer intarziere) {
        this.tema = tema;
        this.intarziere=intarziere;
    }

    public Integer getIdTema() {
        return tema.getId();
    }

    public String getDescriereTema() {
        return tema.getDescriere();
    }

    public Integer getIntarziere() {
        return intarziere;
    }
}

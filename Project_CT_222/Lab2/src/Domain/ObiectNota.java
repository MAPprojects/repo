package Domain;

public class ObiectNota {
    private Integer IDNota;
    private Integer value;
    private String nume;
    private Integer laborator;
    public ObiectNota(Integer IDNota,Integer valoare,String nume,Integer laborator){
        this.value=valoare;
        this.nume=nume;
        this.laborator=laborator;
        this.IDNota=IDNota;
    }

    public Integer getLaborator() {
        return laborator;
    }

    public Integer getValue() {
        return value;
    }
    public String getNume(){
        return nume;
    }

    public Integer getIDNota() {
        return IDNota;
    }
}

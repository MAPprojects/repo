package Domain;

public class GrupeTop {
    private float medie;
    private Integer grupa;
    private float numarStudenti;
    public GrupeTop(float medie,Integer grupa,float studenti){
        this.setMedie(medie);
        this.setGrupa(grupa);
        this.setNumarStudenti(studenti);
    }

    public void setGrupa(Integer grupa) {
        this.grupa = grupa;
    }

    public void setMedie(float medie) {
        this.medie = medie;
    }

    public Integer getGrupa(){
        return grupa;
    }

    public float getMedie(){
        return medie;
    }

    public void setNumarStudenti(float numarStudenti) {
        this.numarStudenti = numarStudenti;
    }

    public float getNumarStudenti(){
        return numarStudenti;
    }
}

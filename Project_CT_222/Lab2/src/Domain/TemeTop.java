package Domain;

public class TemeTop {
    private Integer temaNr;
    private float Medie;


    public TemeTop(Integer temaNr,float Medie){
        this.setTemaNr(temaNr);
        this.setMedie(Medie);

    }

    public void setTemaNr(Integer tema){
        this.temaNr=tema;
    }
    public void setMedie(float medie){
        this.Medie=medie;
    }
    public Integer getTemaNr(){
        return temaNr;
    }

    public float getMedie() {
        return Medie;
    }

}

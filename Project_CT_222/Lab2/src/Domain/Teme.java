package Domain;

public class Teme implements HasID<Integer> {
    private Integer NumarLaborator;
    private String cerinta;
    private Integer deadline;
    public Teme(){}
    public Teme(Integer NumarLaborator,String cerinta,Integer deadline){
        this.setId(NumarLaborator);
        this.setCerinta(cerinta);
        this.setDeadline(deadline);
    }
    public Teme(Teme other){
        this.NumarLaborator=other.NumarLaborator;
        this.deadline=other.deadline;
        this.cerinta=other.cerinta;
    }
    public Integer getId(){
        return NumarLaborator;
    }
    public String getCerinta(){
        return cerinta;
    }
    public Integer getDeadline(){
        return deadline;
    }
    public void setId(Integer NumarLaborator){
        this.NumarLaborator=NumarLaborator;
    }
    public void setCerinta(String cerinta){
        this.cerinta=cerinta;
    }
    public void setDeadline(Integer deadline){
        this.deadline=deadline;
    }

    public Integer getNumarLaborator() {
        return NumarLaborator;
    }

    @Override
    public String toString(){
        return "NumarLab: "+NumarLaborator+"     Cerinta: "+cerinta+"     Deadline: "+deadline;
    }
}

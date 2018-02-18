package Domain;

public class Nota implements HasID<Integer> {
    private Integer NotaID;
    private Integer idStudent;
    private Integer idTema;
    private Integer valoare;
    public Nota(){}
    public Nota(Integer ID,Integer idStudent,Integer idTema,Integer valoare){
        this.setId(ID);
        this.setStudent(idStudent);
        this.setTema(idTema);
        this.setValoare(valoare);
    }
    public void setValoare(Integer valoare){
        this.valoare=valoare;
    }
    public void setId(Integer id){
        this.NotaID=id;
    }
    public void setStudent(Integer idStudent){
        this.idStudent=idStudent;
    }
    public void setTema(Integer idTema){
        this.idTema=idTema;
    }
    public Integer getIdStudent(){
        return idStudent;
    }
    public Integer getIdTema(){
        return idTema;
    }
    public Integer getId(){
        return NotaID;
    }
    public Integer getValoare(){
        return valoare;
    }
    public String toString(){
        return "NotaID: "+ NotaID + "     StudentID " + idStudent+ "     TemaID: " + idTema + "     Valoare: " + valoare;
    }

}

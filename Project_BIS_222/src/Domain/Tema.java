package Domain;

// numarul temei de laborator (identificator unic), descrierea pe scurt a cerintei, termen limita de predare (deadline),
public class Tema implements HasID<Integer>
{
    private Integer idTema;
    private String descriere;
    private int deadline;

    public Tema(Integer nrTema, String descriere, int deadline)
    {
        this.idTema = nrTema;
        this.descriere = descriere;
        this.deadline = deadline;
    }

    public Tema(Integer idTema)
    {
        this.idTema=idTema;
    }

    public String getDescriere()
    {
        return descriere;
    }
    public void setDescriere(String descriere){this.descriere=descriere;}

    public Integer getDeadline()
    {
        return deadline;
    }
    public void setDeadline(int deadline){this.deadline=deadline;}
    @Override
    public Integer getID()
    {
        return idTema;
    }
    @Override
    public void setID(Integer id)
    {
        this.idTema=id;
    }
    @Override
    public String toString()
    {
        return ""+this.idTema+" "+this.descriere+" "+this.deadline;
    }
}

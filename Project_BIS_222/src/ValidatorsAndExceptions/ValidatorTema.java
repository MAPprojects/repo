package ValidatorsAndExceptions;


import Domain.Tema;

public class ValidatorTema implements Validator<Tema>
{
    public void validate(Tema tema)
    {
        String erori="";
        if(tema.getID()<0)
        {
            erori=erori+"ID tema negativ\n";
        }
        if(tema.getDescriere().equals(""))
        {
            erori=erori+"Descriere tema vida\n";
        }
        if(tema.getDeadline()<1 || tema.getDeadline()>15)
        {
            erori=erori+"Deadline-ul e cuprins intre saptamanile 1-14\n";
        }
        if(!erori.equals(""))
        {
            throw new ValidationException(erori);
        }
    }
}

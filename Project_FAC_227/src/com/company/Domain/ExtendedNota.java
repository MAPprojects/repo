package com.company.Domain;

import com.company.Service.Service;

public class ExtendedNota
{
    private Nota nota;
    private String nume;
    private String tema;
    public static Service service;



    public ExtendedNota(Nota nota)
    {
        this.nota = nota;
        for (Student st:service.getStudents())
        {
            if (st.getID() == nota.getIdStudent()) {
                nume = st.getNume();
                break;
            }
        }
        for(Tema t:service.getTeme())
            if(t.getID() == nota.getNrTema())
            {
                tema = t.getDescriere();
                break;
            }
    }

    public Nota getNota() {
        return nota;
    }

    public String getNume() {
        return nume;
    }

    public String getTema() {
        return tema;
    }
}
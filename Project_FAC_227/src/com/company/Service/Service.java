package com.company.Service;

import com.company.Domain.*;
import com.company.Exceptions.RepositoryException;
import com.company.Exceptions.ServiceException;
import com.company.Repositories.NotaRepository;
import com.company.Repositories.SQLStudentRepository;
import com.company.Repositories.TemaRepository;
import com.company.Repositories.StudentRepository;
import com.company.Utility.Observer;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

public class Service {

    private SQLStudentRepository studentRepository;
    private TemaRepository temaRepository;
    private NotaRepository notaRepository;
    //private ArrayList<Account> accounts;

    public Service(StudentRepository studentRepository, TemaRepository temaRepository, NotaRepository notaRepository) {
        this.studentRepository = (SQLStudentRepository)studentRepository;
        this.temaRepository = temaRepository;
        this.notaRepository = notaRepository;
        /*this.accounts=new ArrayList<>();
        accounts.add(new Account("admin","admin",2));
        for(Student st: getStudents())
        {
            accounts.add(new Account(st.getID()+"_"+st.getNume(),"parola",1));
        }*/
    }

    public void addStudent(int idStudent, String nume, int grupa, String email, String profLab) throws ServiceException {
        Student student = new Student(idStudent, nume, grupa, email, profLab);
        try {
            studentRepository.save(student);
            //accounts.add(new Account(student.getID()+"_"+student.getNume(),"parola",1));
        } catch (RepositoryException e) {
            //System.out.println(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteStudent(int idStudent) throws ServiceException {
        try{
            boolean exists = false;
            for(Student st: studentRepository.findAll())
                if(st.getID()==idStudent)
                    exists=true;
            if(exists) {
                /*for (Account account : accounts) {
                    if (account.getUsername() != "admin") {
                        String[] strings = account.getUsername().split("_");
                        if (idStudent == Integer.parseInt(strings[0])) {
                            accounts.remove(account);
                            break;
                        }
                    }
                }*/
                ArrayList<Nota> note = new ArrayList<>();
                note.addAll((Collection<? extends Nota>) notaRepository.findAll());
                for (Nota nota : note) {
                    if (nota.getIdStudent() == idStudent) {
                        int id = 1000 * nota.getIdStudent() + nota.getNrTema();
                        notaRepository.delete(id);
                    }
                }
                Student stDelete = studentRepository.delete(idStudent).get();
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void updateStudent(int idStudent, String nume, int grupa, String email, String profLab) throws ServiceException {
        Student student = new Student(idStudent, nume, grupa, email, profLab);
        try {
            studentRepository.update(student);
        } catch (RepositoryException e) {
            //System.out.println(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public void addTema(int nrTema, String descriere, int deadline) throws ServiceException {
        Tema tema = new Tema(nrTema, descriere, deadline);
        try {
            temaRepository.save(tema);
        } catch (RepositoryException e) {
            //System.out.println(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }


    public void modificareTermen(int nrTema, int newDeadline) throws ServiceException {
        Tema tema = null;
        try {
            tema = temaRepository.findEntity(nrTema);
            if (tema.getDeadline() < newDeadline && tema.getDeadline() > Globals.getInstance().getSaptCurenta() && newDeadline <= 14) {
                temaRepository.update(nrTema, tema.getDescriere(), newDeadline);
            } else
                throw new RepositoryException("Noul deadline nu respecta conditiile!");
        } catch (RepositoryException e) {
            //System.out.println(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    public void deleteTema(int nrTema) throws ServiceException {
        try{
            boolean exists=false;
            for(Tema tema: temaRepository.findAll())
                if(tema.getID()==nrTema)
                    exists=true;
            if(exists) {
                for (Nota nota : notaRepository.findAll()) {
                    if (nrTema == nota.getNrTema()) {
                        int id = nota.getIdStudent() * 1000 + nota.getNrTema();
                        notaRepository.delete(id);
                    }
                }
                Tema tema = temaRepository.delete(nrTema).get();
            }
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void adaugaNota(int idStudent, int nrTema, int vnota) throws ServiceException {

        try {
            Student student = studentRepository.findEntity(idStudent);
            Tema tema = temaRepository.findEntity(nrTema);
            if (vnota < 1 || vnota >10)
                throw new ServiceException("Nota invalida!");
            if (tema.getDeadline() < Globals.getInstance().getSaptCurenta())
                vnota = vnota - (Globals.getInstance().getSaptCurenta() - tema.getDeadline()) * 2;
            if (vnota < 1 || tema.getDeadline()+2 < Globals.getInstance().getSaptCurenta())
                vnota = 1;
            Nota nota = new Nota(idStudent, nrTema, vnota);
            notaRepository.save(nota);

            String studentFileName = "Data\\" + student.getID() + ".txt";
            BufferedWriter wr = new BufferedWriter(new FileWriter(studentFileName,true));

            String line = "Adaugat nota: Tema-" + nrTema + " Nota-" + vnota + " Deadline-" + tema.getDeadline() + " Saptamana predarii-" +Globals.getInstance().getSaptCurenta();
            if (tema.getDeadline() < Globals.getInstance().getSaptCurenta())
                line += "(Penalizare: " + (Globals.getInstance().getSaptCurenta() - tema.getDeadline())*2 + " puncte)";

            wr.write(line);
            wr.write("\n");
            wr.close();

        } catch (RepositoryException e) {
            //System.out.println(e.getMessage());
            throw new ServiceException(e.getMessage());
        } catch (IOException e) {
            //System.out.println("Eroare la scrierea in fisierul studentului!");
            throw new ServiceException("Eroare la scrierea in fisierul studentului!");
        } catch (ServiceException e){
            throw e;
        }
    }

    public void deleteNota(int idStudent, int nrTema) throws ServiceException {
        try{
            notaRepository.delete(1000 * idStudent + nrTema);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public void modificareNota(int idStudent, int nrTema, int vnota) throws ServiceException {
        try {
            Student student = studentRepository.findEntity(idStudent);
            Tema tema = temaRepository.findEntity(nrTema);
            Nota nota = notaRepository.findEntity(idStudent * 1000 + nrTema);
            if (vnota < 1 || vnota >10)
                throw new ServiceException("Nota invalida!");
            if (tema.getDeadline() < Globals.getInstance().getSaptCurenta())
                vnota = vnota - (Globals.getInstance().getSaptCurenta() - tema.getDeadline()) * 2;
            if (vnota < 1 || tema.getDeadline()+2 < Globals.getInstance().getSaptCurenta())
                vnota = 1;
            if (vnota > nota.getNota()) {
                notaRepository.update(idStudent, nrTema, vnota);

                String studentFileName = "Data\\" + student.getID() + ".txt";
                BufferedWriter wr = new BufferedWriter(new FileWriter(studentFileName, true));

                String line = "Modificat nota: Tema-" + nrTema + " Nota-" + vnota + " Deadline-" + tema.getDeadline();
                if (tema.getDeadline() < Globals.getInstance().getSaptCurenta())
                    line += "(Penalizare: " + (Globals.getInstance().getSaptCurenta() - tema.getDeadline()) * 2 + " puncte)";

                wr.write(line);
                wr.write("\n");
                wr.close();
            }
        } catch (RepositoryException e) {
            //System.out.println(e.getMessage());
            throw new ServiceException(e.getMessage());
        } catch (IOException e) {
            //System.out.println("Eroare la scrierea in fisierul studentului!");
            throw new ServiceException("Eroare la scrierea in fisierul studentului!");
        } catch (ServiceException e){
            throw e;
        }
    }

    public <E> List<E> filterAndSorter(List<E> lista, Predicate<E> predicate, Comparator<E> comparator)
    {
        final ArrayList<E> filteredList = new ArrayList<>();
        ArrayList<E> sortedList = new ArrayList<>();
        if(predicate != null) {
            lista.forEach(x -> {
                if (predicate.test(x)) filteredList.add(x);
            });
            sortedList = filteredList;
        }
        else
            sortedList = (ArrayList<E>) lista;
        if(comparator != null)
            sortedList.sort(comparator);
        return sortedList;
    }

    public List<Student> studenti_Fprof_Sgrupa(String prof)
    {
        ArrayList<Student> lista = new ArrayList<Student> ((Collection<? extends Student>)(studentRepository.findAll()));

        Predicate<Student> areProfesorul;
        areProfesorul = x->(x.getProfLab().equals(prof));
        Comparator<Student> comparareGrupa;
        comparareGrupa = (x,y)-> (int)(x.getGrupa()-y.getGrupa());
        return filterAndSorter(lista,areProfesorul, comparareGrupa);
    }

    public List<Student> studenti_Femail_Snume(String email)
    {
        ArrayList<Student> lista = new ArrayList<Student>((Collection<? extends Student>)(studentRepository.findAll()));

        Predicate<Student> eInDomeniul;
        eInDomeniul = x->(x.getEmail().split("@")[1].equals(email));
        Comparator<Student> comparareNume;
        comparareNume = (x,y)-> (int)x.getNume().compareTo(y.getNume());
        return filterAndSorter(lista,eInDomeniul,comparareNume);
    }

    public List<Student> studenti_Fnota_Snume(int vnota){
        ArrayList<Student> listaStudenti = new ArrayList<Student>((Collection<? extends Student>)(studentRepository.findAll()));
        ArrayList<Nota> listaNote = new ArrayList<Nota>((Collection<? extends Nota>) notaRepository.findAll());
        ArrayList<Integer> idStudenti = new ArrayList<>();
        Predicate<Nota> notaMaiMareCa;
        notaMaiMareCa = x->(x.getNota() > vnota);
        listaNote = (ArrayList<Nota>)(filterAndSorter(listaNote,notaMaiMareCa,null));
        for (Nota n:listaNote)
        {
            idStudenti.add(n.getIdStudent());
        }
        Predicate<Student> areNota;
        areNota = x->(idStudenti.contains(x.getID()));
        Comparator<Student> comparareNume;
        comparareNume = (x,y)-> (int)x.getNume().compareTo(y.getNume());
        return filterAndSorter(listaStudenti,areNota,comparareNume);
    }

    public List<Tema> teme_Fdeadline_Sdeadline(int deadline)
    {
        ArrayList<Tema> listaTeme = new ArrayList<Tema>((Collection<? extends Tema>) temaRepository.findAll());
        Predicate<Tema> deadlineMaiMic;
        deadlineMaiMic = x->(x.getDeadline()<=deadline);
        Comparator<Tema> comparaDeadline;
        comparaDeadline = (x,y)->(int)(x.getDeadline()-y.getDeadline());
        return filterAndSorter(listaTeme,deadlineMaiMic,comparaDeadline);
    }

    public List<Tema> teme_Fdescriere_Sdeadline(String sequence)
    {
        ArrayList<Tema> listaTeme = new ArrayList<Tema>((Collection<? extends Tema>) temaRepository.findAll());
        Predicate<Tema> temaContineStringul;
        temaContineStringul = x->(x.getDescriere().contains(sequence));
        Comparator<Tema> comparaDeadline;
        comparaDeadline = (x,y)->(int)(x.getDeadline()-y.getDeadline());
        return filterAndSorter(listaTeme,temaContineStringul,comparaDeadline);
    }

    public List<Tema> teme_Fnote_Sdescriere()
    {
        ArrayList<Tema> listaTeme = new ArrayList<Tema>((Collection<? extends Tema>) temaRepository.findAll());
        ArrayList<Nota> listaNote = new ArrayList<Nota>((Collection<? extends Nota>) notaRepository.findAll());

        ArrayList<Integer> nrTeme = new ArrayList<>();
        for (Nota n:listaNote)
        {
            nrTeme.add(n.getNrTema());
        }
        Predicate<Tema> nuExistaNota;
        nuExistaNota = x->(!(nrTeme.contains(x.getID())));
        Comparator<Tema> comparareNume;
        comparareNume = (x,y)-> (int)x.getDescriere().compareTo(y.getDescriere());
        return filterAndSorter(listaTeme,nuExistaNota,comparareNume);

    }

    public List<Nota> nota_Fstudent_Screscator(int idStudent)
    {
        ArrayList<Nota> listaNote = new ArrayList<Nota>((Collection<? extends Nota>) notaRepository.findAll());
        Predicate<Nota> notaStudentului;
        notaStudentului = x->(x.getIdStudent()==idStudent);
        Comparator<Nota> comparatorNota;
        comparatorNota = (x,y)-> (int)(x.getNota()-y.getNota());
        return filterAndSorter(listaNote,notaStudentului,comparatorNota);
    }

    public List<Nota> nota_Ftema_Sdescrescator(int nrTema)
    {
        ArrayList<Nota> listaNote = new ArrayList<Nota>((Collection<? extends Nota>) notaRepository.findAll());
        Predicate<Nota> notaTemei;
        notaTemei = x->(x.getNrTema()==nrTema);
        Comparator<Nota> comparatorNota;
        comparatorNota = (x,y)-> (int)(y.getNota()-x.getNota());
        return filterAndSorter(listaNote,notaTemei,comparatorNota);
    }

    private static boolean maiMicCa5(Nota nota) { return nota.getNota()<5;}
    private static int comparaNota(Nota nota1, Nota nota2) {return nota1.getNota()-nota2.getNota();}

    public List<Nota> nota_Fsub5_Stema()
    {
        ArrayList<Nota> listaNote = new ArrayList<Nota>((Collection<? extends Nota>) notaRepository.findAll());
        Predicate<Nota> maiMicCa5;
        maiMicCa5 = Service::maiMicCa5;
        Comparator<Nota> comparaNota;
        comparaNota = Service::comparaNota;
        return filterAndSorter(listaNote,maiMicCa5,comparaNota);

    }

    public Iterable<Student> getStudents()
    {
        return studentRepository.findAll();
    }

    public Iterable<Tema> getTeme() {return temaRepository.findAll();}

    public Iterable<Nota> getNote() {return notaRepository.findAll();}

    public void addObserverOnStudents(Observer<Student> observer)
    {
        studentRepository.addObserverOnMap(observer);
    }
    public void addObserverOnTeme(Observer<Tema> observer) {temaRepository.addObserverOnMap(observer);}
    public void addObserverOnNote(Observer<Nota> observer) {notaRepository.addObserverOnMap(observer);}

    public Map<Integer,String> getAccounts() {
        return studentRepository.getAllPasswords();
    }

    public void setAccountPassword(int id, String password)
    {
        studentRepository.setPassword(id,password);
    }

    public ArrayList<Double> raportMediePonderata()
    {
        List<Nota> note = new ArrayList<>();
        note.addAll((Collection<? extends Nota>) getNote());
        ArrayList<Tema> teme = new ArrayList<>();
        teme.addAll((Collection<? extends Tema>) getTeme());
        ArrayList<Double> rezultat = new ArrayList<>();
        for(Student st:getStudents()) {
            Predicate<Nota> noteleStudentului;
            noteleStudentului = x -> (x.getID() == st.getID());
            List<Nota> listaNote = new ArrayList<>() {};//filterAndSorter(note,noteleStudentului,null);
            for(Nota nota: note)
            {
                if(nota.getIdStudent()==st.getID())
                    listaNote.add(nota);
            }
            double sumaNote=0;
            sumaNote= teme.size()-listaNote.size();
            for (Nota nota: listaNote) {
                sumaNote=sumaNote+nota.getNota();
            }
            sumaNote=sumaNote/teme.size();
            rezultat.add(Double.valueOf(st.getID()));
            rezultat.add(sumaNote);
        }
        return rezultat;
    }

    public ArrayList<Tema> hardest3(List<Integer> list) {
        ArrayList<Student> students = new ArrayList<>();
        students.addAll((Collection<? extends Student>) getStudents());
        ArrayList<Integer> penalizate = new ArrayList<>();
        for (Student st : students) {
            BufferedReader read = null;
            try {
                read = new BufferedReader(new FileReader("Data//" + st.getID() + ".txt"));
                String line = read.readLine();
                while (line != null) {
                    if (line.contains("Penalizare") && line.compareTo("")!=0)
                    {
                        //System.out.println(line.contains("Penalizare"));
                        String[] splitByLine = line.split("-");
                        String[] splitBySpace = splitByLine[1].split(" ");
                        int idTema = Integer.parseInt(splitBySpace[0]);
                        penalizate.add(idTema);
                    }
                    line = read.readLine();
                    //line = read.readLine();
                }
                read.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(penalizate);
        ArrayList<Integer> occurance = new ArrayList<>();
        while (penalizate.size() > 0) {
            occurance.add(penalizate.get(0));
            occurance.add(Collections.frequency(penalizate, penalizate.get(0)));
            penalizate.removeAll(Collections.singleton(penalizate.get(0)));
        }
        ArrayList<Integer> results = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int max = 0;
            int index = -1;
            int val = 0;
            for (int j = 1; j < occurance.size(); j += 2) {
                if (occurance.get(j) > max) {
                    max = occurance.get(j);
                    val = occurance.get(j - 1);
                    index = j - 1;
                }

            }
            results.add(val);

            list.add(max);

            if(val!=0) {
                occurance.remove(index + 1);
                occurance.remove(index);
            }
        }
        ArrayList<Tema> toReturn = new ArrayList<>();
        Tema tema1 = null;
        Tema tema2 = null;
        Tema tema3 = null;
        for(Tema tema:getTeme())
        {
            if(tema.getID()==results.get(0) && results.get(0)!=0)
            {
                tema1=tema;
                //toReturn.add(0,tema);
            }
            if(tema.getID()==results.get(1) && results.get(1)!=0)
            {
                tema2=tema;
                //toReturn.add(1,tema);
            }
            if(tema.getID()==results.get(2) && results.get(2)!=0)
            {
                tema3=tema;
                //toReturn.add(2,tema);
            }
        }
        toReturn.add(tema1);
        toReturn.add(tema2);
        toReturn.add(tema3);
        return toReturn;
    }

    public ArrayList<Student> noPenalityStudents()
    {
        ArrayList<Student> result = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        students.addAll((Collection<? extends Student>) getStudents());
        ArrayList<Integer> penalizate = new ArrayList<>();
        for (Student st : students) {
            boolean penalizat = false;
            BufferedReader read = null;
            try {
                read = new BufferedReader(new FileReader("Data//" + st.getID() + ".txt"));
                String line = read.readLine();
                while (line != null) {
                    if (line.contains("Penalizare") && line.compareTo("")!=0)
                    {
                        penalizat=true;
                        break;
                    }
                    line = read.readLine();
                }
                read.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!penalizat)
            {
                result.add(st);
            }
        }
        return result;
    }

}

package Service;

import Domain.DTOgrades;
import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repository.IRepository;
import Repository.RepositoryException;
import ValidatorsAndExceptions.ServiceException;
import javafx.scene.control.ComboBox;
import javafx.util.Pair;

import javax.swing.text.html.HTMLDocument;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Service extends java.util.Observable {

    private IRepository<Student,Integer> repoStudent;
    private IRepository<Tema,Integer> repoTema;
    private IRepository<Nota,Pair<Integer,Integer>> repoNota;
    private static int sapt_curenta;

    public int current_week()
    {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int current_week=week-39;
        if(current_week<=0)
            current_week=11+week;
        return current_week;
    }

    private void initRepoNota()
    {
        for(Nota nota: repoNota.getAll())
        {
            Student student=repoStudent.findOne(nota.getStudent().getID());
            Tema tema =repoTema.findOne(nota.getTema().getID());
            nota.getStudent().setNume(student.getNume());
            nota.getStudent().setCadruDidactic(student.getCadruDidactic());
            nota.getStudent().setEmail(student.getEmail());
            nota.getStudent().setGrupa(student.getGrupa());
            nota.getTema().setDeadline(tema.getDeadline());
            nota.getTema().setDescriere(tema.getDescriere());
        }
    }
    public Service(IRepository<Student,Integer> repoStudent,IRepository<Tema,Integer> repoTema, IRepository<Nota,Pair<Integer,Integer>> repoNota)
    {
       this.repoStudent=repoStudent;
       this.repoTema=repoTema;
       this.repoNota=repoNota;
       initRepoNota();
       sapt_curenta=current_week();
    }

    public Iterable<Student> getAllStudents()
    {
        return repoStudent.getAll();
    }

    public void addStudent(Integer idStudent, String nume, int grupa, String email, String cadruDidactic)
    {
        Student student=new Student(idStudent, nume, grupa, email, cadruDidactic);
        repoStudent.add(student);
        setChanged();
        notifyObservers();
    }

    public Student findStudentByID(Integer idStudent)
    {
        return repoStudent.findOne(idStudent);
    }

    public Student deleteStudent(Integer idStudent)
    {
        deleteNoteByIDStudent(idStudent);
        Student student_sters= repoStudent.delete(idStudent);
        setChanged();
        notifyObservers();
        return  student_sters;
    }
    public void deleteNoteByIDStudent(Integer idStudent)
    {
        Iterable<Nota> note=repoNota.getAll();
        for(Nota nota:note)
        {
            if(nota.getStudent().getID()==idStudent)
                repoNota.delete(nota.getID());
        }
    }
    public void updateStudent(Integer idStudent, String nume, int grupa, String email, String cadruDidactic)
    {
        Student nou=new Student(idStudent, nume, grupa, email, cadruDidactic);
        repoStudent.update(nou);
        setChanged();
        notifyObservers();
    }

    public int sizeStudent()
    {
        return repoStudent.size();
    }

    public void addTema(Integer idTema,String descriere,int deadline)
    {
        Tema tema=new Tema(idTema,descriere,deadline);
        repoTema.add(tema);
        setChanged();
        notifyObservers();
    }

    public Tema deleteTema(Integer idTema)
    {
        Tema tema_sters= repoTema.delete(idTema);
        setChanged();
        notifyObservers();
        return tema_sters;
    }

    /* F2: modificarea termenului de predare pt o tema existenta (daca nr saptamanii in care se modifica
       este ma mic decat nr saptamanii in care a fost stabilit termenul de predare)*/
       public void modifyHomework(Integer idTema, int new_deadline,String newDescription)
    {
        Tema tema_veche=repoTema.findOne(idTema);
        tema_veche.setDescriere(newDescription);
        if(sapt_curenta<tema_veche.getDeadline() && new_deadline>=tema_veche.getDeadline() )
        {
            Tema tema_noua=new Tema(idTema,tema_veche.getDescriere(),new_deadline);
            repoTema.update(tema_noua);
            setChanged();
            notifyObservers();
        }
        else
        {
            throw new ServiceException("Deadline-ul nu poate fi modificat");
        }
    }
//    a adaugarea unei note se vor adauga si in fisierul IdStudent.txt urmatoarele informatii:
//    Adaugare nota, NrTema, Nota, Deadline, Saptamana Predarii temei, Observatii care
//    contin explicatii in legatura cu depunctarile efectuate (daca este cazul)
    public void addNota(Integer idStudent,Integer idTema, int sapt_predare,int valoare, String observatii)
    {
        Student student=repoStudent.findOne(idStudent);
        Tema tema=repoTema.findOne(idTema);
        int val=valoare;
        if(sapt_predare-tema.getDeadline()>2)
        {
            val=1;
        }
        else if (sapt_predare>tema.getDeadline())
        {
            val=val-2*(sapt_predare-tema.getDeadline());
        }
        Nota nota=new Nota(student,tema,val,sapt_predare);
        repoNota.add(nota);
        // adauga in fisier(nota,observatii)
        adaugaIstoric(nota,sapt_predare,observatii,"S-a adaugat nota");
        setChanged();
        notifyObservers();
    }
    public HashMap<Integer,Integer> getIntarzieri()
    {
        HashMap<Integer,Integer> intarzieri=new HashMap<>();
        for (Nota nota:repoNota.getAll())
        {
            if(nota.getSaptPredare()>nota.getTema().getDeadline())
            {
                if(!intarzieri.containsKey(nota.getTema().getID()))
                    intarzieri.put(nota.getTema().getID(),1);
                else
                {
                    intarzieri.replace(nota.getTema().getID(),intarzieri.get(nota.getTema().getID()),intarzieri.get(nota.getTema().getID())+1);
                    //intarzieri.get(idTema)=intarzieri.get(idTema)+1;
                }
            }

        }
        return intarzieri;
    }

    public HashMap<Integer,Integer> getNoteIntre()
    {
        HashMap<Integer,Integer> toateNotele=new HashMap<>();
        for (Nota nota:repoNota.getAll())
        {
            if(!toateNotele.containsKey(nota.getValoare()))
                    toateNotele.put(nota.getValoare(),1);
            else
                {
                    toateNotele.replace(nota.getValoare(),toateNotele.get(nota.getValoare()),toateNotele.get(nota.getValoare())+1);
                }
        }
        return toateNotele;
    }


    public void modifyNota(Integer idStudent,Integer idNota, int sapt_predare, int valoare,String observatii)
    {
        Nota nota=repoNota.findOne(new Pair<Integer, Integer>(idStudent,idNota));
        Tema tema=nota.getTema();
        Student student=nota.getStudent();
        int val=valoare;
        if(sapt_predare-tema.getDeadline()>2)
        {
            val=1;
        }
        else if (sapt_predare>tema.getDeadline())
        {
            val=val-2*(sapt_predare-tema.getDeadline());
        }
        if(nota.getValoare()<val)
        {
            Nota nota_noua = new Nota(student, tema, val,sapt_predare);
            repoNota.update(nota_noua);
            adaugaIstoric(nota_noua, sapt_predare, observatii, "S-a modificat nota in");
            setChanged();
            notifyObservers();
        }
        else
        {
            throw new RepositoryException("Nota a ramas nemodificata");
        }

    }
    private void adaugaIstoric(Nota nota,int sapt_predare, String observatii,String actiune)
    {
        String fileName="src\\Resources\\"+nota.getStudent().getID()+"Student.txt";
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(fileName,true)))
        {
            writer.write("\n"+actiune+" "+nota.getValoare()+" pentru tema cu numarul "+nota.getTema().getID()+
                          " care avea deadline-ul in saptamana "+nota.getTema().getDeadline()+" si a fost predata in saptamana "+
                            sapt_predare+"\nObservatii: "+observatii+"\n_______________________________________________________");
        } catch (IOException e)
        {
            System.err.println(e);
        }
    }
    public int sizeTema()
    {
        return repoTema.size();
    }
    public Iterable<Tema> getAllHomeworks()
    {
        return repoTema.getAll();
    }

    public Tema getHomeworkByNr(int nr) throws Exception
    {
        for (Tema tema:repoTema.getAll())
        {
            if(tema.getID()==nr)

            {
                return tema;
            }

        }
        throw new Exception("Tema cu acest numar nu exista");
    }
    public Nota getGradeByNrAndIdStudent(int nr,int id) throws Exception
    {
        for (Nota nota :repoNota.getAll())
        {
            if(nota.getTema().getID()==nr && nota.getStudent().getID()==id)
            {
                return nota;
            }
        }
        throw new Exception("Nu exista nota la aceasta tema pentru acest student");
    }

    public <E>List<E> filterAndSorter(List<E> lista, Predicate<E> predicate, Comparator<E> comparator)
    {
        return lista.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }


    public <E>ArrayList<E> iterableToArrayList(Iterable<E> iterable)
    {
         ArrayList<E> arrayList=new ArrayList<>();
        if(iterable!=null)
        {
            for (E e:iterable)
                arrayList.add(e);
        }
        return arrayList;
    }
    public List<Student> filterByGroupSortedByName(List<Student> studenti,int grupa)
    {
        //Iterable<Student> iterable=repoStudent.getAll();
        //ArrayList<Student> studenti=iterableToArrayList(iterable);
        Predicate<Student> predicate=x->x.getGrupa()==grupa;
        Comparator<Student> comparator=(x,y)->{return x.getNume().compareTo(y.getNume());};
        List<Student> studenti_fil=filterAndSorter(studenti,predicate,comparator);
        return studenti_fil;
    }
    public List<Student> filterByTeacherSortedByGroup(List<Student>studenti,String nume_profesor)
    {
        //Iterable<Student> iterable=repoStudent.getAll();
        //ArrayList<Student> studenti=iterableToArrayList(iterable);
        Predicate<Student> predicate=x->x.getCadruDidactic().toLowerCase().contains(nume_profesor.toLowerCase());
        Comparator<Student> comparator=(x,y)->{if(x.getGrupa()>y.getGrupa())return 1;
                                               if(x.getGrupa()<y.getGrupa())return -1;
                                                return 0;};
        List<Student> studenti_fil=filterAndSorter(studenti,predicate,comparator);
        return studenti_fil;
    }

    public List<Student> filterByStudentNameSortedByGroup(List<Student> studenti,String nume_student)
    {
        //Iterable<Student> iterable=repoStudent.getAll();
        //ArrayList<Student> studenti=iterableToArrayList(iterable);
        Predicate<Student> predicate=x->x.getNume().toLowerCase().contains(nume_student.toLowerCase());
        Comparator<Student> comparator=(x,y)->{if(x.getGrupa()>y.getGrupa())return 1;
                                                if(x.getGrupa()<y.getGrupa())return -1;
                                                return 0;};
        List<Student> studenti_fil=filterAndSorter(studenti,predicate,comparator);
        return studenti_fil;
    }
    public List<Student> filterAllStudent(String nume_student,int grupa,String cadru_didactic)
    {
        List<Student> filtrare=new ArrayList<>();
        filtrare=iterableToArrayList(repoStudent.getAll());
        if(nume_student!="")
        {
            filtrare=filterByStudentNameSortedByGroup(filtrare,nume_student);
        }
        if (grupa!=-1 )
        {
            filtrare=filterByGroupSortedByName(filtrare,grupa);

        }
        if(cadru_didactic!="")
        {
            filtrare=filterByTeacherSortedByGroup(filtrare,cadru_didactic);
        }
        return  filtrare;
    }

    public List<Tema> filterAllHomework(String description,int deadline)
    {
        List<Tema> filtrare=new ArrayList<>();
        filtrare=iterableToArrayList(repoTema.getAll());
        if(description!="")
        {
            filtrare=filterByDescriptionSortedByDeadline(filtrare,description);
        }
        if (deadline!=-1 )
        {
            filtrare=filterByDeadlineSortedByDescription(filtrare,deadline);

        }
        return filtrare;
    }

    public List<DTOgrades> filterAllDTO(String name,int group)
    {
        List<DTOgrades> filtrare=new ArrayList<>();
        filtrare=iterableToArrayList(getAllDtoGrades());

        Predicate<DTOgrades> predicateName=x->x.getStudent().getNume().equals(x.getStudent().getNume());
        Predicate<DTOgrades> predicateGroup=x->x.getGroup()==x.getGroup();
        Comparator<DTOgrades> comparator=(x,y)->{return  x.getStudent().getNume().compareTo(y.getStudent().getNume());};

        if(name!="")
        {
            predicateName=x->x.getStudent().getNume().toLowerCase().contains(name.toLowerCase());

        }
        if (group!=-1 )
        {
            predicateGroup=x->x.getGroup()==group;

        }
        filtrare=filterAndSorter(filtrare,predicateName.and(predicateGroup),comparator);
        return filtrare;
    }

    public List<Tema> filterByDeadlineSortedByDescription(List<Tema> teme, int deadline)
    {
        //Iterable<Tema> iterable=repoTema.getAll();
        //ArrayList<Tema> teme=iterableToArrayList(iterable);
        Predicate<Tema> predicate=x->x.getDeadline()==deadline;
        Comparator<Tema> comparator=(x,y)->{return x.getDescriere().compareTo(y.getDescriere());};
        List<Tema> teme_fil=filterAndSorter(teme,predicate,comparator);
        return teme_fil;
    }

    public List<Tema> filterByDescriptionSortedByDeadline(List<Tema> teme, String description)
    {
        //Iterable<Tema> iterable=repoTema.getAll();
        //ArrayList<Tema> teme=iterableToArrayList(iterable);
        Predicate<Tema> predicate=x->x.getDescriere().toLowerCase().contains(description.toLowerCase());
        Comparator<Tema> comparator=(x,y)->{if(x.getDeadline()<y.getDeadline())return -1;
                                            if(x.getDeadline()>y.getDeadline())return 1;
                                            return 0;};
        List<Tema> teme_fil=filterAndSorter(teme,predicate,comparator);
        return teme_fil;
    }

    public List<Tema> filterByExpiredDeadline(List<Tema> teme)
    {
        //Iterable<Tema> iterable=repoTema.getAll();
        //ArrayList<Tema> teme=iterableToArrayList(iterable);
        Predicate<Tema> predicate=x->x.getDeadline()<sapt_curenta;
        Comparator<Tema> comparator=(x,y)->{ if(x.getDeadline()<y.getDeadline())return -1;
                                             if(x.getDeadline()>y.getDeadline())return 1;
                                             return 0;};
        List<Tema> teme_fil=filterAndSorter(teme,predicate,comparator);
        return  teme_fil;
    }

    public List<Tema> filterByAvailableDeadline(List<Tema> teme)
    {
        //Iterable<Tema> iterable=repoTema.getAll();
        //ArrayList<Tema> teme=iterableToArrayList(iterable);
        Predicate<Tema> predicate=x->x.getDeadline()>=sapt_curenta;
        Comparator<Tema> comparator=(x,y)->{ if(x.getDeadline()<y.getDeadline())return -1;
            if(x.getDeadline()>y.getDeadline())return 1;
            return 0;};
        List<Tema> teme_fil=filterAndSorter(teme,predicate,comparator);
        return  teme_fil;
    }
    public List<Tema> filterByDeadlineAll(boolean expired,String description)
    {
        Iterable<Tema> iterable=repoTema.getAll();
        ArrayList<Tema> teme=iterableToArrayList(iterable);
        Predicate<Tema> predicateEx=x->x.getDeadline()==x.getDeadline();
        if(expired==true)
        {
            predicateEx=x->x.getDeadline()<sapt_curenta;
        }
        else
        {
            predicateEx=x->x.getDeadline()>sapt_curenta;
        }
        Predicate<Tema> predicateDesc=x->x.getDescriere().contains(description);
        Comparator<Tema> comparator=(x,y)->{ if(x.getDeadline()<y.getDeadline())return -1;
            if(x.getDeadline()>y.getDeadline())return 1;
            return 0;};
        List<Tema> teme_fil=filterAndSorter(teme,predicateEx.and(predicateDesc),comparator);
        return  teme_fil;
    }

    public List<Nota> filterByStudentNameSortedByGrade(String student_name)
    {
        Iterable<Nota> iterable=repoNota.getAll();
        ArrayList<Nota> note=iterableToArrayList(iterable);
        Predicate<Nota> predicate=x->x.getStudent().getNume().toLowerCase().contains(student_name.toLowerCase());
        Comparator<Nota> comparator=(x,y)->{if(x.getValoare()<y.getValoare())return -1;
                                           if(x.getValoare()>y.getValoare())return 1;
                                           return 0;};
        List<Nota> teme_fil=filterAndSorter(note,predicate,comparator);
        return teme_fil;
    }
    public List<Nota> filterByGradeGreaterthan(int valoare)
    {
        Iterable<Nota> iterable=repoNota.getAll();
        ArrayList<Nota> note=iterableToArrayList(iterable);
        Predicate<Nota> predicate=x->x.getValoare()>valoare;
        Comparator<Nota> comparator=(x,y)->{return (x.getStudent().getNume()).compareTo(y.getStudent().getNume());};
        List<Nota> teme_fil=filterAndSorter(note,predicate,comparator);
        return teme_fil;
    }
    public List<Nota> filterByHomeworkSortedByGrade(String description)
    {
        Iterable<Nota> iterable=repoNota.getAll();
        ArrayList<Nota> note=iterableToArrayList(iterable);
        Predicate<Nota> predicate=x->x.getTema().getDescriere().contains(description);
        Comparator<Nota> comparator=(x,y)->{if(x.getValoare()<y.getValoare())return -1;
                                            if(x.getValoare()>y.getValoare())return 1;
                                            return 0;};
        List<Nota> teme_fil=filterAndSorter(note,predicate,comparator);
        return teme_fil;
    }

    public List<DTOgrades> getAllDtoGrades()
    {
        List<DTOgrades> dtogrades=new ArrayList<>();
        for (Student student:repoStudent.getAll())
        {
            HashMap<Integer,Integer> grades=new HashMap<Integer, Integer>();
            for (Nota nota:repoNota.getAll())
            {
                if(nota.getStudent().getID()==student.getID())
                {
                    grades.put(nota.getTema().getID(),nota.getValoare());
                }
            }
            DTOgrades dtograde=new DTOgrades(student,grades);
            dtogrades.add(dtograde);
        }
        return  dtogrades;
    }


    public Student getStudentById(Integer id)
    {
        for (Student st:getAllStudents())
        {
            if(st.getID()==id)
                return st;
        }
        throw new ServiceException("Student doesn't exist");
    }

    public ArrayList<Integer> getAllGroups()
    {
        ArrayList<Integer> groups=new ArrayList<>();
        for (Student student:repoStudent.getAll())
        {
                if(!groups.contains(student.getGrupa()))
                {
                   groups.add(student.getGrupa());
                }
        }
        return groups;
    }

    public float getAvgPerGroup(int group)
    {
        int nrStudents=0;
        int sumaNote=0;
        for (Student student:repoStudent.getAll())
        {
            if(student.getGrupa()==group)
            {
                nrStudents=nrStudents+1;
            }
        }
        for (Nota nota:repoNota.getAll())
        {
            if(nota.getStudent().getGrupa()==group)
            {
                sumaNote=sumaNote+nota.getValoare();
            }
        }
        return sumaNote/nrStudents;
    }
}

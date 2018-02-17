package service;

import domain.*;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import repository.*;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Service extends sablonObserver.Observable {
    private Repository<Student,Integer> repoStudents;
    private Repository<TemaLaborator,Integer> repoTemeLab;
    private NotaRepository repoNote;
    private Repository<Intarziere,Integer> repoIntarzieri;
    private RepositoryFisierLog repositoryFisierLog=new RepositoryFisierLog();
    private static Integer saptamana_curenta;

    /******************************************STATISTICI********************************************************************/

    private Map<Integer,Double> ponderi=new HashMap<>();

    public void setPonderi(Map<Integer, Double> ponderi) {
        this.ponderi = ponderi;
    }

    public List<StudentMedia> getStstisticaNoteleTuturorStudentilor(Map<Integer,Double> ponderi, List<Student> lista){
        setPonderi(ponderi);
        List<StudentMedia> listStatistica=new ArrayList<>();
        lista.forEach(student -> {
            StudentMedia studentMedia=new StudentMedia(student.getId(),student.getNume(),student.getGrupa(),student.getCadru_didactic_indrumator_de_laborator(),student.getEmail(),calculeazaMedie(student.getId()));
            listStatistica.add(studentMedia);
        });

        List<StudentMedia> sortedList;

        sortedList=listStatistica.stream().sorted((x,y)->y.getMedia().compareTo(x.getMedia())).collect(Collectors.toList());

        return sortedList;
    }

    private Double calculeazaMedie(Integer id) {
        Double media= Double.valueOf(0);
        List<Nota> listNote=this.getNoteByIdStudent(id);
        for (Map.Entry<Integer,Double> pair:ponderi.entrySet()){
            Float val=getValoareNotaByNrTema(pair.getKey(),listNote);
            if (val!=null)
                media=media+val*pair.getValue();
            else media=media+1*pair.getValue();
        }
        return media/100;
    }

    private Float getValoareNotaByNrTema(Integer nrtema,List<Nota> notaList){
        for (Nota nota:notaList){
            if (nota.getNrTema().equals(nrtema))
                return nota.getValoare();
        }
        return null;
    }

    public List<StudentMedia> getStatisticaStudentiiCarePotIntraInExamen(Map<Integer,Double> ponderi,List<Student> listaStudenti,Integer notaFiltru){
        List<StudentMedia> studentMediaList;
        studentMediaList=getStstisticaNoteleTuturorStudentilor(ponderi,listaStudenti).stream().filter((x)->x.getMedia()>=notaFiltru).sorted((x,y)->y.getMedia().compareTo(x.getMedia())).collect(Collectors.toList());
        return studentMediaList;
    }

    /**
     * Cele mai grele teme la care au fost penalizati studentii din grupele sau de la profesori dati
     * @param listaStudenti - anumiti studenti din anumite grupe, de la anumiti profesori
     * @return List<TemaPenalizri></TemaPenalizri>
     */
    public List<TemaPenalizari> getStatisticaCeleMaiGreleTeme(List<Student> listaStudenti){
        List<TemaPenalizari> penalizariList=new ArrayList<>();

        final List<Intarziere> intarziereList=getIntarzieriForStudenti(listaStudenti);

        List<TemaLaborator> temaLaboratorList = getTemaLaboratorListForListIntarzieri(intarziereList);

        for (TemaLaborator temaLaborator:temaLaboratorList){
            Integer nrStudenti=0;
            for (Intarziere intarziere:intarziereList){
                if (intarziere.getNrTema().equals(temaLaborator.getId())){
                    nrStudenti=nrStudenti+1;
                }
            }
            TemaPenalizari temaPenalizari=new TemaPenalizari(temaLaborator.getId(),temaLaborator.getCerinta(),temaLaborator.getDeadline(),nrStudenti);
            penalizariList.add(temaPenalizari);
        }

        return penalizariList.stream().sorted((x,y)->y.getNrStudentiPenalizati().compareTo(x.getNrStudentiPenalizati())).collect(Collectors.toList());
    }

    private List<TemaLaborator> getTemaLaboratorListForListIntarzieri(List<Intarziere> intarziereList) {
        List<TemaLaborator> temaLaboratorList=new ArrayList<>();
        intarziereList.forEach((Intarziere intarziere)->{
            try {
                TemaLaborator temaLaborator=repoTemeLab.findOne(intarziere.getNrTema()).get();
                if (!temaLaboratorList.contains(temaLaborator)){
                    temaLaboratorList.add(temaLaborator);
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        });
        return temaLaboratorList;
    }

    private List<Intarziere> getIntarzieriForStudenti(List<Student> listaStudenti) {
        List<Intarziere> list=new ArrayList<>();
        listaStudenti.forEach((Student student)->{
            repoIntarzieri.findAll().forEach((Intarziere intarziere)->{
                if (student.getId().equals(intarziere.getIdStudent()))
                    list.add(intarziere);
            });
        });
        return list;
    }

    /**
     * Studentii care au predat la timp toate temele dintre studentii aflati in studentList
     * @param studentList List<Student></Student>
     * @return List<Student></student>
     */
    public List<Student> getStatisticaStudentiiCareAuPredatLaTimpToateTemele(List<Student> studentList){
        Integer nrTemeDePredat=Integer.valueOf((int) getSizeTemeLab());

        List<Student> listStatistica=new ArrayList<>();

        for (Student student:studentList) {
            List<Intarziere> intarziereList=getIntarzieriForStudent(student.getId());
            if (intarziereList.size()==nrTemeDePredat){
                //a predat toate temele
                //verificam daca le-a predat la timp

                Boolean ok=true;
                for (Intarziere intarziere:intarziereList){
                    try{
                        TemaLaborator temaLaborator=repoTemeLab.findOne(intarziere.getNrTema()).get();
                        if (intarziere.getSaptamana_predarii()>temaLaborator.getDeadline()){
                            ok=false;
                        }
                    }
                    catch (EntityNotFoundException e){}
                }
                if (ok){
                    //nu are intarzieri
                    listStatistica.add(student);
                }
            }
        }

        return listStatistica.stream().sorted((x,y)->x.getNume().compareTo(y.getNume())).collect(Collectors.toList());
    }

    /**
     *
     * @param studentList
     * @param nrTemeMinNepredate
     * @param intarzieriMinim
     * @return
     */
    public List<StudentiIntarziati> getStatisticaStudentiIntarziatiCuTemeNepredate(List<Student> studentList,Integer nrTemeMinNepredate,Integer intarzieriMinim){
        List<StudentiIntarziati> listStatistica=new ArrayList<>();
        //toate teme ce trebuie predate pana in saptamana curenta inclusiv
        Integer nrTemeDePredat=filteredTemeByDeadlineDepasit().size()+filteredTemeByDeadlineThisWeek().size();

        List<TemaLaborator> temeDePredat=filteredTemeByDeadlineDepasit();
        temeDePredat.addAll(temeDePredat.size(),filteredTemeByDeadlineThisWeek());

        List<Intarziere> intarziereList=getIntarzieriForStudenti(studentList);

        for(Student student:studentList){
            List<Intarziere> listIntarziereSt=getIntarzieriForStudent(student.getId());
            Integer nrTemePredate=getNrTemePredate(temeDePredat,listIntarziereSt,student.getId());
            Integer intarzieri=getNrIntarzieriForStudent(temeDePredat,listIntarziereSt);

            StudentiIntarziati studentiIntarziati=new StudentiIntarziati(student.getId(),student.getNume(),student.getGrupa(),student.getCadru_didactic_indrumator_de_laborator(),student.getEmail(),nrTemeDePredat-nrTemePredate,intarzieri);
            listStatistica.add(studentiIntarziati);

        }

        return listStatistica.stream().filter(x->{
             return (x.getNrTemeNepredate()<=nrTemeMinNepredate && x.getNrIntarzieri()<=intarzieriMinim);
        }).sorted(
                (x,y)->{
                    if (x.getNrTemeNepredate().equals(y.getNrTemeNepredate())){
                        return x.getNrIntarzieri().compareTo(y.getNrIntarzieri());
                    }
                    else return x.getNrTemeNepredate().compareTo(y.getNrTemeNepredate());
                }
        ).collect(Collectors.toList());
    }

    private Integer getNrIntarzieriForStudent(List<TemaLaborator> temeDePredat, List<Intarziere> listIntarziereSt) {
        Integer rezultat=0;

        for (Intarziere intarziere:listIntarziereSt){
            try {
                TemaLaborator tema=repoTemeLab.findOne(intarziere.getNrTema()).get();
                if (temeDePredat.contains(tema)){
                    if (intarziere.getSaptamana_predarii()>tema.getDeadline()){
                        rezultat=rezultat+intarziere.getSaptamana_predarii()-tema.getDeadline();
                    }
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }

        return rezultat;
    }

    private Integer getNrTemePredate(List<TemaLaborator> temeDePredat, List<Intarziere> listIntarziereSt, Integer id) {
        Integer rezultat=0;

        for (Intarziere intarziere:listIntarziereSt){
            try {
                if (temeDePredat.contains(repoTemeLab.findOne(intarziere.getNrTema()).get())){
                    rezultat=rezultat+1;
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }

        return rezultat;
    }

    private List<Intarziere> getIntarzieriForStudent(Integer id) {
        List<Intarziere> list=new ArrayList<>();
        for (Intarziere intarziere:repoIntarzieri.findAll()){
            if (intarziere.getIdStudent().equals(id))
                list.add(intarziere);
        }
        return list;
    }

    /***************************************************************************************************************************/

    public Service() {
    }

    public List<Student> getStudentiForProfesor(String numeProfesor){
        List<Student> list=new ArrayList<>();
        List<Student> studentList=getListStudenti();
        for (Student student:studentList){
            if (student.getCadru_didactic_indrumator_de_laborator().equals(numeProfesor))
                list.add(student);
        }
        return list;
    }

    public List<Nota> getNotePentruStudentiiUnuiProfesor(String numeProfesor){
        List<Nota> list=new ArrayList<>();
        List<Nota> notaList=getListNote();

        for (Nota nota:notaList){
            try{
                Optional<Student> student=repoStudents.findOne(nota.getIdStudent());
                if (student.isPresent()){
                    if (student.get().getCadru_didactic_indrumator_de_laborator().equals(numeProfesor)){
                        list.add(nota);
                    }
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Integer getSaptamana_curenta() {
        return saptamana_curenta;
    }

    /**
     * Filters and sorts a list using a predicate and a compartor
     * @param lista List<E> the list we want to filter and sort
     * @param predicate Predicate <E></>
     * @param comparator Comparator<E></E>
     * @param <E> a generic type
     * @return List<E> the filtered and sorted list
     */
    public <E> List<E> filterAndSorter(List<E> lista, Predicate<E> predicate,Comparator<E> comparator){
        List<E> filteredList=new ArrayList<E>();
        filteredList=lista.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
        return filteredList;
    }

    public Optional<Student> getStudentById(Integer id) throws EntityNotFoundException {
        return repoStudents.findOne(id);
    }

    /**
     * Returns a list of all students from repoStudents
     * @return List<Studenti> </Studenti>
     */
    public List<Student> getListStudenti(){
        List<Student> studenti=new ArrayList<Student>();
        repoStudents.findAll().forEach(student ->studenti.add(student));
        return studenti;
    }

    /**
     *  Va filtra studentii dupa o grupa data ca parametru
     *  si ii va returna in ordine alfabetica
     * @param grupa Integer
     * @return List<Student></>
     */
    public List<Student> filteredStudentsByGroup(Integer grupa){
        List<Student> studenti=getListStudenti();
        return filterAndSorter(studenti,(x)->x.getGrupa()==grupa,(x,y)->x.getNume().compareTo(y.getNume()));
    }

    public List<Student> filteredStudentsByName(String name){
        List<Student> students=getListStudenti();
        return filterAndSorter(students,(x)->x.getNume().contains(name),(x,y)->x.getId()-y.getId());
    }

    /**
     * Returns all students that have the string email in their email
     * @param email String
     * @return List<student></student>
     */
    public List<Student> filteredStudentsByEmail(String email){
        List<Student> students=getListStudenti();
        return filterAndSorter(students,(x)->x.getEmail().contains(email),(x,y)->x.getId()-y.getId());
    }

    /**
     * Returns the students that passed the materia
     * @param grade Float
     * @return List<student></student>
     */
    public List<Student> filteredStudentsByGrades(Float grade){
        List<Student> students=getListStudenti();
        return filterAndSorter(students,(x)->{
            List<Nota> note=repoNote.getNoteByIdStudent(x.getId());
            Float suma=new Float(0);
            for (Nota n:note){
                suma=suma+n.getValoare();
            }
            return suma/note.size()>grade;
        },(x,y)->x.getNume().compareTo(y.getNume()));
    }

    public List<Nota> getNoteByIdStudent(Integer id){
        return repoNote.getNoteByIdStudent(id);
    }

    /**
     * Returns all temeLaborator from repoTemeLaborator
     * @return List<TemaLaborator></TemaLaborator>
     */
    public List<TemaLaborator> getListTemeLaborator(){
        List<TemaLaborator> teme=new ArrayList<TemaLaborator>();
        for (TemaLaborator tema:repoTemeLab.findAll()){
            teme.add(tema);
        }
        return teme;
    }

    /**
     * Returns a list of all grades from repoNote
     * @return list<Note></Note>
     */
    public List<Nota> getListNote(){
        List<Nota> note=new ArrayList<Nota>();
        for (Nota nota:repoNote.findAll()){
            note.add(nota);
        }
        return note;
    }

    /**
     * Teme de laborator care au deadlineul in aceasta sapatamana
     * ordonate dupa id in ordine crescatoare
     * @return List<TemeLaborator></>
     */
    public List<TemaLaborator> filteredTemeByDeadlineThisWeek(){
        List<TemaLaborator> teme=getListTemeLaborator();
        return filterAndSorter(teme,(x)->x.getDeadline()==saptamana_curenta,(x,y)->x.getId()-y.getId());
    }

    /**
     * Teme de laborator care au deadlineul depasit ordonate dupa id crescator
     * @return List<TemeLaborator></TemeLaborator>
     */
    public List<TemaLaborator> filteredTemeByDeadlineDepasit(){
        List<TemaLaborator> teme=getListTemeLaborator();
        return filterAndSorter(teme,(x)->x.getDeadline()<saptamana_curenta,(x,y)->x.getId()-y.getId());
    }

    /**
     * Temele de laborator care contin in cerinta un anumit cuvant cheie, ordonate in ordine crecatoare
     * dupa Deadline
     * @param cuvantCheie String
     * @return List<TemeLaborator></TemeLaborator>
     */
    public List<TemaLaborator> filteredTemeByCerinta(String cuvantCheie){
        List<TemaLaborator> teme=getListTemeLaborator();
        return filterAndSorter(teme,(x)->x.getCerinta().contains(cuvantCheie),(x,y)->x.getDeadline()-y.getDeadline());
    }

    /**
     * Filtreaza notele care sunt mai mari sau egale decat o valoare data
     * Notele sunt ordonate in ordine dupa idStudent si dupa idTema
     * @param valoare Float
     * @return List<Nota></Nota>
     */
    public List<Nota> filteredNoteByValoare(Float valoare){
        List<Nota> note=getListNote();
        return filterAndSorter(note,(x)->x.getValoare()>=valoare,(x,y)->{
            if (x.getIdStudent().equals(y.getIdStudent()))
                return x.getNrTema()-y.getNrTema();
            else return x.getIdStudent()-y.getIdStudent();
        });
    }

    /**
     * Filtreaza notele pe care le are un anumit student si le ordoneaza dupa valoare in ordine descrescatoare
     * @param idStudent Integer
     * @return List<Nota></Nota>
     */
    public List<Nota> filteredNotebyIdStudent(Integer idStudent){
        List<Nota> note=getListNote();
        return filterAndSorter(note,(x)->x.getIdStudent().equals(idStudent),(x,y)->y.getValoare().compareTo(x.getValoare()));
    }

    /**
     * Filtreaza notele pe care le are o anumita tema si le ordoneaza dupa valoare in ordine descrescatoare
     * @param nrTema Integer
     * @return List<Nota></Nota>
     */
    public List<Nota> filteredNoteByNrTema(Integer nrTema){
        List<Nota> note=getListNote();
        return filterAndSorter(note,(x)->x.getNrTema().equals(nrTema),(x,y)->y.getValoare().compareTo(x.getValoare()));
    }


    /**
     * Pentru studentii care nu au asignate note la temele de laborator la care a trecut deadlineul
     * li se va adauga in repositoryNote nota 1 cu saptamana predarii fiind 0
     * @throws EntityNotFoundException
     * @throws ValidationException
     */
    private void asignareNote() throws EntityNotFoundException, ValidationException {
        ArrayList<TemaLaborator> temaCuDeadlineDepasit=new ArrayList<TemaLaborator>();
        for (TemaLaborator tema:repoTemeLab.findAll()){
            if (tema.getDeadline()<saptamana_curenta)
                temaCuDeadlineDepasit.add(tema);
        }
        for (TemaLaborator tema:temaCuDeadlineDepasit){
            ArrayList<Student> studentiCuTeme=new ArrayList<Student>();
            for (Nota n:repoNote.findAll()){
                if (n.getNrTema().equals(tema.getId()))
                    studentiCuTeme.add(repoStudents.findOne(n.getIdStudent()).get());
            }
            ArrayList<Student> studentiFaraTeme=new ArrayList<Student>();
            for (Student st:repoStudents.findAll()){
                if (!studentiCuTeme.contains(st))
                    studentiFaraTeme.add(st);
            }
            //la toti studentii fara tema le asignam nota 1
            for (Student st:studentiFaraTeme){
                Nota nota=new Nota(generareIdNota(),st.getIdStudent(),tema.getNr_tema_de_laborator(),new Float (1));
                DetaliiNota detaliiNota=new DetaliiNota(nota,tema.getDeadline(),0,true,false);
                repoNote.save(nota,detaliiNota);
            }
        }
    }

    /**
     * Returns a random number suitable for idNota
     * @return Int >0 an random number that is unique for repoNote
     */
    private int generareIdNota(){
        Random r=new Random();
        Integer rand=r.nextInt();
        Boolean ok=true;
        while ((rand<=0) || (ok==true)){
            rand=r.nextInt();
            ok=false;
            for (Nota n:repoNote.findAll()){
                if (n.getId()==rand){
                    ok=true;
                }
            }
        }

        return rand;
    }

    private Integer generareIdIntarziere(){
        Random r=new Random();
        Integer rand=r.nextInt();
        Boolean ok=true;
        while ((rand<=0) || (ok==true)){
            rand=r.nextInt();
            ok=false;
            for (Intarziere n:repoIntarzieri.findAll()){
                if (n.getId()==rand){
                    ok=true;
                }
            }
        }

        return rand;
    }

    /**
     * Constructor
     * @param repoStudents repository.StudentRepository
     * @param repoTemeLab repository.TemeLabRepository
     * @param repoNote Repository<Nota,Integer></Nota,Integer>
     */
    public Service(Repository<Student,Integer> repoStudents, Repository<TemaLaborator,Integer> repoTemeLab,NotaRepository repoNote,Repository<Intarziere,Integer> repoIntarzieri,Integer sapt) {
        this.repoStudents = repoStudents;
        this.repoTemeLab = repoTemeLab;
        this.repoNote=repoNote;
        this.repoIntarzieri=repoIntarzieri;
        this.saptamana_curenta=sapt;
    }


    /**
     * Adds the student st in the repo if it is valid and if doesn't already exists a student with the same id already saved
     * @param st domain.Student
     * @return domain.Student (the added student)
     * @return null if the student st has the same id as a student already saved in repo
     * @throws ValidationException if st is not valid
     */
    public Optional<Student> addStudent (Student st)throws ValidationException{
        Optional<Student> aux=repoStudents.save(st);
        if (!aux.isPresent()){
            notifyObservers();
        }
        return aux;
    }

    /**
     * Puts in a list all the grades a student with the id given as a parameter has in repo
     * @param id Integer
     * @return ArrayList<Nota></>
     */
    private ArrayList<Nota> getNoteForStudent(Integer id){
        List<Nota> list=new ArrayList<Nota>();
        for (Nota n: findAllNote()){
            if (n.getIdStudent()==id){
                list.add(n);
            }
        }
        return (ArrayList<Nota>) list;
    }

    /**
     *Deletes a student from repository
     * @param id integer
     * @return domain.Student (the deleted student)
     * @return null if you can not delete the student because he/she has assigned grades
     * @throws EntityNotFoundException if the student with the id given does not exists in repo
     */
    public Optional<Student> deleteStudent(Integer id) throws EntityNotFoundException{
        List<Nota> noteStudent=getNoteForStudent(id);
        if (noteStudent.size()==0) {
            Optional<Student> aux=repoStudents.delete(id);
            if (aux.isPresent()){
                notifyObservers();
            }
            return aux;
        }
        else return Optional.ofNullable(null);
    }

    /**
     *
     * @param id integer
     * @return domain.Student with the id given as a parameter
     * @throws EntityNotFoundException - if there isn't a student with the id in repo
     */
    public Optional<Student> getStudent(Integer id) throws EntityNotFoundException{
        return repoStudents.findOne(id);
    }

    /**
     * For the use of foreach
     * @return Iterable<domain.Student> </domain.Student>
     */
    public Iterable<Student> findAllStudents(){
        return repoStudents.findAll();
    }

    /**
     *
     * @param id integer
     * @param numeNou String
     * @param emailNou String
     * @param cadruNou String
     * @param grupaNoua Integer
     * @throws ValidationException if newStudent is not valid
     */
    public void  updateStudent(Integer id,String numeNou, String emailNou, String cadruNou,Integer grupaNoua)throws ValidationException, EntityNotFoundException{
        Student stNew=new Student(id,numeNou,grupaNoua,emailNou,cadruNou);
        repoStudents.update(stNew);
        notifyObservers();
    }

    /**
     *Saves temaLab in repo
     * @param temaLab domain.TemaLaborator
     * @return domain.TemaLaborator if there is another domain.TemaLaborator with the same id already saved in repo
     * @return null if the entity does not exists
     * @throws ValidationException if temaLab is not valid
     */
    public Optional<TemaLaborator> addTemaLab(TemaLaborator temaLab)throws ValidationException{
        Optional<TemaLaborator> aux=repoTemeLab.save(temaLab);
        if (!aux.isPresent())
            notifyObservers();
        return aux;
    }

//    /**
//     *
//     * @param id integer
//     * @return domain.TemaLaborator returns the deleted temaLab
//     * @throws exceptii.EntityNotFoundException - if there isn't a Temalaborator object with the id given
//     */
//    public domain.TemaLaborator deleteTemaLab(Integer id) throws exceptii.EntityNotFoundException{
//        return repoTemeLab.delete(id);
//    }

    /**
     *Updates a temaLaborator object from the repo
     * @param id integer
     * @param newDeadline integer
     * @throws EntityNotFoundException the domain.TemaLaborator you want to update does not exists in repo
     * @throws ValidationException if the deadline is not valid
     */
    public void updateTemaLabTermenPredare(Integer id,Integer newDeadline) throws  EntityNotFoundException, ValidationException{
        TemaLaborator newTema = new TemaLaborator(id, repoTemeLab.findOne(id).get().getCerinta(), newDeadline);
        Optional<TemaLaborator> old=repoTemeLab.findOne(newTema.getId());
        if (!old.isPresent()) throw new EntityNotFoundException("Nu exista tema de laborator dorita");
        if ((saptamana_curenta<old.get().getDeadline())&&(newTema.getDeadline()>old.get().getDeadline())){
            repoTemeLab.update(newTema);
            notifyObservers();
        }

    }

    /**
     * For the use of forech
     * @return Iterable<TemeLaborator></TemeLaborator>
     */
    public Iterable<TemaLaborator>findAllTemeLab(){
        return repoTemeLab.findAll();
    }

    /**
     *
     * @param id integer
     * @return domain.TemaLaborator with the id given as a paramter
     * @throws EntityNotFoundException if there is not found a TemeLaborator with the id given
     */
    public Optional<TemaLaborator> getTemaLab(Integer id) throws EntityNotFoundException {
        return repoTemeLab.findOne(id);
    }

    /**
     * Returns the number of students saved in repo
     * @return long integer
     */
    public long getSizeSt(){
        return repoStudents.size();
    }

    /**
     * Returns the number of temeLab saved in repo
     * @return long integer
     */
    public long getSizeTemeLab(){
        return  repoTemeLab.size();
    }

    /**
     * Returns the numeber of grades saved in repo
     * @return long integer
     */
    public long getSizeNote(){return repoNote.size();}

    /**
     * For the use of foreach
     * @return Iterable<Nota></Nota>
     */
    public Iterable<Nota> findAllNote(){return repoNote.findAll();}

    /**
     * Applies the reductions according to the lab rules
     * @param nota Nota
     * @param saptamana_predarii Integer
     * @return Nota (the nota with the reductions given by the lab rules)
     * @throws EntityNotFoundException if for the nota given the idStudent or nrTema does have associated in repo a Student and a TemaLab
     */
    private Nota aplicarePenalizari(Nota nota,Integer saptamana_predarii)throws EntityNotFoundException{
        Integer dif=saptamana_predarii-repoTemeLab.findOne(nota.getNrTema()).get().getDeadline();
        if ((dif==1) ||(dif==2)){
            Float oldVal=nota.getValoare();
            nota.setValoare(oldVal-dif*2);
        }else if (dif>3){
            nota.setValoare((float)1);
        }
        return nota;
    }

    /**
     *Adds a grade in repo
     * @param idStudent int
     * @param nrTema int
     * @param val float
     * @return Nota if nota was not saved in repo ( ex: has the same id as other grades)
     * @return null if nota with the modifications applied was saved
     * @throws EntityNotFoundException if there is not a student or a temaLab with the id as the same as the one contained
     * by the object Nota
     * @throws ValidationException if nota is not valid
     */
    public Optional<Nota> addNota(int idStudent,int nrTema,float val) throws EntityNotFoundException, ValidationException{
        Nota nota=new Nota(generareIdNota(),idStudent,nrTema,val);
        if ((repoTemeLab.findOne(nota.getNrTema())!=null) && (repoStudents.findOne(nota.getIdStudent())!=null)){

            Boolean intarzieri=false,greseli=false;
            if (nota.getValoare()<10) greseli=true;
            if (saptamana_curenta>repoTemeLab.findOne(nota.getNrTema()).get().getDeadline()) intarzieri=true;

            Nota n=aplicarePenalizari(nota,saptamana_curenta);
            DetaliiNota detaliiNota=addAdaugareNota(n,saptamana_curenta,intarzieri,greseli);
            Optional<Nota> aux=repoNote.save(n,detaliiNota);
            if (!aux.isPresent()) {
                repoIntarzieri.save(new Intarziere(generareIdIntarziere(),n.getId(),n.getIdStudent(),n.getNrTema(),detaliiNota.getSaptamana_predarii()));
                notifyObservers();
            }
            return aux;
        }
        else return Optional.ofNullable(nota);
    }

    /**
     *Adds to istoric the grade added
     * @param nota Nota
     * @param saptamana_predarii Integer
     * @param intarzieri Boolean
     * @param greseli Boolean
     * @throws EntityNotFoundException if there isn't a temaLab with the same id as the one from nota
     */
    private DetaliiNota addAdaugareNota(Nota nota,Integer saptamana_predarii,Boolean intarzieri, Boolean greseli) throws EntityNotFoundException{
        Integer deadline=repoTemeLab.findOne(nota.getNrTema()).get().getDeadline();
        DetaliiNota detNota=new DetaliiNota(nota,deadline,saptamana_predarii,intarzieri,greseli);
        return detNota;
    }

    /**
     * Adds to the istoric the grade updated
     * @param nota Nota
     * @param saptamana_predarii Integer
     * @param intarzieri Boolean
     * @param greseli Boolean
     * @throws EntityNotFoundException if there isn't a temaLab with the same id as the one from nota
     */
    private DetaliiNota updateNotaIstoric(Nota nota,Integer saptamana_predarii,Boolean intarzieri,Boolean greseli) throws EntityNotFoundException{
        Integer deadline=repoTemeLab.findOne(nota.getNrTema()).get().getDeadline();
        DetaliiNota detaliiNota=new DetaliiNota(nota,deadline,saptamana_predarii,intarzieri,greseli);
        return detaliiNota;
    }

    /**
     *
     * @param nota Nota
     * @return null if the update is made
     * @throws ValidationException if nota is not valid
     * @throws EntityNotFoundException if the idStudent or nrTema contained by nota belong to an entity that does not exists in repo
     */
    public Nota modificareNota(Nota nota) throws ValidationException,EntityNotFoundException{
        if ((repoTemeLab.findOne(nota.getNrTema())!=null)&&(repoStudents.findOne(nota.getIdStudent())!=null)){
            Boolean intarzieri=false,greseli=false;
            if (nota.getValoare()<10) greseli=true;
            if (saptamana_curenta>repoTemeLab.findOne(nota.getNrTema()).get().getDeadline()) intarzieri=true;

            Nota n=aplicarePenalizari(nota,saptamana_curenta);
            if (n.getValoare()>repoNote.findOne(nota.getId()).get().getValoare()) {
                DetaliiNota detaliiNota=updateNotaIstoric(nota,saptamana_curenta,intarzieri,greseli);
                repoNote.update(n,detaliiNota);
                notifyObservers();
                return null;
            }
            else return n;
        }
        else return nota;
    }

    public List<TemaLaborator> getTemeNepredateForEmail(String email){
        List<TemaLaborator> list=getListTemeLaborator();
        Student student=getStudentByEmail(email);
        if (student!=null) {
            List<TemaLaborator> listPredate = getListTemePredate(student.getId());
            List<TemaLaborator> result=new ArrayList<>();
            for(TemaLaborator tema:list){
                if (!listPredate.contains(tema)){
                    result.add(tema);
                }
            }
            return result;
        }
        return new ArrayList<>();
    }

    private List<TemaLaborator> getListTemePredate(Integer id) {
        List<Nota> listNota=getNoteByIdStudent(id);
        List<TemaLaborator> rezultat=new ArrayList<>();
        for (Nota nota:listNota){
            try {
                TemaLaborator tema=repoTemeLab.findOne(nota.getNrTema()).get();
                if (!rezultat.contains(tema)){
                    rezultat.add(tema);
                }
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
        }
        return rezultat;
    }

    public Student getStudentByEmail(String email) {
        List<Student> list=getListStudenti();
        for(Student st:list){
            if (st.getEmail().equals(email)){
                return st;
            }
        }
        return null;
    }

    public List<DetaliiLog> getDetaliiLogForStudent(Integer idStudent) throws FileNotFoundException {
        return repositoryFisierLog.incarcaFisierLog(idStudent);
    }

    /**
     * Studentii care au media intre valoare si valoare-1
     * @param valoare
     * @return
     */
    public List<StudentMedia> getStatisticaCuNotePeCategorii(Integer valoare) {
        List<StudentMedia> list=new ArrayList<>();
        List<StudentMedia> listToata=getStstisticaNoteleTuturorStudentilor(ponderi,getListStudenti());
        for(StudentMedia student:listToata){
            if (student.getMedia()<=valoare&& student.getMedia()>valoare-1)
                list.add(student);
        }
        return list;
    }
}

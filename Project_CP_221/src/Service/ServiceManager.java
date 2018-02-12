package Service;

import Domain.Grade;
import Domain.LabHomework;
import Domain.LabHomeworkSuper;
import Domain.Student;
import Repository.*;
import Utils.MailSender;
import Utils.Observable;
import Utils.Reports;
import Validator.ValidatorException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Comparator.*;

import static Utils.Reports.Cele_mai_dificile_teme;
import static Utils.Reports.Media_pentru_fiecare_student;

public class ServiceManager {
    private long currentWeek;
    private IRepository<Integer, Student> stRepo;
    private IRepository<Integer, LabHomework> labRepo;
    private IRepository<Pair<Integer,Integer>, Grade> gradeRepo;
    private static PrintWriter pw;
    private static StudentComparator stComp;
    private static HomeworkComparator hwComp;
    private static GradeComparator grComp;
    private static MailSender mailSender;
    public long getCurrentWeek() {
        return currentWeek;
    }

    private void verifyDeadline() throws ValidatorException {
        for (Student s:stRepo.getAll())
        {
            for(LabHomework l:labRepo.getAll()) {
                if (gradeRepo.findOne(new Pair<>(s.getId(),l.getId())) == null && l.getDeadline()<currentWeek-2)
                {
                    Grade g=new Grade(s.getId(),l.getId(),1);
                    gradeRepo.save(g);
                    stRepo.findOne(s.getId()).setHomeworkOnTime((byte)0);
                    this.exportToFile(g,(int)currentWeek,"Tema nepredata","Adaugare nota");
                }
            }
        }
    }

    private void init_currentWeek()
    {
        LocalDate startingDate=LocalDate.of(2017, Month.OCTOBER,3);
        LocalDate endDate=LocalDate.now();
        currentWeek= ChronoUnit.WEEKS.between(startingDate,endDate)-1;
    }
    public ServiceManager(IRepository<Integer, Student> stRepo, IRepository<Integer, LabHomework> labRepo,IRepository<Pair<Integer,Integer>, Grade> gradeRepo) {
        this.stRepo = stRepo;
        this.labRepo = labRepo;
        this.gradeRepo=gradeRepo;
        this.stComp=new StudentComparator();
        this.hwComp=new HomeworkComparator();
        this.grComp=new GradeComparator();
        this.mailSender=new MailSender();
        init_currentWeek();
        //setNameForGrades();
        /*try
        {
            verifyDeadline();
        }
        catch (ValidatorException exc)
        {
            System.err.println("Nu-i cazu'");
        }
        */
    }

    //public ServiceManager(){}

    public void setNameForGrades()
    {
        for (Grade g:gradeRepo.getAll()
             ) {
            g.setName(stRepo.findOne(g.getIdStudent()).getName());
        }
    }

    public void setGradeRepo(IRepository<Pair<Integer, Integer>, Grade> gradeRepo) {
        this.gradeRepo = gradeRepo;
    }

    public void setLabRepo(IRepository<Integer, LabHomework> labRepo) {
        this.labRepo = labRepo;
    }

    public void setStRepo(IRepository<Integer, Student> stRepo) {
        this.stRepo = stRepo;
    }

    public void addStudent(int id,String name,String email,String group,String professor) throws ValidatorException {
        Student st=new Student(id,name,email,group,professor);
        stRepo.save(st);
    }

    public void deleteStudent(int id)
    {
        stRepo.delete(id);
    }

    public void updateStudent(int id,String name,String email,String group,String professor) throws ValidatorException {
        Student oldSt=findOneStudent(id);
        if(name.compareTo("")==0)
            name=oldSt.getName();
        if(email.compareTo("")==0)
            email=oldSt.getEmail();
        if(group.compareTo("")==0)
            group=oldSt.getGroup();
        if(professor.compareTo("")==0)
            professor=oldSt.getProfessor();
        Student st=new Student(id,name,email,group,professor);
        stRepo.update(id,st);
    }


    public void addHomework(int id, int deadline, String description) throws ValidatorException {
        if(deadline<currentWeek)
            throw new RepositoryException("Deadline must be greater or at least equal with current week!");
        LabHomework lh=new LabHomework(id,deadline,description);
        labRepo.save(lh);
        //notifyAllViaEmail("New homework for you at map!","");
    }

    public void deleteHomework(int id)
    {
        labRepo.delete(id);
    }

    public void updateHomework(int id,int deadline,String description) throws ValidatorException {
        /*if (deadline<=currentWeek)
            throw new ValidatorException("New deadline must be greater than the current week.");
        if(labRepo.findOne(id).getDeadline()<=currentWeek)
            throw new ValidatorException("The deadline for this homework cannot be changed");
        LabHomework lh=new LabHomework(id,deadline,description);*/
        labRepo.update(id,new LabHomework(id,deadline,description));
    }

    public void extendDeadline(int id,int deadline) throws ValidatorException {

        /*if (deadline<=currentWeek)
            throw new ValidatorException("New deadline must be greater than the current week.");
        if(labRepo.findOne(id).getDeadline()<=currentWeek)*/
        if((int)currentWeek>=labRepo.findOne(id).getDeadline())
            throw new ValidatorException("The deadline for this homework cannot be changed");
        if(deadline>14)
            throw new ValidatorException("Deadline must be less than 14.");
        if((int)currentWeek>=deadline)
            throw new ValidatorException("Deadline must be greater than the old one.");
        labRepo.update(id,new LabHomework(id,deadline,labRepo.findOne(id).getDescription()));
        notifyAllViaEmail("Deadline for homework number"+id+"was extended!","");
    }

    public void addGrade( int ids, int idh,int grade,int week,String obs,String type) throws ValidatorException {

        if(stRepo.findOne(ids)==null)
            throw new RepositoryException("There is no student with this id");
        if(labRepo.findOne(idh)==null)
            throw new RepositoryException("There is no homework with this id");
        if(week>currentWeek)
            throw new ValidatorException("Week cannont be greater than current week" + currentWeek);
        int deadline=labRepo.findOne(idh).getDeadline();
        if(week>deadline)
        {
            if(week-deadline<3)
                grade-=(week-deadline)*2;
            else grade=1;
            stRepo.findOne(ids).setHomeworkOnTime((byte)0);
            labRepo.findOne(idh).incrementDelayNumber();
            if(stRepo instanceof StudentXmlRepository)
                ((StudentXmlRepository) stRepo).updateHomeworkOnTime(ids);
            if(labRepo instanceof LabHomeworkXmlRepository)
                ((LabHomeworkXmlRepository)labRepo).updateDelays(idh);
        }
        Grade newGrade=new Grade(ids,idh,grade,new String(stRepo.findOne(ids).getName()));
        gradeRepo.save(newGrade);
        Student assignedSt=stRepo.findOne(ids);
        gradeRepo.findOne(new Pair<>(ids,idh)).setName(assignedSt.getName());
        assignedSt.setGrade(assignedSt.getGrade()+grade);
        assignedSt.setNumberOfGrades(assignedSt.getNumberOfGrades()+1);
        exportToFile(newGrade,(int)currentWeek,obs,type);
        notifyStudentViaEmail(ids,"-Map-Labs-News-","New grade succesufully added!Check out your grades' report.");
        //notifyAllViaEmail("subject","Text");
    }

    public void notifyStudentViaEmail(int id,String subject,String text)
    {
        String adressTo=stRepo.findOne(id).getEmail();
        String path="./HomeworkReports";
        String fileName=""+id+".txt";
        mailSender.send(adressTo,subject,text,path,fileName);
    }

    public void notifyAllViaEmail(String subject,String text)
    {
        String adresses="";
        byte count=0;
        for (Student s:stRepo.getAll()
             ) {
            if(count>50)
            {
                adresses=adresses.substring(0,adresses.length()-1);
                mailSender.send(adresses,subject,text);
                adresses="";
                count=0;
            }
            adresses+=s.getEmail()+",";
            count++;
            //mailSender.send(s.getEmail(),"Sorry for spam","It's just a spam,ignore it!");
        }
        //flush remainings
        mailSender.send(adresses,subject,text);
        //System.out.println("am flushuit");
    }

    public void deleteGrade(Pair<Integer,Integer> p)
    {
        gradeRepo.delete(p);
    }

    public void updateGrade(int ids,int idh,int grade,int week) throws ValidatorException {

        if(week>currentWeek)
            throw new ValidatorException("Week cannont be greater than current week" + currentWeek);
        Grade oldGrade=findOneGrade(new Pair<>(ids,idh));
        if(oldGrade==null)
            throw new RepositoryException("Inexistent Key:"+ids+","+idh);
        else {
            int deadline = labRepo.findOne(idh).getDeadline();
            if (week > deadline) {
                if (week - deadline < 3)
                    grade -= (week - deadline) * 2;
                else grade = 1;
            }
            //System.out.println("note:" + grade + " " + findOneGrade(id).getValue() + " ");
            if (grade > oldGrade.getValue())
            {
                Grade g=new Grade(ids,idh,grade);
                g.setName(oldGrade.getName());
                gradeRepo.update(oldGrade.getId(), g);
                //System.out.println("am facut update");
            }
        }
    }
    public Iterable<Student> getAllStudents()
    {
        return stRepo.getAll();
    }
    public int getSizeStudents()
    {
        return stRepo.size();
    }
    public Student findOneStudent(Integer id)
    {
        return stRepo.findOne(id);
    }
    public Iterable<LabHomework> getAllHomework()
    {
        return labRepo.getAll();
    }
    public int getSizeHomework()
    {
        return labRepo.size();
    }
    public LabHomework findOneHomework(Integer id)
    {
        return labRepo.findOne(id);
    }
    public Iterable<Grade> getAllGrades()
    {
        return gradeRepo.getAll();
    }
    public int getSizeGrades()
    {
        return gradeRepo.size();
    }
    public Grade findOneGrade(Pair<Integer,Integer> id)
    {
        return gradeRepo.findOne(id);
    }

    public void exportToFile(Grade g,int week,String obs,String type)
    {
        String fileName=""+g.getIdStudent()+".txt";
        String path="./HomeworkReports/"+fileName;
        try
        {
            pw=new PrintWriter(new FileWriter(path,true));
            pw.println(type+"Nr.Tema:"+g.getIdHomework()+";Nota:"+g.getValue()+";Deadline saptamana:"+findOneHomework(g.getIdHomework()).getDeadline()+
                    ";Saptamana predarii temei:"+week+";Eventuale observatii:"+obs);
            pw.close();
        }
        catch (IOException ex)
        {
            System.err.println("Error when trying to open the file "+fileName);
        }
    }

    //generic filter functions
    public <E> List<E> genericFilterAndSorter(List<E> list, Predicate<E> p,Comparator<E> c)
    {
        return list.stream().filter(p).sorted(c).collect(Collectors.toList());
    }

    public <E> List<E> genericFilterOnly(List<E> list,Predicate<E> p)
    {
        return list.stream().filter(p).collect(Collectors.toList());
    }

    //filters for student

    public List<Student> filterStudentsContaining(String s)
    {
        List<Student> st = new ArrayList<>();
        for (Student ss:getAllStudents()
                ) {
            st.add(ss);
        }
        return genericFilterAndSorter(st,x->(x.getName().toLowerCase().contains(s.toLowerCase())
                || x.getProfessor().toLowerCase().contains(s.toLowerCase()) || x.getProfessor().toLowerCase().contains(s.toLowerCase())
                || x.getEmail().toLowerCase().contains(s.toLowerCase()) || x.getGroup().toLowerCase().contains(s.toLowerCase())
                )
                ,(x,y)->stComp.compare(x,y));
    }

    public List<Student> filterStudentsStartingWith(String s)
    {
        List<Student> st = new ArrayList<>();
        for (Student ss:getAllStudents()
                ) {
            st.add(ss);
        }
        return genericFilterOnly(st,x->x.getName().startsWith(s));
    }
    public List<Student> filterStudentsHavingSameGroupAlphabetically(String group){
        List<Student> st = new ArrayList<>();
        for (Student ss:getAllStudents()
                ) {
            st.add(ss);
        }
        return genericFilterAndSorter(st,x->x.getGroup().compareTo(group)==0,(x,y)->stComp.compare(x,y));
    }
    public List<Student> filterStudentsHavingSameProfessorOrderedByName(String professor)
    {
        List<Student> st = new ArrayList<>();
        for (Student ss:getAllStudents()
                ) {
            st.add(ss);
        }
        return genericFilterAndSorter(st,x->x.getProfessor().compareTo(professor)==0,(x,y)->stComp.compare(x,y));
    }

    public List<LabHomework> filterAllUngradedHomeworkForStudent(int id)
    {
        List<LabHomework> all=FXCollections.observableArrayList();
        getAllHomework().forEach(
                x->{
                    if(findOneGrade(new Pair<>(id,x.getId()))==null)
                        all.add( x);
                }
        );
        return all;
    }

    public List<LabHomework> filterHomeworkStillInProgress(List<LabHomework> list)
    {
        return genericFilterAndSorter(list,x->(x.getDeadline()+2>=currentWeek),(x,y)->hwComp.compare(x,y));
    }

    public List<LabHomework> filterHomeworkDescriptionContainsGivenString(List<LabHomework> list,String s)
    {

        return genericFilterAndSorter(list,x->x.getDescription().contains(s),(x,y)->hwComp.compare(x,y));
    }

    public List<LabHomework> filterHomeworkHavingGivenDeadline(List<LabHomework>list,int deadline)
    {

        return genericFilterAndSorter(list,x->x.getDeadline()==deadline,(x,y)->hwComp.compare(x,y));
    }

    public List<Grade> filterGradesHavingSameValue(List<Grade> list,int value)
    {
        return genericFilterOnly(list,x->x.getValue()==value);
    }

    public List<Grade> filterGradesHavingSameStudent(List<Grade> list,int idS)
    {
        return genericFilterAndSorter(list,x->x.getIdStudent()==idS,(x,y)->grComp.compare(x,y));
    }
    public List<Grade> filterGradesHavingSameHomework(List<Grade> list,int idH)
    {
        return genericFilterAndSorter(list,x->x.getIdHomework()==idH,(x,y)->grComp.compare(x,y));
    }


    public ObservableList<Student> generateListForReport(Reports r)
    {
       List<Student> all=filterStudentsStartingWith("");
       ObservableList<Student> rez= FXCollections.observableArrayList();
       switch (r)
       {
            case Studenti_eligibili_pentru_examen:
                rez=FXCollections.observableArrayList(genericFilterOnly(all,x->(x.getGradeDouble()>=5)));
                break;
            case Studentii_care_au_predat_toate_temele:
                rez=FXCollections.observableArrayList(genericFilterOnly(all,x->(x.getHomeworkOnTime()==1 && x.getGrade()>0)));
                break;
            case Media_pentru_fiecare_student:
                rez=FXCollections.observableArrayList(all);
                break;
       }
        return rez;
    }

    public void exportReportToPdf(List<Student> rez,String path,String context)
    {
        Document doc = new Document();
        try {
                PdfWriter.getInstance(doc, new FileOutputStream(path));
                doc.open();
                doc.add(new Paragraph(context));
                rez.forEach(x->{
                    try {
                        doc.add(new Paragraph(x.getName()+" "+x.getGradeDouble()));
                    } catch (DocumentException e1) {
                        e1.printStackTrace();
                    }
                });
            } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        doc.close();
    }

    public void exportPDFTop3MostDifficultHomework(String path)
    {
        List<LabHomework> allhw=new ArrayList<>();
        labRepo.getAll().forEach(x->allhw.add(x));
        Stream<LabHomework> all=allhw.stream().sorted((x, y)->{
            if(x.getDelayNumber()>y.getDelayNumber())
                return -1;
            else if(x.getDelayNumber()<y.getDelayNumber())
                return 1;
            return 0;
        });
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();
            doc.add(new Paragraph("Top 3 most difficult homework according to our application:"));
            all.forEach(x -> {
                try {
                    String para=x.getDescription()+" "+x.getDelayNumber();
                    doc.add(new Paragraph(para));
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    /*
    public void exportPDFStudentsEligibleForExam(String path) throws Exception {
        String info="";
        List<Student> all=filterStudentsStartingWith("");
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();
            doc.add(new Paragraph("Students eligible for exam at MAP"));
            all.forEach(x -> {
                try {
                    if(x.getGrade()>=4) {
                        String para = x.getName() + " ";
                        para += (x.getGrade() / x.getNumberOfGrades());
                        doc.add(new Paragraph(para));
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });
            doc.close();
        }
        catch(Exception ex)
        {
            throw new Exception("Error when trying to convert the report into pdf.");
        }
    }


    public void exportPDFStudentsHavingHomeworkOnTime(String path) throws Exception {
        String info="";
        List<Student> all=filterStudentsStartingWith("");
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();
            doc.add(new Paragraph("Students having homework on time"));
            all.forEach(x -> {
                try {
                    if(x.getHomeworkOnTime()==1)
                        if(x.getGrade()>=0) {
                            String para = x.getName() + " ";
                            para += (x.getGrade() / x.getNumberOfGrades());
                            doc.add(new Paragraph(para));
                        }
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            });
            doc.close();
        }
        catch(Exception ex)
        {
            throw new Exception("Error when trying to convert the report into pdf.");
        }
    }


    public void exportPDFAllStudentsGrades(String path) throws Exception {
        String info="";
       List<Student> all=filterStudentsStartingWith("");
       try {
           Document doc = new Document();
           PdfWriter.getInstance(doc, new FileOutputStream(path));
           doc.open();
           doc.add(new Paragraph("Students average grades at MAP"));
           all.forEach(x -> {
               try {
                   String para=x.getName()+" ";
                   if(x.getGrade()>0)
                       para+=(x.getGrade()/x.getNumberOfGrades());
                   else
                       para+="0.00";
                   doc.add(new Paragraph(para));
               } catch (DocumentException e) {
                   e.printStackTrace();
               }
           });
            doc.close();
       }
       catch(Exception ex)
       {
            throw new Exception("Error when trying to convert the report into pdf.");
       }
    }
*/
    /*public void sendStudentEmail()
    {
        //mailSender.send("cus.paul@yahoo.com","Map Labs","Youre grades at map labs.Good work,keep it going! :)");
        mailSender.send("cus.paul@yahoo.com,cus.petre@yahoo.com","Map Labs","New grade succesufully added!","./HomeworkReports","1.txt");
    }

    public void triggerXmlParserNbyN(int fromIndex,int toIndex)
    {
            ((StudentXmlRepository) stRepo).parseXmlResourceNbyN(fromIndex,toIndex);
        else if (o instanceof Grade)
            ((GradeXmlRepository) gradeRepo).parseXmlResourceNbyN(fromIndex,toIndex);

    }
    */

}
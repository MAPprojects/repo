package tests;

import domain.*;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import junit.framework.TestCase;
import repository.*;
import service.Service;
import service.ServiceUsers;
import validator.*;

import java.util.List;

public class ServiceTest extends TestCase {
    Service ser;

    public void tearUp(Integer saptamana){
        StudentValidator stVal=new StudentValidator();
        TemeLabValidator teVal=new TemeLabValidator();
        StudentRepository stRep=new StudentRepository(stVal);
        TemeLabRepository temaRep=new TemeLabRepository(teVal);
        NotaRepository notaRep=new NotaRepository(new NotaValidator());
        Validator<Intarziere> intarziereValidator=new IntarziereValidator();
        Repository<Intarziere,Integer> repoIntarzieri=new IntarzieriRepository(intarziereValidator);

        ser=new Service(stRep,temaRep,notaRep,repoIntarzieri,saptamana);

        try {
            ser.addStudent(new Student(1,"a",15,"a","a"));
            ser.addStudent(new Student(2,"b",15,"b","b"));
            assertEquals(2,ser.getSizeSt());
        } catch (ValidationException e) {
            assertEquals(1,0);
        }

        try{
            ser.addTemaLab(new TemaLaborator(1,"a",7));
            ser.addTemaLab(new TemaLaborator(2,"b",9));
        }catch (ValidationException e){
            assertEquals(1,0);
        }
    }

    public void testFiltrareStudenti(){
        tearUp(1);
        //filtrare dupa grupa
        List<Student> f1=ser.filteredStudentsByGroup(15);
        assertEquals(2,f1.size());
        assertEquals(1,f1.get(0).getIdStudent());
        assertEquals(2,f1.get(1).getIdStudent());

        List<Student> f2=ser.filteredStudentsByEmail("a");
        assertEquals(1,f2.size());
        assertEquals(1,f2.get(0).getIdStudent());

        List<Student> f3=ser.filteredStudentsByGrades(new Float(5.0));
        assertEquals(0,f3.size());

        try {
            ser.addStudent(new Student(456,"a",90,"a","a"));
            ser.addNota((Integer)456,(Integer)2,new Float(9.5));
            f3=ser.filteredStudentsByGrades(new Float(5.0));
            assertEquals(1,f3.size());
            assertEquals(456,f3.get(0).getIdStudent());
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void testFiltrareTeme(){
        tearUp(9);
        List<TemaLaborator> f1=ser.filteredTemeByCerinta("a");
        assertEquals(1,f1.size());
        assertEquals(1,f1.get(0).getNr_tema_de_laborator());

        List<TemaLaborator> f2=ser.filteredTemeByDeadlineDepasit();
        assertEquals(1,f2.size());
        assertEquals(1,f2.get(0).getNr_tema_de_laborator());

        List<TemaLaborator> f3=ser.filteredTemeByDeadlineThisWeek();
        assertEquals(1,f3.size());
        assertEquals(2,f3.get(0).getNr_tema_de_laborator());
    }

    public void testFiltrareNote(){
        tearUp(1);
        try {
            ser.addStudent(new Student(456, "a", 90, "a", "a"));
            ser.addNota((Integer) 456, (Integer) 2, new Float(9.5));
            ser.addNota(456,1,new Float(8.9));
            ser.addNota(1,1,new Float(4.5));

        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        List<Nota> f1=ser.filteredNotebyIdStudent(456);
        assertEquals(2,f1.size());
        assertEquals(new Float(9.5),f1.get(0).getValoare());
        assertEquals(new Float(8.9),f1.get(1).getValoare());

        List<Nota> f2=ser.filteredNoteByNrTema(1);
        assertEquals(2,f2.size());
        assertEquals(new Float(8.9),f2.get(0).getValoare());
        assertEquals(new Float(4.5),f2.get(1).getValoare());

        List<Nota> f3=ser.filteredNoteByValoare(new Float(5.0));
        assertEquals(2,f3.size());
        assertEquals(new Float(9.5),f3.get(1).getValoare());
        assertEquals(new Float(8.9),f3.get(0).getValoare());
    }

    public void testStAdd(){
        tearUp(1);
        try{
            ser.addStudent(new Student(5,"",4,"m","m"));
        }catch (ValidationException e){
            assertEquals(1,e.getMesaje().size());
        }
    }

    public void testStDelete(){
        tearUp(1);
        try{
            ser.deleteStudent(1);
            assertEquals(1,ser.getSizeSt());
        }catch (EntityNotFoundException e){
            assertEquals(1,0);
        }

        try{
            ser.deleteStudent(89);
        }catch (EntityNotFoundException e){
            assertEquals("Entitatea dorita a fi stearsa nu a fost gasita in repository",e.getMessage());
            assertEquals(1,ser.getSizeSt());
        }
    }

    public void testStUpdate(){
        tearUp(1);
        try{
            ser.updateStudent(1,"nou","nou","nou",48);
            assertEquals("nou",ser.getStudent(1).get().getCadru_didactic_indrumator_de_laborator());
            assertEquals("nou",ser.getStudent(1).get().getEmail());
            assertEquals("nou",ser.getStudent(1).get().getNume());
            assertEquals(48,ser.getStudent(1).get().getGrupa());
        }catch (EntityNotFoundException e){
            assertEquals(1,0);
        }catch (ValidationException e){
            assertEquals(1,0);
        }
        try{
            ser.updateStudent(1,"","","",0);
        }catch (EntityNotFoundException e){
            assertEquals(1,0);
        }catch (ValidationException e){
            assertEquals(4,e.getMesaje().size());
        }
        try{
            ser.updateStudent(67,"p","p","p",12);
        }catch (EntityNotFoundException e){
            assertEquals("Entitatea nu a fost gasita in repository",e.getMessage());
        }catch (ValidationException e){
            assertEquals(1,0);
        }
    }
    public void testAddTemaLab(){
        tearUp(1);
        try{
            ser.addTemaLab(new TemaLaborator(0,"",-9));
        }catch (ValidationException e){
            assertEquals(3,e.getMesaje().size());
        }
    }

    public void testUpdateDeadline(){
        tearUp(1);
        try{
            ser.updateTemaLabTermenPredare(1,4);
            assertEquals(7,ser.getTemaLab(1).get().getDeadline());
            ser.updateTemaLabTermenPredare(1,8);
            assertEquals(8,ser.getTemaLab(1).get().getDeadline());
        }catch (EntityNotFoundException e){
            assertEquals(1,0);
        }catch (ValidationException e){
            assertEquals(1,0);
        }

        try{
            ser.updateTemaLabTermenPredare(45,10);
        }catch (ValidationException e){
            assertEquals(1,0);
        }catch (EntityNotFoundException e){
            assertEquals("Entitatea nu este in repository",e.getMessage());
        }

        try{
            ser.updateTemaLabTermenPredare(1,110);
        }catch (ValidationException e){
            assertEquals(1,e.getMesaje().size());
        }catch (EntityNotFoundException e){
            assertEquals(1,0);
        }
    }

    public void testAddNota(){
        tearUp(1);
        try{
            ser.addStudent(new Student(456,"a",90844,"a","a"));
            assertEquals(3,ser.getSizeSt());
            ser.addNota((Integer) 456,(Integer)1,new Float(9.8));
            assertEquals(1,ser.getSizeNote());
            /*????*/ser.addNota((Integer)456,(Integer)1,new Float(9.5));
            assertEquals(1,ser.getSizeNote());
            ser.addNota((Integer)456,(Integer)2,new Float(9.5));
            assertEquals(2,ser.getSizeNote());
            assertEquals(2,ser.getSizeNote());
            ser.addNota((Integer)1,(Integer)2,new Float(-9.5));
        } catch (ValidationException e) {
            assertEquals(1,e.getMesaje().size());
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }

        try{
            ser.addNota((Integer)4,(Integer)2,new Float(9.5));
        } catch (EntityNotFoundException e) {
            assertEquals("Entitatea nu este in repository",e.getMessage());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    public void testUpdateNota(){
        tearUp(1);
        try{
            ser.addStudent(new Student(456,"a",90,"a","a"));
            ser.addNota((Integer)456,(Integer)2,new Float(9.5));
            assertEquals(1,ser.getSizeNote());
            Integer idNota=1;
            for (Nota n:ser.findAllNote()){
                if ((n.getIdStudent().equals(456)) && (n.getNrTema().equals(2))) idNota=n.getIdNota();
            }
            ser.modificareNota(new Nota(idNota,(Integer)456,(Integer)2,new Float(10)));
            assertEquals(1,ser.getSizeNote());
            ser.addNota((Integer)456,(Integer)1,new Float(9.5));
            for (Nota n:ser.findAllNote()){
                if ((n.getIdStudent().equals(456)) && (n.getNrTema().equals(1))) idNota=n.getIdNota();
            }
            ser.modificareNota(new Nota(idNota,(Integer)456,(Integer)2,new Float(10)));
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (EntityNotFoundException e) {
            assertEquals("Entitatea nu este in repository",e.getMessage());
            assertEquals(2,ser.getSizeNote());
        }
        try{
            Integer idNota=1;
            for (Nota n:ser.findAllNote()){
                if ((n.getIdStudent().equals(456)) && (n.getNrTema().equals(1))) idNota=n.getIdNota();
            }
            ser.modificareNota(new Nota(idNota,(Integer)456,(Integer)1,new Float(-5)));
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            assertEquals(1,e.getMesaje().size());
        }
    }
}

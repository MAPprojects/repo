package UI;

import domain.Nota;
import domain.Student;
import domain.TemaLaborator;
import exceptii.EntityNotFoundException;
import exceptii.ValidationException;
import service.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UI {
    Service service;
    BufferedReader in=new BufferedReader(new InputStreamReader(System.in));

    /**
     * Constructor
     * @param service Service
     */
    public UI(Service service) {
        this.service = service;
    }

    /**
     * Prints menu
     */
    private void afisareMeniu(){
        System.out.println("MENIU\n" +
                "1. Adauda student nou\n" +
                "2. Modifica student existent\n" +
                "3. Sterge student\n" +
                "4. Afisare studenti existenti\n" +
                "5. Adaugare tema laborator\n" +
                "6. Modificare termen de predare pentru o tema existenta\n" +
                "7. Afisare teme de laborator existente\n"+
                "8. Afisare note existente\n"+
                "9. Adaugare nota\n"+
                "10. Modificare nota existenta\n"+
                "0. Iesire aplicatie\n");
    }

    /**
     * Add student UI
     * @throws IOException for console reading
     */
    private void addStudentUI() throws IOException{
        String idS= "",nume="",email="",nume_cadru_didactic="",group="";
        System.out.println("Introduceti datele studentului:\n"+" 1. Id student:\n");
        idS=in.readLine();
        System.out.println("2. Nume student:");
        nume=in.readLine();
        System.out.println("3. Grupa:");
        group=in.readLine();
        System.out.println("4. Email:");
        email=in.readLine();
        System.out.println("5. Nume cadru didactic indrumator");
        nume_cadru_didactic=in.readLine();
        Student st = new Student(Integer.parseInt(idS),nume,Integer.parseInt(group),email,nume_cadru_didactic);
        try {
            service.addStudent(st);
        } catch (ValidationException e) {
            System.out.println(e);
        }
    }

    /**
     * Update Student UI
     * @throws IOException for console reading
     */
    private void updateStudentUI() throws IOException{
        System.out.println("Dati idul studentului pe care doriti sa il modificati\n");
        String idStudent=in.readLine(),newName,newEmail,newGroup,newProfesor;
        System.out.println("Dati noul nume al studentului\n");
        newName=in.readLine();
        System.out.println("Dati noul email al studentului\n");
        newEmail=in.readLine();
        System.out.println("Dati noul cadru didactic\n");
        newProfesor=in.readLine();
        System.out.println("Dati noua grupa\n");
        newGroup=in.readLine();
        try {
            service.updateStudent(Integer.parseInt(idStudent),newName,newEmail,newProfesor,Integer.parseInt(newGroup));
        } catch (ValidationException e) {
            System.out.println(e);
        } catch (EntityNotFoundException e) {
            System.out.println(e);
        }
    }

    /**
     * Detele student from repo UI
     * @throws IOException for console reading
     */
    private void deleteStudentUI() throws IOException{
        System.out.println("Dati idul studentului pe care doriti sa il stergeti\n");
        String idStudent=in.readLine();
        try {
            service.deleteStudent(Integer.parseInt(idStudent));
        } catch (EntityNotFoundException e) {
            System.out.println(e);
        }
    }

    /**
     * Prints all students from repo
     */
    private void afisareStudentiUI(){
        System.out.println("Lista Studenti:");
        for (Student st: service.findAllStudents()){
            System.out.println(st);
        }
    }

    /**
     * add TemaLaborator UI
     * @throws IOException for console reading
     */
    private void addTemaLaboratorUI() throws IOException{
        System.out.println("Introduceti datele despre tema de laborator\n 1. Numarul temei\n");
        String nrTema,cerinta,deadline;
        nrTema=in.readLine();
        System.out.println("2. Cerinta pe scurt\n");
        cerinta=in.readLine();
        System.out.println("3. Deadlineul temei de laborator\n");
        deadline=in.readLine();
        TemaLaborator tm=new TemaLaborator(Integer.parseInt(nrTema),cerinta,Integer.parseInt(deadline));
        try{
            service.addTemaLab(tm);
        } catch (ValidationException e) {
            System.out.println(e);
        }
    }

    /**
     * update Deadline UI
     * @throws IOException for console reading
     */
    private void updateTermenPredareUI() throws IOException{
        String idTema,newDeadline;
        System.out.println("Dati idul temei careia doriti sa ii modificati termenul limita\n");
        idTema=in.readLine();
        System.out.println("Dati noul termen limita\n");
        newDeadline=in.readLine();
        try {
            service.updateTemaLabTermenPredare(Integer.parseInt(idTema),Integer.parseInt(newDeadline));
        } catch (EntityNotFoundException e) {
            System.out.println(e);
        } catch (ValidationException e) {
            System.out.println(e);
        }
    }

    /**
     * Prints all temaLaborator froom repo
     */
    private void afisareTemeLaborator(){
        System.out.println("Lista teme de laborator:");
        for (TemaLaborator t:service.findAllTemeLab()){
            System.out.println(t);
        }
    }

    /**
     * Prints all grades from repo
     */
    private void afisareNote(){
        System.out.println("Lista note");
        for (Nota nota:service.findAllNote()){
            System.out.println(nota);
        }
    }

    /**
     *
     * @return String the command given by the user
     * @throws IOException for console reading
     */
    private String citireComanda() throws IOException{
        return in.readLine();
    }

    /**
     * update Nota UI
     * @throws IOException for console reading
     */
    private void updateNota() throws IOException{
        String idNota,idStudent,idTema,valoare;
        System.out.println("Dati idul notei pe care doriti sa o modificati");
        idNota=in.readLine();
        System.out.println("Dati idul studentului");
        idStudent=in.readLine();
        System.out.println("Dati numarul temei de laborator");
        idTema=in.readLine();
        System.out.println("Dati noua nota");
        valoare=in.readLine();

        Nota nota=new Nota(Integer.parseInt(idNota),Integer.parseInt(idStudent),Integer.parseInt(idTema),Float.parseFloat(valoare));
        try {
            if (service.modificareNota(nota)==null)
                System.out.println("Nota a fost modificata");
        } catch (ValidationException e) {
            System.out.println(e);
        } catch (EntityNotFoundException e) {
            System.out.println(e);
        }
    }

    /**
     * Add Nota UI
     * @throws IOException for console reading
     */
    private void addNota() throws IOException{
        String idStudent,idTema,valoare;
        System.out.println("Dati idul studentului");
        idStudent=in.readLine();
        System.out.println("Dati numarul temei de laborator");
        idTema=in.readLine();
        System.out.println("Dati nota");
        valoare=in.readLine();

        try {
            if (service.addNota(Integer.parseInt(idStudent),Integer.parseInt(idTema),Float.parseFloat(valoare))==null){
                System.out.println("Nota a fost adaugata\n");
            }
            else System.out.println("Nota nu a fost adaugata\n");
        } catch (EntityNotFoundException e) {
            System.out.println(e);
        } catch (ValidationException e) {
            System.out.println(e);
        }
    }

    /**
     * Run UI
     * @throws IOException For reading from console
     */
    public void runUI()throws IOException{
        while (true) {
            afisareMeniu();
            String cmd = citireComanda();
            if (cmd.equals("0")){
                System.out.println("La revedere");
                break;
            }
            switch (cmd) {
                case "1": {
                    addStudentUI();
                    break;
                }
                case "2": {
                    updateStudentUI();
                    break;
                }
                case "3": {
                    deleteStudentUI();
                    break;
                }
                case "4": {
                    afisareStudentiUI();
                    break;
                }
                case "5": {
                    addTemaLaboratorUI();
                    break;
                }
                case "6": {
                    updateTermenPredareUI();
                    break;
                }
                case "7":{
                    afisareTemeLaborator();
                    break;
                }
                case "8":{
                    afisareNote();
                    break;
                }
                case "9":{
                    addNota();
                    break;
                }
                case "10":{
                    updateNota();
                    break;
                }
                default: {
                    System.out.println("Comanda invalida!!!\n");
                }
            }
        }
    }
}

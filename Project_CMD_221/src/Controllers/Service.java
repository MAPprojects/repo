package Controllers;

import Entities.Teme;
import Entities.User;
import Repository.IRepository;
import Entities.Student;
import Repository.IUserRepository;
import Repository.UserRepository;
import Validators.ValidatorException;
import Entities.Nota;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import utils.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import com.sun.mail.smtp.SMTPMessage;

public class Service {
    private IRepository<Integer, Student> repo_st;
    private IRepository<Integer, Teme> repo_t;
    private IRepository<Pair<Integer, Integer>, Nota> repo_n;
    private IUserRepository<Pair<String, String>, User> repo_user;
    LocalDate startingDate = LocalDate.of(2017, Month.OCTOBER, 3);
    LocalDate endDate = LocalDate.now();
    private long saptCurent = ChronoUnit.WEEKS.between(startingDate, endDate) - 1;

    public Service(IRepository<Integer, Student> repo_st, IRepository<Integer, Teme> repo_t, IRepository<Pair<Integer, Integer>, Nota> repo_n, IUserRepository<Pair<String, String>, User> repo_user) {
        this.repo_st = repo_st;
        this.repo_t = repo_t;
        this.repo_n = repo_n;
        this.repo_user = repo_user;
    }

    public void save_student(Integer id, String nume, String email, String grupa, String prof, Integer note, Integer nr, Double medie, Boolean val, Boolean fint) throws ValidatorException {
        Student student = new Student(id, nume, email, grupa, prof, note, nr, medie, val, fint);
        repo_st.save(student);
    }

    public void update_student(Integer id, String nume, String email, String grupa, String prof, Integer note, Integer nr, Double medie, Boolean val, Boolean fint) {
        Student student = new Student(id, nume, email, grupa, prof, note, nr, medie, val, fint);
        repo_st.update(id, student);
    }

    public void delete_student(Integer id) {
        Student st = findStudent(id);
        repo_st.delete(id);
    }

    public Iterable<Student> getAllStudents() {
        return repo_st.getAll();
    }

    public int sizeStudent() {
        return repo_st.size();
    }

    public Student findStudent(Integer id) {
        return repo_st.findOne(id);
    }

    public void save_tema(Integer id, Integer deadline, String descriere, Integer dif) throws ValidatorException {
        if ((int) saptCurent >= deadline)
            throw new ControllerException("Termenul de predare trebuie sa fie setat in saptamanile urmatoare.\n");
        Teme tema = new Teme(id, deadline, descriere, dif);
        repo_t.save(tema);
        notificareTeme("Tema noua", "Buna ziua! \nS-a adaugat o tema noua. \nVa doresc o zi placuta!");
    }

    public void update_tema(Integer id, Integer deadline, String descriere, Integer dif) {
        Teme tema = new Teme(id, deadline, descriere, dif);
        repo_t.update(id, tema);
    }

    public void update_Deadline(Integer id, Integer deadline) throws ValidatorException {
        System.out.println(saptCurent);
        /*if ((int) saptCurent <= this.findTema(id).getDeadline())
            throw new ControllerException("Termenul de predare trebuie sa fie setat in saptamanile urmatoare.\n");*/
        if ((int) saptCurent > deadline)
            throw new ControllerException("Termenul de predare trebuie sa fie setat in saptamanile urmatoare.\n");
        repo_t.update(id, new Teme(id, deadline, this.findTema(id).getDescriere(), this.findTema(id).getDif()));
        notificareTeme("Modificare deadline", "Buna ziua! \nS-a modificat deadline-ul temei " + id + " .\nVa doresc o zi placuta!");
    }

    public void delete_tema(Integer id) {
        repo_t.delete(id);
    }

    public Iterable<Teme> getAllTeme() {
        return repo_t.getAll();
    }

    public int sizeTeme() {
        return repo_t.size();
    }

    public Teme findTema(Integer id) {
        return repo_t.findOne(id);
    }

    public void saveNota(Pair<Integer, Integer> id, String nume, Integer val, Integer week) {
        Student st = findStudent(id.getKey());
        Teme t = findTema(id.getValue());
        if (st == null || t == null || (st == null && t == null))
            throw new ControllerException("Nu exista studentul sau tema");
        for (Nota note : getAllNote())
            if (note.getIDStudent() == st.getID() && note.getNrTema() == t.getID())
                throw new ControllerException("Nu puteti adauga la acelasi student aceeasi tema inca o data");
        Integer dif = t.getDif();
        Integer marks = st.getNote();
        Integer nr = st.getNr();
        if (week > saptCurent)
            throw new ControllerException("Saptamana de predare nu poate fi mai mare decat saptamana curenta");
        if (t.getDeadline() == week - 1) {
            val = val - 2;
            update_tema(t.getID(), t.getDeadline(), t.getDescriere(), dif + 1);
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, st.isVal(), false);
        }
        if (t.getDeadline() == week - 2) {
            val = val - 4;
            update_tema(t.getID(), t.getDeadline(), t.getDescriere(), dif + 2);
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, st.isVal(), false);
        }
        if (t.getDeadline() < week - 2) {
            val = 1;
            update_tema(t.getID(), t.getDeadline(), t.getDescriere(), dif + 3);
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, st.isVal(), false);
        }
        Nota nota = new Nota(id.getKey(), st.getNume(), id.getValue(), val);
        repo_n.save(nota);
        nr++;
        marks = marks + val;
        if ((double) marks / nr > 5.0)
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, true, st.isFint());
        else
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, false, st.isFint());
        export(nota, week, "-", "Adaugare nota");
        notificareNote("Adaugare nota", "Buna ziua! \nVi s-a adaugat o nota. Va trimit atasat fisierul dumneavoastra cu note. \nVa doresc o zi placuta!", id.getKey() + ".txt");
    }

    public void updateNota(Pair<Integer, Integer> id, String nume, Integer val, Integer week) {
        Student st = findStudent(id.getKey());
        Teme t = findTema(id.getValue());
        if (st == null || t == null || (st == null && t == null))
            throw new ControllerException("Nu exista studentul sau tema");
        if (week > saptCurent)
            throw new ControllerException("Saptamana de predare nu poate fi mai mare decat saptamana curenta");
        Nota note = findNota(id);
        Integer dif = t.getDif();
        Integer marks = st.getNote();
        Integer nr = st.getNr();
        if (t.getDeadline() == week - 1) {
            val = val - 2;
            update_tema(t.getID(), t.getDeadline(), t.getDescriere(), dif + 1);
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, st.isVal(), false);
        }
        if (t.getDeadline() == week - 2) {
            val = val - 4;
            update_tema(t.getID(), t.getDeadline(), t.getDescriere(), dif + 2);
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, st.isVal(), false);
        }
        if (t.getDeadline() < week - 2) {
            val = 1;
            update_tema(t.getID(), t.getDeadline(), t.getDescriere(), dif + 3);
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, st.isVal(), false);
        }
        if (note.getValoare() > val)
            val = note.getValoare();
        marks = marks + val;
        nr++;
        Nota nota = new Nota(id.getKey(), st.getNume(), id.getValue(), val);
        repo_n.update(id, nota);
        if ((double) marks / nr > 5.0)
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, true, st.isFint());
        else
            update_student(st.getID(), st.getNume(), st.getEmail(), st.getGrupa(), st.getProf(), marks, nr, (double) marks / nr, false, st.isFint());
        export(nota, week, "-", "Modificare nota");
        notificareNote("Modificare nota", "Buna ziua! \nVi s-a modificat o nota. Va trimit atasat fisierul dumneavostra cu note. \nVa doresc o zi placuta!", id.getKey() + ".txt");
    }

    public void deleteNota(Pair<Integer, Integer> id) {
        repo_n.delete(id);
    }

    public Iterable<Nota> getAllNote() {
        return repo_n.getAll();
    }

    public int sizeNote() {
        return repo_n.size();
    }

    public Nota findNota(Pair<Integer, Integer> id) {
        return repo_n.findOne(id);
    }

    public void export(Nota nota, Integer week, String obs, String tip) {
        String filename = "" + nota.getIDStudent() + ".txt";
        String line = "";
        Integer sum = 0, nr = 0;
        Float medie;
        try (PrintWriter pr = new PrintWriter(new FileWriter(filename, true))) {
            Student st = findStudent(nota.getIDStudent());
            String s = "IDStudent: " + nota.getIDStudent() + " Nume: " + st.getNume() + " NrTema: " + nota.getNrTema() + " Nota: " + nota.getValoare() + " Deadline: " + findTema(nota.getNrTema()).getDeadline() + " Saptamana predarii: " + week + " Observatii: " + obs + " " + tip + "\n";
            pr.append(s);
        } catch (IOException e) {
            throw new ControllerException("Nu s-a putut salva entitatea");
        }
    }

    public void save_user(Pair<String, String> ID, Integer tip1, Integer tip2) throws ValidatorException {
        User user = new User(ID.getKey(), ID.getValue(), tip1, tip2);
        repo_user.save(user);
    }

    public void update_user(Pair<String, String> ID, Integer tip1, Integer tip2) throws ValidatorException {
        User user = new User(ID.getKey(), ID.getValue(), tip1, tip2);
        repo_user.update(ID, user);
    }

    public Iterable<User> getAllUsers() {
        return repo_user.getAll();
    }

    public User findUser(Pair<String, String> id) {
        return repo_user.findOne(id);
    }

    public <E> List<E> fromIterableToList(Iterable<E> itList) {
        List<E> l = new ArrayList<>();
        for (E e : itList) {
            l.add(e);
        }
        return l;
    }

    private static class Compare {
        public static int CompareStudents(Student st, Student st2) {
            return st.getNume().compareTo(st2.getNume());
        }

        public static int CompareStudentsDesc(Student st, Student st2) {
            return st2.getNume().compareTo(st.getNume());
        }

        public static int CompareTemeByID(Teme tema, Teme tema2) {
            if (tema.getID() < tema2.getID())
                return 1;
            if (tema.getID() > tema2.getID())
                return -1;
            return 0;
        }

        public static int CompareTemeByIDDesc(Teme tema, Teme tema2) {
            if (tema.getID() < tema2.getID())
                return -1;
            if (tema.getID() > tema2.getID())
                return 1;
            return 0;
        }

        public static int CompareNotaByIDStudent(Nota nota, Nota nota2) {
            if (nota.getIDStudent() < nota2.getIDStudent())
                return 1;
            if (nota.getIDStudent() > nota2.getIDStudent())
                return -1;
            return 0;
        }

        public static int CompareNotaByValoare(Nota nota, Nota nota2) {
            if (nota.getValoare() < nota2.getValoare())
                return 1;
            if (nota.getValoare() > nota2.getValoare())
                return -1;
            return 0;
        }
    }

    public <E> List<E> genericFilter(List<E> l, Predicate<E> predicate, Comparator<E> comparator) {
        return l.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }

    public List<Student> filterStudentByGroup(List<Student> l, String grupa) {
        //Iterable<Student> l = getAllStudents();
        return genericFilter(fromIterableToList(l), x -> x.getGrupa().equals(grupa), Compare::CompareStudents);
    }

    public List<Student> filterStudentByTeacher(List<Student> l, String prof) {
        //Iterable<Student> l = getAllStudents();
        return genericFilter(fromIterableToList(l), x -> x.getProf().equals(prof), Compare::CompareStudentsDesc);
    }

    public List<Student> filterByFirstLetter(List<Student> l, String litera) {
        Predicate<Student> predicate = x -> x.getNume().indexOf(litera) == 0;
        //Iterable<Student> l = getAllStudents();
        return genericFilter(fromIterableToList(l), predicate, Compare::CompareStudents);
    }

    public List<Teme> filterByDeadline(int deadline) {
        return genericFilter(fromIterableToList(getAllTeme()), x -> x.getDeadline() == deadline, Compare::CompareTemeByID);
    }

    public List<Teme> filterByLowerDeadline(int deadline) {
        return genericFilter(fromIterableToList(getAllTeme()), x -> x.getDeadline() < deadline, Compare::CompareTemeByIDDesc);
    }

    public List<Teme> filterByHigherDeadline(int deadline) {
        return genericFilter(fromIterableToList(getAllTeme()), x -> x.getDeadline() > deadline, Compare::CompareTemeByID);
    }

    public List<Nota> filterByValoare(int val) {
        return genericFilter(fromIterableToList(getAllNote()), x -> x.getValoare() == val, Compare::CompareNotaByIDStudent);
    }

    public List<Nota> filterByStudent(int idStudent) {
        return genericFilter(fromIterableToList(getAllNote()), x -> x.getIDStudent() == idStudent, Compare::CompareNotaByValoare);
    }

    public List<Nota> filterByNrHomework(int nrTema) {
        return genericFilter(fromIterableToList(getAllNote()), x -> x.getNrTema() == nrTema, Compare::CompareNotaByIDStudent);
    }

    public void notificareTeme(String subject, String text) {
        String address = "";
        int nr = 0;
        for (Student st : getAllStudents()) {
            if (nr > sizeStudent()/2) {
                address = address.substring(0, address.length() - 1);
                    sendMail(address, subject, text);
                address = "";
                nr = 0;
            }
            address = address + st.getEmail() + ",";
            nr++;
        }
        sendMail(address, subject, text);
    }

    public void notificareNote(String subject, String text, String filename) {
        String address = "";
        int nr = 0;
        for (Student st : getAllStudents()) {
            if (nr > sizeStudent()/2) {
                address = address.substring(0, address.length() - 1);
                sendMail2(address, subject, text, filename);
                address = "";
                nr = 0;
            }
            address = address + st.getEmail() + ",";
            nr++;
        }
        sendMail2(address, subject, text, filename);
    }

    public int fetchForwardS() {
        return repo_st.fetchForward();
    }

    public int fetchBackwardS() {
        return repo_st.fetchBackward();
    }

    public int fetchForwardT() {
        return repo_t.fetchForward();
    }

    public int fetchBackwardT() {
        return repo_t.fetchBackward();
    }

    public int fetchForwardN() {
        return repo_n.fetchForward();
    }

    public int fetchBackwardN() {
        return repo_n.fetchBackward();
    }

    public Iterable<Student> findAllStudenti() {
        return repo_st.findAll();
    }

    public Iterable<Teme> findAllTeme() {
        return repo_t.findAll();
    }

    public Iterable<Nota> findAllNote() {
        return repo_n.findAll();
    }

    public void sendMail(String address, String subject, String text) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "805");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("czelimark@gmail.com", "kalinikta");
            }
        });

        try {
            SMTPMessage message = new SMTPMessage(session);
            message.setFrom(new InternetAddress("czelimark@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
            message.setSubject(subject);
            message.setText(text);
            message.setNotifyOptions(SMTPMessage.NOTIFY_SUCCESS);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
            public void sendMail2(String address, String subject, String text, String filename) {
                try {
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.socketFactory.class",
                            "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "805");
                    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication("czelimark@gmail.com", "kalinikta");
                        }
                    });
                    SMTPMessage message = new SMTPMessage(session);
                    message.setFrom(new InternetAddress("czelimark@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
                    message.setSubject(subject);
                    // Create the message part
                    BodyPart messageBodyPart = new MimeBodyPart();

                    messageBodyPart.setText(text);

                    Multipart multipart = new MimeMultipart();
                    // Set text message part
                    multipart.addBodyPart(messageBodyPart);

                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(filename);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(filename);
                    multipart.addBodyPart(messageBodyPart);

                    // Send the complete message parts
                    message.setContent(multipart);
                    message.setNotifyOptions(SMTPMessage.NOTIFY_SUCCESS);
                    Transport.send(message);
                }
                catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
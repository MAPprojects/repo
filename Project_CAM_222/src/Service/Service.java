package Service;

import Domain.Nota;
import Repository.IRepository;
import Domain.Student;
import Domain.Tema;
import Domain.ValidationException;
import Utils.Observable;
import Utils.Observer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

class RunnableEmail implements Runnable {
    private String mesaj;
    private String mail;

    public RunnableEmail(String mail, String mesaj) {
        this.mesaj = mesaj;
        this.mail = mail;
    }

    @Override
    public void run() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("email@gmail.com", parola);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("email@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail));
            message.setSubject("Grade Activity");
            message.setText(mesaj);

            Transport.send(message);

            //System.out.println("Executat cu succes!");
        } catch (MessagingException e) {
            //System.out.println("Eroare!");
        }
    }

    String parola = "parola";
}


public class Service implements Observable {
    private IRepository<Integer,Student> repositoryStudenti;
    private IRepository<Integer, Tema> repositoryTeme;
    private IRepository<String, Nota> repositoryNote;

    private List<Observer> observers = new ArrayList<>();

    public static int weekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + 11;

    public Service(IRepository<Integer, Student> repositoryStudenti,
                   IRepository<Integer, Tema> repositoryTeme,
                   IRepository<String, Nota> repositoryNote) {
        this.repositoryStudenti = repositoryStudenti;
        this.repositoryTeme = repositoryTeme;
        this.repositoryNote = repositoryNote;
    }

    public void modificareDeadline(Integer id, int deadline) {
        Optional<Tema> temaLab = repositoryTeme.findOne(id);
        if (temaLab.isPresent()) {
            if ((weekOfYear < temaLab.get().getDeadline()) && (deadline < 14) && (temaLab.get().getDeadline() < deadline)) {
                repositoryTeme.update(new Tema(id, temaLab.get().getDescriere(), deadline));
                String mesaj = "Deadline modified for assignment: " + temaLab.get().getDescriere() + "; new deadline: week " + deadline + ".";
                for (Student s : repositoryStudenti.getAll()) {
                    RunnableEmail runnableEmail = new RunnableEmail(s.getEmail(), mesaj);
                    Thread t = new Thread(runnableEmail);
                    t.start();
                }
            }
            else throw new ValidationException(("The deadline can no longer be modified!"));
        }
        notifyObservers();
    }

    public void addStudent(Student student) {
        repositoryStudenti.save(student);
        notifyObservers();
    }

    public void modifyStudent(Student student) {
        repositoryStudenti.update(student);
        notifyObservers();
    }

    public void deleteStudent(Integer id) {
        repositoryStudenti.delete(id);
        notifyObservers();
    }

    public void addTema(Tema tema) {
        repositoryTeme.save(tema);
        String mesaj = "Assignment added: " + tema.getDescriere() + "; deadline in week " + tema.getDeadline() + ".";
        for (Student s : repositoryStudenti.getAll()) {
            RunnableEmail runnableEmail = new RunnableEmail(s.getEmail(), mesaj);
            Thread t = new Thread(runnableEmail);
            t.start();
        }
        notifyObservers();
    }

    public void modifyTema(Tema tema) {
        repositoryTeme.update(tema);
        String mesaj = "Assignment modified: " + tema.getDescriere() + "; deadline in week " + tema.getDeadline() + ".";
        for (Student s : repositoryStudenti.getAll()) {
            RunnableEmail runnableEmail = new RunnableEmail(s.getEmail(), mesaj);
            Thread t = new Thread(runnableEmail);
            t.start();
        }
        notifyObservers();
    }

    public void deleteTema(Integer id) {
        repositoryTeme.delete(id);
        notifyObservers();
    }

    public void deleteNota(String id) {
        repositoryNote.delete(id);
        notifyObservers();
    }

    public void addNota(Nota nota, String observatii, int saptamana) {
        // Gasirea temei / a studentului la care se refera nota
        Optional<Student> studentOptional = repositoryStudenti.findOne(nota.getStudent().getId());
        Optional<Tema> temaOptional = repositoryTeme.findOne(nota.getTema().getId());

        if (studentOptional.isPresent() && temaOptional.isPresent()) {
            //daca s-a intarziat mai mult de 2 saptamani, nota e automat 1
            if (saptamana - temaOptional.get().getDeadline() >= 3)
                nota.setValoare(1);
            else if (saptamana > temaOptional.get().getDeadline()) {
                nota.setValoare(nota.getValoare() - (saptamana - temaOptional.get().getDeadline()) * 2);
                System.out.println(nota.getValoare());
            }
            
            // Cautare daca exista deja o nota pentru acel student la acea tema:
            boolean exista = false;
            for (Nota n : getAllNote()) {
                if (n.getStudent().getId().equals(nota.getStudent().getId()) &&
                        n.getTema().getId().equals(nota.getTema().getId())) {
                    nota.setId(n.getId());
                    exista = true;
                    if (n.getValoare() >= nota.getValoare())
                        throw new ValidationException("The newer grade is lower than the previous one!");
                    break;
                }
            }

            // Gasirea temei / a studentului la care se refera nota
            Student student = studentOptional.get();
            Tema tema = temaOptional.get();

            // Cream fisierul de note daca nu exista
            String numeFisier = "" + nota.getStudent().getId() + ".txt";

            // Adaug./Modif. nota, NrTema, Nota, Deadline, Saptamana Predarii temei, Observatii
            // care contin explicatii in legatura cu depunctarile efectuate (daca este cazul).
            try {
                try (FileReader fileReader = new FileReader(numeFisier)) {
                } catch (IOException e) {
                    FileWriter fileWriter = new FileWriter(numeFisier);
                    fileWriter.write("---------------------------------------------------\n");
                    fileWriter.write("                 STUDENT DATA\n");
                    fileWriter.write("            Name: " + student.getNume() + "\n");
                    fileWriter.write("           Group: " + student.getGrupa() + "\n");
                    fileWriter.write("           Email: " + student.getEmail() + "\n");
                    fileWriter.write("         Teacher: " + student.getProfIndrumator() + "\n");
                    fileWriter.write("---------------------------------------------------\n");
                    fileWriter.write("       HISTORY OF THE STUDENT'S GRADES\n");
                    fileWriter.close();
                }

                // Deschidem fisierul in append
                FileWriter fw = new FileWriter(numeFisier, true);
                String s = "";

                // Updatam nota, daca exista deja
                if (exista) {
                    s = s + "Updated Grade, ";
                    repositoryNote.update(nota);
                }

                // Altfel, o salvam
                else {
                    s = s + "New Grade, ";
                    repositoryNote.save(nota);
                }

                // Scriem restul lucrurilor in fisierul 'idStudent'.txt
                s = s + "Assignment Nr. " + tema.getId() + ", ";
                s = s + "Grade " + nota.getValoare() + ", ";
                s = s + "Deadline week " + tema.getDeadline() + ", ";
                s = s + "Handin week " + saptamana + ", ";
                s = s + "Observations: " + observatii + "\n";

                fw.write(s);
                RunnableEmail runnableEmail = new RunnableEmail(nota.getStudent().getEmail(),s);
                Thread t = new Thread(runnableEmail);
                t.start();

                // Inchidem fisierul
                fw.close();
            } catch (IOException e) {
                System.err.println("Eroare IO: " + e);
                System.exit(1);
            }
        }
        notifyObservers();
    }

    public Optional<Student> findStudent(Integer id) {
        return repositoryStudenti.findOne(id);
    }

    public Optional<Tema> findTema(Integer id) {
        return repositoryTeme.findOne(id);
    }

    public Optional<Nota> findNota(String id) {
        return repositoryNote.findOne(id);
    }

    public Iterable<Student> getAllStudenti() {
        return repositoryStudenti.getAll();
    }

    public Iterable<Tema> getAllTeme() {
        return repositoryTeme.getAll();
    }

    public Iterable<Nota> getAllNote() {
        return repositoryNote.getAll();
    }

    // Filtreaza studentii dupa grupa in care se afla, invers alfabetic
    public List<Student> filtrareStudentiDupaGrupa(Integer grupa) {
        Predicate<Student> p1 = (x -> x.getGrupa().equals(grupa));
        Comparator<Student> c1 = Comparator.comparing(Student::getNume).reversed();
        Iterable<Student> lista = repositoryStudenti.getAll();
        List<Student> l1 = new ArrayList<>();
        lista.forEach(l1::add);
        return Filtrare.filtrareSortare(l1, p1, c1);
    }

    //Filtreaza studentii dupa proful indrumator, alfabetic
    public List<Student> filtrareStudentiDupaProf(String prof) {
        Predicate<Student> p1 = (x -> x.getProfIndrumator().contains(prof));
        Comparator<Student> c1 = Comparator.comparing(Student::getNume);
        Iterable<Student> lista = (repositoryStudenti.getAll());
        List<Student> l1 = new ArrayList<>();
        lista.forEach(l1::add);
        return Filtrare.filtrareSortare(l1, p1, c1);
    }

    // Filtreaza studentii cu un anumit nume in ordine invers alfabetica
    public List<Student> filtrareStudentiDupaNume(String nume) {
        Predicate<Student> p1 = (x -> x.getNume().contains(nume));
        Comparator<Student> c1 = Comparator.comparing(Student::getNume).reversed();
        Iterable<Student> lista = repositoryStudenti.getAll();
        List<Student> l1 = new ArrayList<>();
        lista.forEach(l1::add);
        return Filtrare.filtrareSortare(l1, p1, c1);
    }

    // Filtreaza toate temele dupa deadline crescator
    public List<Tema> filtrareTemeDupaDeadline(Integer deadline) {
        Predicate<Tema> p1 = (x -> x.getDeadline() >= deadline);
        Comparator<Tema> c1 = Comparator.comparing(Tema::getDeadline);
        Iterable<Tema> lista = repositoryTeme.getAll();
        List<Tema> l1 = new ArrayList<>();
        lista.forEach(l1::add);
        return Filtrare.filtrareSortare(l1, p1, c1);
    }

    // Filtreaza toate temele care contin o anumita descriere, alfabetic
    public List<Tema> filtrareTemeDupaDescriere(String descriere) {
        Predicate<Tema> p1 = (x -> x.getDescriere().contains(descriere));
        Comparator<Tema> c1 = Comparator.comparing(Tema::getDeadline);
        Iterable<Tema> lista = repositoryTeme.getAll();
        List<Tema> l1 = new ArrayList<>();
        lista.forEach(l1::add);
        return Filtrare.filtrareSortare(l1, p1, c1);
    }

    // Filtreaza toate temele cu deadline pana in saptamana s data
    public List<Tema> filtrareTemeCuDeadline(Integer deadline) {
        Predicate<Tema> p1 = (x -> x.getDeadline() <= deadline);
        Comparator<Tema> c1 = Comparator.comparing(Tema::getDeadline);
        Iterable<Tema> lista = repositoryTeme.getAll();
        List<Tema> l1 = new ArrayList<>();
        lista.forEach(l1::add);
        return Filtrare.filtrareSortare(l1, p1, c1);
    }

    // Filtreaza toate notele peste 5, dupa nume, alfabetic
    public List<Nota> filtrareNotePeste5() {
        Predicate<Nota> p1 = (x -> x.getValoare() > 5);
        Comparator<Nota> c1 = Comparator.comparing(x -> x.getStudent().getNume());
        Iterable<Nota> lista = repositoryNote.getAll();
        List<Nota> l1 = new ArrayList<>();
        lista.forEach(l1::add);
        return Filtrare.filtrareSortare(l1, p1, c1);
    }

    // Filtreaza toate notele sub 5 dupa valoarea lor
    public List<Nota> filtrareNoteSub5() {
        Predicate<Nota> p1 = (x -> x.getValoare() < 5);
        Comparator<Nota> c1 = Comparator.comparing(Nota::getValoare).reversed();
        Iterable<Nota> lista = repositoryNote.getAll();
        List<Nota> l1 = new ArrayList<>();
        lista.forEach(l1::add);
        return Filtrare.filtrareSortare(l1, p1, c1);
    }

    // Filtreaza toate notele de 10 dupa numarul temei
    public List<Nota> filtrareNoteDe10() {
        Predicate<Nota> p1 = (x -> x.getValoare().equals(10));
        Comparator<Nota> c1 = Comparator.comparing(x -> x.getTema().getId());
        Iterable<Nota> lista = repositoryNote.getAll();
        List<Nota> l1 = new ArrayList<>();
        lista.forEach(l1::add);
        return Filtrare.filtrareSortare(l1, p1, c1);
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observers){
            o.update(this);
        }
    }

    public int getStudentiCount(Predicate<Student> filter) {
        List<Student> listaInitiala = new ArrayList<>();
        getAllStudenti().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);
        return listaInitiala.size();
    }

    public int getTemeCount(Predicate<Tema> filter) {
        List<Tema> listaInitiala = new ArrayList<>();
        getAllTeme().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);
        return listaInitiala.size();
    }

    public int getNoteCount(Predicate<Nota> filter) {
        List<Nota> listaInitiala = new ArrayList<>();
        getAllNote().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);
        return listaInitiala.size();
    }

    public int getNoteCount(Predicate<Nota> filter, String email) {
        List<Nota> listaInitiala = new ArrayList<>();
        for (Nota n : getAllNote())
            if (Objects.equals(n.getEmailStudent(), email))
                listaInitiala.add(n);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);
        return listaInitiala.size();
    }

    public int getStudentiCount() {
        return repositoryStudenti.size();
    }

    public int getTemeCount() {
        return repositoryTeme.size();
    }

    public int getNoteCount() {
        return repositoryNote.size();
    }

    public List<Student> studentsSublist(int startIndex, int endIndex, Predicate<Student> filter) {
        List<Student> lista = new ArrayList<>();

        List<Student> listaInitiala = new ArrayList<>();
        getAllStudenti().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);

        int i = 0;
        for (Student s : listaInitiala) {
            if (i >= startIndex && i < endIndex)
                lista.add(s);
            i++;
            if (i >= endIndex)
                break;
        }

        return lista;
    }

    public List<Tema> assignmentsSublist(int startIndex, int endIndex, Predicate<Tema> filter) {
        List<Tema> lista = new ArrayList<>();

        List<Tema> listaInitiala = new ArrayList<>();
        getAllTeme().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);

        int i = 0;
        for (Tema t : listaInitiala) {
            if (i >= startIndex && i < endIndex)
                lista.add(t);
            i++;
            if (i >= endIndex)
                break;
        }
        return lista;
    }

    public List<Nota> gradesSublist(int startIndex, int endIndex, Predicate<Nota> filter) {
        List<Nota> lista = new ArrayList<>();
        
        List<Nota> listaInitiala = new ArrayList<>();
        getAllNote().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);

        int i = 0;
        for (Nota n : listaInitiala) {
            if (i >= startIndex && i < endIndex)
                lista.add(n);
            i++;
            if (i >= endIndex)
                break;
        }
        return lista;
    }

    public List<Nota> gradesSublist(int startIndex, int endIndex, Predicate<Nota> filter, String email) {
        List<Nota> lista = new ArrayList<>();

        List<Nota> listaInitiala = new ArrayList<>();
        for (Nota n : getAllNote())
            if (Objects.equals(n.getEmailStudent(), email))
                listaInitiala.add(n);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);

        int i = 0;
        for (Nota n : listaInitiala) {
            if (i >= startIndex && i < endIndex)
                lista.add(n);
            i++;
            if (i >= endIndex)
                break;
        }
        return lista;
    }

    public List<Nota> gradesOfStudent(int startIndex, int endIndex, Predicate<Tema> filter, Student student) {
        List<Nota> lista = new ArrayList<>();

        List<Tema> listaInitiala = new ArrayList<>();
        getAllTeme().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);

        int i = 0;
        for (Tema tema : listaInitiala) {
            if (i >= startIndex && i < endIndex) {
                String id = student.getId().toString() + "_" + tema.getId().toString();
                Optional<Nota> notaOptional = findNota(id);
                if (notaOptional.isPresent())
                    lista.add(notaOptional.get());
                else
                    lista.add(new Nota(id, student, tema, 0));
            }
            i++;
            if (i >= endIndex)
                break;
        }
        return lista;
    }

    public List<Nota> gradesOfAssignment(int startIndex, int endIndex, Predicate<Student> filter, Tema tema) {
        List<Nota> lista = new ArrayList<>();

        List<Student> listaInitiala = new ArrayList<>();
        getAllStudenti().forEach(listaInitiala::add);
        listaInitiala = Filtrare.filtrare(listaInitiala, filter);

        int i = 0;
        for (Student student : listaInitiala) {
            if (i >= startIndex && i < endIndex) {
                String id = student.getId().toString() + "_" + tema.getId().toString();
                Optional<Nota> notaOptional = findNota(id);
                if (notaOptional.isPresent())
                    lista.add(notaOptional.get());
                else
                    lista.add(new Nota(id, student, tema, 0));
            }
            i++;
            if (i >= endIndex)
                break;
        }
        return lista;
    }
}

package services;

import entities.*;
import exceptions.AbstractValidatorException;
import exceptions.TemaServiceException;
import exceptions.TemaValidatorException;
import observer_utils.AbstractObservable;
import observer_utils.TemaEvent;
import observer_utils.TemaEventType;
import repository.AbstractCrudRepo;
import repository.StudentRepository;
import validator.TemaValidator;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TemaService extends AbstractObservable<TemaEvent> {
    private AbstractCrudRepo<Tema, String> repository;
    private AbstractCrudRepo<Student, String> studentRepository;
    private TemaValidator validator;

    /**
     * Consturctor al clasei TemaService
     *
     * @param repository TemaRepository
     * @param validator  TemaValidator
     */
    public TemaService(AbstractCrudRepo<Tema, String> repository, AbstractCrudRepo<Student, String> studentRepository, TemaValidator validator) {
        this.repository = repository;
        this.validator = validator;
        this.studentRepository = studentRepository;
    }

    /**
     * Salveaza o tema in repository
     *
     * @param cerinta Tema -> Tema pe care dorim sa o salvam in repository
     * @return Tema -> entitatea ce tocmai a fost salvata in repository
     * @throws AbstractValidatorException Arunca aexceptie daca tema nu indeplineste conditiile de validare
     */
    public Tema addTema(String cerinta, Integer termenPredare) throws TemaValidatorException, TemaServiceException, IOException {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString().replace("-", "");
        } while (repository.findOne(uuid).isPresent());
        Tema tema = new Tema(uuid, cerinta, termenPredare);
        //prima data validam tema
        validator.validate(tema);
        //daca totul este ok il salvam in repo
        repository.save(tema);
        notifyAll(new TemaEvent(TemaEventType.ADD, tema));
        //acum trimitem un mail la toti studentii
        ArrayList<Student> studenti = (ArrayList<Student>) studentRepository.findAll();
        String subiectMail = "S-a adaugat o tema noua.";
        String bodyMesaj = "Cerinta: " + tema.getCerinta() + "\nDeadline saptamana: " + tema.getTermenPredare();
        studenti.forEach(x -> {
            try {
                this.sendMailToStudent(x.getEmail(), subiectMail, bodyMesaj);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        return tema;
    }

    public AbstractCrudRepo<Tema, String> getRepository() {
        return repository;
    }

    /**
     * Sterge uo tema din repository
     *
     * @param tema Tema
     * @return Tema -> Returneaza tema ce tocmai a fost stearsa
     * @throws AbstractValidatorException Arunca aexceptie daca tema nu indeplineste conditiile de validare
     */
    public Tema deleteTema(Tema tema) throws TemaValidatorException, TemaServiceException, IOException {
        validator.validate(tema);
        if (findOneTemaById(tema.getId()) == null) {
            throw new TemaServiceException("Nu exista aceasta tema in repository.");
        }
        repository.delete(tema.getId());
        notifyAll(new TemaEvent(TemaEventType.REMOVE, tema));
        return tema;
    }

    /**
     * Cauta o tema in repository pe baza id-ului
     *
     * @return Tema -> tema ce are id-ul introdus
     */
    public Tema findOneTemaById(String id) throws TemaServiceException {
        Optional<Tema> temaOptional = repository.findOne(id);
        if (!temaOptional.isPresent()) {
            throw new TemaServiceException("Nu exista tema cu acest id.");
        }
        Tema tema = temaOptional.get();
        return tema;
    }

    /**
     * Returneaza o lista cu toate temele din repository
     *
     * @return ArrayList<Tema> -> toate temele din repository
     */
    public ArrayList<Tema> findAllTeme() {
        return new ArrayList<>((Collection<Tema>) repository.findAll());
    }

    /**
     * Returneaza numarul total de teme din repository
     *
     * @return int -> numarul de teme din repository
     */
    public int numarTeme() {
        return (int) repository.size();
    }

    /**
     * Actualizeaza termenl de predare al unei teme
     *
     * @param id        - Integer=->idul temei pe care dorim sa il modificam
     * @param termenNou Integer -> numarul saptamanii in care predam
     * @return Returneaza tema modificata
     * @throws TemaServiceException Daca nu exista in fisier tema
     * @throws IOException          Daca nu exista in fisier tema
     */
    public Tema actualizeazaTermenulDePredare(String id, Integer termenNou) throws TemaServiceException, IOException {
        Tema tema = findOneTemaById(id);
        if (tema.getTermenPredare() <= Cronos.getSaptamanActuala()) {
            throw new TemaServiceException("Tema poate fi prelungita doar inainte de termenul de predare!");
        }
        if (termenNou <= tema.getTermenPredare()) {
            throw new TemaServiceException("Termenul de predare trebuie sa fie mai mare decat cel deja stabilit.");
        }
        if (termenNou <= 1 || termenNou > 14) {
            throw new TemaServiceException("Termenul nou trebuie sa fie cuprins intre 2 si 14.");
        }
        Tema temaNoua = new Tema(id, tema.getCerinta(), termenNou);
        repository.update(id, temaNoua);
        notifyAll(new TemaEvent(TemaEventType.PRELUNGIRE, temaNoua));
        //trimitem mail la toti studentii
        String subiect = "Actualizare termen tema";
        String mesajBody = "S-a actualizat termenul de predare al temei cu cerinta: \"" + tema.getCerinta() +
                "\" de la saptamana: " + tema.getTermenPredare() + " la saptamana: " + termenNou;
        ArrayList<Student> students = (ArrayList<Student>) studentRepository.findAll();
        students.forEach(s -> {
            try {
                this.sendMailToStudent(s.getEmail(), subiect, mesajBody);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        });
        return temaNoua;
    }

    /**
     * Filtreaza temele care sa contine fragmentul introdus in cerinta
     *
     * @param sample String->fragment de text pe baza caruia se cauta
     * @return Optional-> container cu elementele care s-au potrivit
     */
    public Optional<List<Tema>> filtreazaCerinta(String sample) {
        List<Tema> teme = new ArrayList<Tema>((Collection<? extends Tema>) repository.findAll());
        List<Tema> result;
        Predicate<Tema> predicate = (p) -> {
            return p.getCerinta().contains(sample);
        };
        Filter filter = new FilterImpl();
        result = filter.defaultFilterAndSort(teme, predicate, (t1, t2) -> {
            return t1.getId().compareTo(t2.getId());
        });
        return Optional.ofNullable(result);
    }

    /**
     * Filtreaza temele cu termenul de predare dat si le sorteaza crescator pe baza id-ului
     *
     * @param termenPredare Integer->fragment de text pe baza caruia se cauta
     * @return Optional-> container cu elementele care s-au potrivit
     */
    public Optional<List<Tema>> filtreazaTermenPredare(Integer termenPredare) {
        List<Tema> teme = new ArrayList<Tema>((Collection<? extends Tema>) repository.findAll());
        List<Tema> result;
        Predicate<Tema> predicate = (t) -> {
            return t.getTermenPredare().equals(termenPredare);
        };
        Filter filter = new FilterImpl();
        Comparator<Tema> comparator = (t1, t2) -> {
            return t1.getId().compareTo(t2.getId());
        };
        result = filter.defaultFilter(teme, predicate);
        result.sort(comparator);
        return Optional.ofNullable(result);
    }

    /**
     * Filtreaza temele cu termenul de predare dat si le sorteaza crescator pe baza id-ului
     *
     * @param termenPredare Integer->fragment de text pe baza caruia se cauta
     * @return Optional-> container cu elementele care s-au potrivit
     */
    public Optional<List<Tema>> filtreazaTermenPredareSiCerinta(String sample, Integer termenPredare) {
        List<Tema> teme = new ArrayList<Tema>((Collection<? extends Tema>) repository.findAll());
        List<Tema> result;
        Predicate<Tema> predicate = (t) -> {
            return t.getTermenPredare().equals(termenPredare) &&
                    t.getCerinta().contains(sample);
        };
        Filter filter = new FilterImpl();
        Comparator<Tema> comparator = (t1, t2) -> {
            return t1.getId().compareTo(t2.getId());
        };
        result = filter.defaultFilterAndSort(teme, predicate, Tema::compareToId);
        return Optional.ofNullable(result);
    }

    /**
     * Trimite un mesaj la un student
     *
     * @param studentAddress - String - mail-ul studentului
     * @param subject        - Subiectul mesajului
     * @param messageBody    - Continutul mesajului
     * @throws MessagingException - Arunca exceptie daca apare vreo problema in timpul trimiterii
     */
    private void sendMailToStudent(String studentAddress, String subject, String messageBody) throws MessagingException {
        String username = MailCredentials.getApplicationEmail();
        String password = MailCredentials.getApplicationPassword();
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(studentAddress));
        message.setSubject(subject);
        message.setText(messageBody);
        Transport.send(message);
    }

}

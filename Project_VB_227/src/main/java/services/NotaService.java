package services;

import com.sun.nio.zipfs.ZipPath;
import entities.*;
import exceptions.AbstractValidatorException;
import exceptions.NotaServiceException;
import observer_utils.AbstractObservable;
import observer_utils.NotaEvent;
import observer_utils.NotaEventType;
import repository.AbstractCrudRepo;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;
import validator.NotaValidator;
import vos.NotaVO;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.xml.bind.ValidationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class NotaService extends AbstractObservable<NotaEvent> {
    private AbstractCrudRepo<Nota, NotaPk> repository;
    private AbstractCrudRepo<Student, String> studentiRepo;
    private AbstractCrudRepo<Tema, String> temeRepo;
    private NotaValidator validator;

    /**
     * Constructor al clasei NotaService
     *
     * @param repository   - Un repository de note
     * @param validator    - validator pentru note
     * @param studentiRepo - repository cu studenti
     * @param temeRepo     - repository cu teme
     */
    public NotaService(AbstractCrudRepo<Nota, NotaPk> repository, NotaValidator validator,
                       AbstractCrudRepo<Student, String> studentiRepo, AbstractCrudRepo<Tema, String> temeRepo) {
        this.repository = repository;
        this.studentiRepo = studentiRepo;
        this.temeRepo = temeRepo;
        this.validator = validator;
    }

    public void setStudentiRepo(AbstractCrudRepo<Student, String> studentiRepo) {
        this.studentiRepo = studentiRepo;
    }

    public void setTemeRepo(AbstractCrudRepo<Tema, String> temeRepo) {
        this.temeRepo = temeRepo;
    }

    /**
     * Adauga o nota la o tema pentru un student in repository
     *
     * @param idTema      - Nota - nota concreta a studentului la o tema
     * @param nrSaptamana - numarul saptamanii in care a predat studentul tema
     * @param observatii  - eventuale observatii pentru notare
     * @throws AbstractValidatorException Daca nu este valabila nota
     * @throws ValidationException        Daca nu este valabila nota
     * @throws NotaServiceException       Daca a fost deja notat la aceasta tema studentul
     * @throws IOException                Daca nu gaseste fisierul personal al studentului
     */
    public void addNota(String idStudent, String idTema, int nrSaptamana, int valoareNota, String observatii) throws AbstractValidatorException, ValidationException, NotaServiceException, IOException, MessagingException {
        if (nrSaptamana < 1 || nrSaptamana > 14) {
            throw new NotaServiceException("Introduceti o saptamana intre 1 si 14.");
        }
        Nota nota = new Nota(idStudent, idTema, valoareNota);
        validator.validate(nota);
//        ArrayList<Nota> note = new ArrayList<Nota>((Collection<? extends Nota>) repository.findAll());
//        for (Nota n : note) {
//            if (n.getNrTemei() == nota.getNrTemei() && n.getIdStudent() == n.getIdStudent()) {
//                throw new NotaServiceException("Studentul a fost deja notat la aceasta materie");
//            }
//        }
//        if (repository.findOne(nota.getId()).isPresent()) {
//            throw new NotaServiceException("Studentul a fost deja notat la aceasta materie");
//        }
        List<Nota> note = (List<Nota>) repository.findAll();
        if (note.stream().filter(x -> {
            return x.getIdTema().equals(idTema) && x.getIdStudent().equals(idStudent);
        }).collect(Collectors.toList()).size() != 0) {
            throw new NotaServiceException("Studentul a fost deja notat la aceasta materie");
        }
        Tema tema;
        Optional<Tema> temaOptional = temeRepo.findOne(idTema);
        if (!temaOptional.isPresent()) {
            throw new NotaServiceException("Nu exista aceasta tema!");
        } else {
            tema = temaOptional.get();
        }
        if (nrSaptamana - tema.getTermenPredare() == 1) {
            nota.setValoare(valoareNota - 2);
        } else if (nrSaptamana - tema.getTermenPredare() == 2) {
            nota.setValoare(valoareNota - 4);
        } else if (nrSaptamana - tema.getTermenPredare() > 2) {
            nota.setValoare(1);
        }
        if (nota.getValoare() < 1) {
            nota.setValoare(1);
        }
        //scriem in fisierul personal al studentului inforamtii cu privire la nota
        if (observatii.equals("")) {
            observatii = "nu sunt observatii";
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("src\\main\\resources\\" + nota.getId().getIdStudent() + ".txt", true));
        String line = "Adaugare nota pentru tema " + nota.getId().getIdTema() + " : " + nota.getValoare() + " avand " +
                "deadline saptamana " + tema.getTermenPredare() + " cu urmatoarele observatii :" + observatii + ".\n";
        bw.write(line);
        bw.close();
        //salvam si in repo tema introdusa
        Student student = studentiRepo.findOne(nota.getId().getIdStudent()).get();
        Tema temaa = temeRepo.findOne(nota.getId().getIdTema()).get();
        NotaVO notaVo = new NotaVO(nota.getId().getIdStudent(), nota.getId().getIdTema(), nota.getValoare(),
                student.getNume(), temaa.getCerinta());
        notifyAll(new NotaEvent(NotaEventType.ADD, notaVo));
        repository.save(nota);
        //trmitem prin mail fisierul
        String studentFilePath = "src\\main\\resources\\" + nota.getId().getIdStudent() + ".txt";
        String subject = "Adaugare nota";
        String bodyText = "S-a adaugat nota corespunzatoare temei: \"" + tema.getCerinta() + "\" " + "avand valoarea " + nota.getValoare();
        this.sendMailWithAttachment(studentiRepo.findOne(nota.getIdStudent()).get().getEmail(), subject, bodyText, studentFilePath);
    }

    public AbstractCrudRepo<Nota, NotaPk> getRepository() {
        return repository;
    }

    public List<NotaVO> findAllNote() {
        List<Nota> note = (List<Nota>) repository.findAll();
        List<NotaVO> result = new ArrayList<>();
        note.forEach(n -> {
            result.add(new NotaVO(n.getIdStudent(), n.getIdTema(), n.getValoare(), studentiRepo.findOne(n.getIdStudent()).get().getNume(), temeRepo.findOne(n.getIdTema()).get().getCerinta()));
        });
        return result;

    }

    public void stergeToateNotelePentruUnStudent(String idStudent) {
        List<Nota> note = (List<Nota>) repository.findAll();
        note.forEach(n -> {
            if (n.getIdStudent().equals(idStudent)) {
                try {
                    repository.delete(new NotaPk(idStudent, n.getIdTema()));
                    notifyAll(new NotaEvent(NotaEventType.REMOVE, new NotaVO(n.getIdStudent(), n.getIdTema(), n.getValoare(), "", "")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void stergeToateNotelePentruOTema(String temaId) {
        List<Nota> note = (List<Nota>) repository.findAll();
        note.forEach(n -> {
            if (n.getIdTema().equals(temaId)) {
                try {
                    repository.delete(new NotaPk(n.getIdStudent(), temaId));
                    notifyAll(new NotaEvent(NotaEventType.REMOVE, new NotaVO(n.getIdStudent(), n.getIdTema(), n.getValoare(), "", "")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Modifica o nota adaugata deja.In final va ramane nota cea mai mare in repository
     *
     * @param notaNoua    Nota -> noua nota dupa modificare
     * @param nrSaptamana int -> numarul saptamanii in care a fost modificata nota
     * @throws NotaServiceException       Daca nu a fost notat anterior la aceasta tema studentul
     * @throws AbstractValidatorException Daca nu a fost introdusa o nota valida
     * @throws ValidationException        Daca nu a fost introdusa o nota valida
     * @throws IOException                Daca fisierul elevului nu a fost gasit
     */
    public void updateNota(Nota notaNoua, int nrSaptamana) throws NotaServiceException, AbstractValidatorException, ValidationException, IOException, MessagingException {
        List<Nota> note = (List<Nota>) repository.findAll();
        note = note.stream().filter(x -> {
            return x.getIdTema().equals(notaNoua.getIdTema()) && x.getIdStudent().equals(notaNoua.getIdStudent());
        }).collect(Collectors.toList());
        if (note.size() == 0) {
            throw new NotaServiceException("Studentul nu a mai fost notat la aceasta materie");
        }
        Nota oldNota = note.get(0);
        validator.validate(notaNoua);
        if (nrSaptamana < 1 || nrSaptamana > 14) {
            throw new NotaServiceException("Introduceti o saptamana intre 1 si 14.");
        }
        String comentarii = "";
        Optional<Tema> temaOptional = temeRepo.findOne(notaNoua.getId().getIdTema());
        if (!temaOptional.isPresent()) {
            throw new NotaServiceException("Nu exista nicio astfel de tema!");
        }
        Tema tema = temaOptional.get();
        if (nrSaptamana - tema.getTermenPredare() == 1) {
            notaNoua.setValoare(notaNoua.getValoare() - 2);
            comentarii = "Intarziere cu o saptamana";
        } else if (nrSaptamana - tema.getTermenPredare() == 2) {
            notaNoua.setValoare(notaNoua.getValoare() - 4);
            comentarii = "Intarziere cu doua saptamani";
        } else if (nrSaptamana - tema.getTermenPredare() > 2) {
            notaNoua.setValoare(1);
            comentarii = "Depasire termen limita";
        }
        if (oldNota.getValoare() >= notaNoua.getValoare()) {
            throw new NotaServiceException("Nota noua e mai mica decat cea veche");
        }
        //Scriem in fisier modificarile
        String studentFilePath = "src\\main\\resources\\" + notaNoua.getId().getIdStudent() + ".txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(studentFilePath, true));
        String line = "Modificare nota pentru tema: " + notaNoua.getId().getIdTema() + " avand noua nota nota: " + notaNoua.getValoare() +
                "cu deadline: " + tema.getTermenPredare() + " iar saptamana in care a fost predata tema este: " + nrSaptamana +
                " comentarii: " + comentarii + "\n";
        bw.write(line);
        bw.close();
        String subject = "Modificare nota";
        String bodyText = "S-a modificat nota corespunzatoare temei: \"" + tema.getCerinta() + "\" de la: " + oldNota.getValoare() +
                " la: " + notaNoua.getValoare();
        this.sendMailWithAttachment(studentiRepo.findOne(oldNota.getIdStudent()).get().getEmail(), subject, bodyText, studentFilePath);
        repository.update(notaNoua.getId(), notaNoua);
        notifyAll(new NotaEvent(NotaEventType.UPDATE, new NotaVO(notaNoua.getIdStudent(), notaNoua.getIdTema(), notaNoua.getValoare(),
                "", tema.getCerinta())));

    }

    /**
     * Sterge o nota din repository
     *
     * @param nota Nota - nota pe care dorim sa o stergem
     * @throws NotaServiceException - nu exista nota introdusa
     * @throws IOException          - Nu a fost gasit fisierul de note
     */
    public void deleteNota(Nota nota) throws NotaServiceException, IOException {
        if (repository.findOne(nota.getId()) == null) {
            throw new NotaServiceException("Nu exista nicio astfel de nota");
        }
        notifyAll(new NotaEvent(NotaEventType.REMOVE, new NotaVO(nota.getIdStudent(), nota.getIdTema(), 0, "", "")));
        repository.delete(nota.getId());
    }

    /**
     * Filtreaza toate notele cu valoarea introdusa
     *
     * @param valoare Integer-valoarea notei
     * @return O lista cu NoteVo
     */
    public Optional<List<NotaVO>> filtreazaPentruValoare(Integer valoare) {
        List<Nota> note = new ArrayList<Nota>((Collection<? extends Nota>) repository.findAll());
        List<Nota> result;
        Predicate<Nota> predicate = (n) -> {
            return n.getValoare() == valoare;
        };
        Filter filter = new FilterImpl();
        result = filter.defaultFilter(note, predicate);
        result.sort(new Comparator<Nota>() {
            @Override
            public int compare(Nota o1, Nota o2) {
                return o1.getId().getIdStudent().compareTo(o2.getId().getIdStudent());
            }
        });
        List<NotaVO> noteVos = new ArrayList<NotaVO>();
        for (Nota nota : result) {
            Student student = studentiRepo.findOne(nota.getId().getIdStudent()).get();
            Tema tema = temeRepo.findOne(nota.getId().getIdTema()).get();
            NotaVO notaVo = new NotaVO(nota.getId().getIdStudent(), nota.getId().getIdTema(), nota.getValoare(),
                    student.getNume(), tema.getCerinta());
            noteVos.add(notaVo);
        }
        return Optional.ofNullable(noteVos);
    }

    /**
     * Filtreaza toate notele pentru studentul cu numele dat
     *
     * @param nume String-numele studentului
     * @return O lista cu NoteVo
     */
    public Optional<List<NotaVO>> filtreazaNotelePentruUnStudent(String nume) {
        List<Nota> note = new ArrayList<Nota>((Collection<? extends Nota>) repository.findAll());
        List<Nota> result;
        Predicate<Nota> predicate = (n) -> {
            return studentiRepo.findOne(n.getId().getIdStudent()).get().getNume().equals(nume);
        };
        Filter filter = new FilterImpl();
        result = filter.defaultFilter(note, predicate);
        result.sort(new Comparator<Nota>() {
            @Override
            public int compare(Nota o1, Nota o2) {
                return o1.getId().getIdStudent().compareTo(o2.getId().getIdStudent());
            }
        });
        List<NotaVO> noteVos = new ArrayList<NotaVO>();
        for (Nota nota : result) {
            Student student = studentiRepo.findOne(nota.getId().getIdStudent()).get();
            Tema tema = temeRepo.findOne(nota.getId().getIdTema()).get();
            NotaVO notaVo = new NotaVO(nota.getId().getIdStudent(), nota.getId().getIdTema(), nota.getValoare(),
                    student.getNume(), tema.getCerinta());
            noteVos.add(notaVo);
        }
        return Optional.ofNullable(noteVos);
    }

    /**
     * Filtreaza toate notele pentru o tema
     *
     * @param idTema - in - id-ul temei cautate
     * @return Returneaza o lista ce contine toate noele posibile
     */
    public Optional<List<NotaVO>> filtreazaNotelePentruOTema(Tema idTema) {
        List<Nota> result;
        List<Nota> notele = new ArrayList<Nota>((Collection<? extends Nota>) repository.findAll());
        Predicate<Nota> predicate = (n) -> {
            return n.getId().getIdTema().equals(idTema);
        };
        Filter filter = new FilterImpl();
        result = filter.defaultFilter(notele, predicate);
        List<NotaVO> resultVo = new ArrayList<>();
        for (Nota nota : result) {
            Student student = studentiRepo.findOne(nota.getId().getIdStudent()).get();
            Tema tema = temeRepo.findOne(nota.getId().getIdTema()).get();
            NotaVO notaVo = new NotaVO(nota.getId().getIdStudent(), nota.getId().getIdTema(), nota.getValoare(),
                    student.getNume(), tema.getCerinta());
            resultVo.add(notaVo);
        }
        return Optional.ofNullable(resultVo);

    }

    public Optional<List<NotaVO>> filtreazaNotelePentruCerinta(String sample) throws NotaServiceException {
        List<Nota> result;
        Predicate<Nota> pred = (n) -> {
            return temeRepo.findOne(n.getIdTema()).get().getCerinta().contains(sample);
        };
        Filter filter = new FilterImpl();
        result = filter.defaultFilter((List<Nota>) repository.findAll(), pred);
        List<NotaVO> resultVo = new ArrayList<>();
        for (Nota nota : result) {
            Student student = studentiRepo.findOne(nota.getId().getIdStudent()).get();
            Tema tema = temeRepo.findOne(nota.getId().getIdTema()).get();
            NotaVO notaVo = new NotaVO(nota.getId().getIdStudent(), nota.getId().getIdTema(), nota.getValoare(),
                    student.getNume(), tema.getCerinta());
            resultVo.add(notaVo);
        }
        return Optional.ofNullable(resultVo);

    }

    public Optional<List<NotaVO>> filtreazaNotelePentruCerintaSiNota(String sample, Integer valoareNota) throws NotaServiceException {
        List<Nota> result;
        Predicate<Nota> pred = (n) -> {
            return temeRepo.findOne(n.getIdTema()).get().getCerinta().contains(sample) &&
                    n.getValoare() == valoareNota;
        };
        Filter filter = new FilterImpl();
        result = filter.defaultFilter((List<Nota>) repository.findAll(), pred);
        List<NotaVO> resultVo = new ArrayList<>();
        for (Nota nota : result) {
            Student student = studentiRepo.findOne(nota.getId().getIdStudent()).get();
            Tema tema = temeRepo.findOne(nota.getId().getIdTema()).get();
            NotaVO notaVo = new NotaVO(nota.getId().getIdStudent(), nota.getId().getIdTema(), nota.getValoare(),
                    student.getNume(), tema.getCerinta());
            resultVo.add(notaVo);
        }
        return Optional.ofNullable(resultVo);

    }

    public Optional<List<NotaVO>> filtreazaNotelePentruStudentSiNota(String sample, Integer valoareNota) throws NotaServiceException {
        List<Nota> result;
        Predicate<Nota> pred = (n) -> {
            return studentiRepo.findOne(n.getIdStudent()).get().getNume().contains(sample) &&
                    n.getValoare() == valoareNota;
        };
        Filter filter = new FilterImpl();
        result = filter.defaultFilter((List<Nota>) repository.findAll(), pred);
        List<NotaVO> resultVo = new ArrayList<>();
        for (Nota nota : result) {
            Student student = studentiRepo.findOne(nota.getId().getIdStudent()).get();
            Tema tema = temeRepo.findOne(nota.getId().getIdTema()).get();
            NotaVO notaVo = new NotaVO(nota.getId().getIdStudent(), nota.getId().getIdTema(), nota.getValoare(),
                    student.getNume(), tema.getCerinta());
            resultVo.add(notaVo);
        }
        return Optional.ofNullable(resultVo);

    }

    public Optional<List<NotaVO>> filtreazaNotelePentruStudentSiCerinta(String sampleNume, String sampleCerinta) throws NotaServiceException {
        List<Nota> result;
        Predicate<Nota> pred = (n) -> {
            return studentiRepo.findOne(n.getIdStudent()).get().getNume().contains(sampleNume) &&
                    temeRepo.findOne(n.getIdTema()).get().getCerinta().contains(sampleCerinta);
        };
        Filter filter = new FilterImpl();
        result = filter.defaultFilter((List<Nota>) repository.findAll(), pred);
        List<NotaVO> resultVo = new ArrayList<>();
        for (Nota nota : result) {
            Student student = studentiRepo.findOne(nota.getId().getIdStudent()).get();
            Tema tema = temeRepo.findOne(nota.getId().getIdTema()).get();
            NotaVO notaVo = new NotaVO(nota.getId().getIdStudent(), nota.getId().getIdTema(), nota.getValoare(),
                    student.getNume(), tema.getCerinta());
            resultVo.add(notaVo);
        }
        return Optional.ofNullable(resultVo);

    }

    public Optional<List<NotaVO>> filtreazaPentruToateCriteriile(String sampleNume, String sampleCerinta, Integer valoareNota) throws NotaServiceException {
        List<Nota> result;
        Predicate<Nota> pred = (n) -> {
            return studentiRepo.findOne(n.getIdStudent()).get().getNume().contains(sampleNume) &&
                    temeRepo.findOne(n.getIdTema()).get().getCerinta().contains(sampleCerinta) &&
                    n.getValoare() == valoareNota;
        };
        Filter filter = new FilterImpl();
        result = filter.defaultFilter((List<Nota>) repository.findAll(), pred);
        List<NotaVO> resultVo = new ArrayList<>();
        for (Nota nota : result) {
            Student student = studentiRepo.findOne(nota.getId().getIdStudent()).get();
            Tema tema = temeRepo.findOne(nota.getId().getIdTema()).get();
            NotaVO notaVo = new NotaVO(nota.getId().getIdStudent(), nota.getId().getIdTema(), nota.getValoare(),
                    student.getNume(), tema.getCerinta());
            resultVo.add(notaVo);
        }
        return Optional.ofNullable(resultVo);

    }

    private void sendMailWithAttachment(String studentAddress, String subject, String bodyText, String filePath) throws MessagingException, IOException {
        String username = MailCredentials.getApplicationEmail();
        String password = MailCredentials.getApplicationPassword();
        File file = new File(filePath);
        String fileName = file.getName();

//        String fileNameWithoutExtension = fileName.split(".txt")[0];
//        File zipFile = new File("src\\main\\resources\\" + fileNameWithoutExtension + ".zip");
//        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));
//        ZipEntry entry = new ZipEntry(fileName);
//        out.putNextEntry(entry);
//        Path path = Paths.get(filePath);
//        byte[] data = Files.readAllBytes(path);
//        out.write(data,0,data.length);
//        out.closeEntry();

        DataSource source = new FileDataSource(file);
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
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(bodyText);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(studentAddress));
        message.setSubject(subject);
        message.setContent(multipart);
        Transport.send(message);
    }
}

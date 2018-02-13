package Service;

import Domain.Nota;
import Domain.NotaID;
import Domain.Student;
import Domain.Tema;
import Repository.IRepository;
import Repository.RepositoryException;
import Util.ListEvent;
import Util.ListEventType;
import Util.Observable;
import Util.Observer;
import Validator.ValidationException;
import ViewFXML.CryptoUtil;
import com.sun.mail.smtp.SMTPSendFailedException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static Util.Util.toList;

public class NotaService implements Observable<Nota>{
    private CryptoUtil cryptoUtil= new CryptoUtil();
    private IRepository<NotaID, Nota> repoNota;
    private IRepository<Integer, Tema> repoTema;
    private  IRepository<Integer,Student> repoStudent;
    ArrayList<Observer<Nota>> notaObservers=new ArrayList<>();

    public NotaService(IRepository<NotaID,Nota> repoNota,IRepository<Integer, Tema> repoTema,IRepository<Integer,Student> repoStudent){
        this.repoNota=repoNota;
        this.repoTema=repoTema;
        this.repoStudent=repoStudent;
    }

    @Override
    public void addObserver(Util.Observer<Nota> o) {
        notaObservers.add(o);
    }

    @Override
    public void removeObserver(Observer<Nota> o) {
        notaObservers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Nota> event) {
        notaObservers.forEach(x->x.notifyEvent(event));
    }

    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l){
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getList() {
                return l;
            }
            @Override
            public E getElement() {
                return elem;
            }
        };
    }

    public List<Nota> getAllN(){
        return StreamSupport.stream(repoNota.getAll().spliterator(),false).collect(Collectors.toList());
    }


    public void send(String file,String receiver){
        final String username = "laurascurtu8751015@gmail.com";
        try {
            String key = "ezeon8547";
            String password=cryptoUtil.decrypt(key, "DGrHsjwL+xQ4kaS/G8EzUw==");

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
            String text=readFile(file);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("laurascurtu8751015@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(receiver));
            message.setSubject("GRADES");
            message.setText(text);
            Transport.send(message);
        }
        catch (IOException ioe){
            System.err.println("ERR" + ioe);
        }
        catch (SendFailedException sendFailedException) {
            Address failedAddresses[] = sendFailedException.getInvalidAddresses();
            StringBuilder errorMessage = new StringBuilder();
            if (failedAddresses != null) {
                errorMessage.append("The following mail addresses are invalid:").append("\n");
                for (Address address : failedAddresses) {
                    errorMessage.append(address.toString()).append("\n");
                }
            }
            Address validUnsentAddresses[] = sendFailedException.getValidUnsentAddresses();
            if (validUnsentAddresses != null) {
                errorMessage.append("No mail has been sent to the following valid addresses:").append("\n");
                for (Address address : validUnsentAddresses) {
                    errorMessage.append(address.toString()).append("\n");
                }
            }
            System.out.println(sendFailedException);
        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch(InvalidKeySpecException e){
            e.printStackTrace();
        }catch (NoSuchPaddingException e){
            e.printStackTrace();
        }catch (InvalidKeyException e){
            e.printStackTrace();
        }catch(InvalidAlgorithmParameterException e){
            e.printStackTrace();
        }catch(IllegalBlockSizeException e){
            e.printStackTrace();
        }catch(BadPaddingException e){
            e.printStackTrace();
        }

    }

    public void addNota(int idStudent,int nrTema,double valoare,int saptamanaPredare,String obs){
        try{
            String fileName,receiver;
        Student stud=repoStudent.findOne(idStudent);
        Tema tema=repoTema.findOne(nrTema);
        NotaID idNota=new NotaID(idStudent,nrTema);
        if(saptamanaPredare>repoTema.findOne(nrTema).getDeadline())
            if(saptamanaPredare-repoTema.findOne(nrTema).getDeadline()<=0)
                valoare=0;
            else
                valoare=valoare-(saptamanaPredare-repoTema.findOne(nrTema).getDeadline());
        Nota nota=new Nota(idStudent,nrTema,idNota,valoare);
        repoNota.save(nota);
            ListEvent<Nota> ev = createEvent(ListEventType.REMOVE, nota, repoNota.getAll());
            notifyObservers(ev);
        if(obs=="") {
            obs = "NONE";
            writeToFileStudent("Adaugare nota", idStudent, nrTema, valoare, repoTema.findOne(nrTema).getDeadline(), saptamanaPredare, obs);
        }
            fileName=""+nota.getIdStudent()+".txt";
            receiver=repoStudent.findOne(nota.getIdStudent()).getEmail();
            Thread thread = new Thread(){
                public void run(){
                    send(fileName,receiver);
                }
            };
            thread.start();
        }
        catch(RepositoryException e){
            throw e;
        }
        catch(RuntimeException e){
            throw e;
        }

    }

    public void writeToFileStudent(String inceput,int idStudent,int nrTema, double nota,int deadline,int saptamanaPredare,String obs){
        String fileName=""+idStudent+".txt";
        try (FileWriter fw = new FileWriter(fileName,true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out= new PrintWriter(bw))
        {
            String l = inceput+" Tema: "+nrTema+" Nota: "+nota+" Deadline: "+deadline+" Saptamana predarii: "+saptamanaPredare;
            String obser="Observatii Tema "+nrTema+" : "+obs;
            out.println(l);
            out.println(obser);
        } catch (IOException e) {
            System.err.println("ERR" + e);
        }
    }

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader (file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    public int data_inceput_facultate(){
        String data_inc_facultate = "20171001";
        String format = "yyyyMMdd";

        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        try {
            Date date = df.parse(data_inc_facultate);
            cal.setTime(date);
            int week = cal.get(Calendar.WEEK_OF_YEAR);
            return week;
            //System.out.println("A inceput facultatea in saptamana "+week);
        }catch(ParseException e){
            //System.out.println(e);
        }
        return 0;
    }

    public int sapt_de_facultate_curenta(){
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int s_curr =  date.get(weekFields.weekOfWeekBasedYear());
        int sapt_de_fac = 52+s_curr - data_inceput_facultate() + 1;
        //System.out.println("SAptamana curenta de facultate este "+sapt_de_fac);
        return sapt_de_fac;

    }

    public Nota deleteNota(NotaID id){
        Nota nota=repoNota.delete(id);
        if(nota!=null){
            ListEvent<Nota> ev = createEvent(ListEventType.REMOVE,nota,repoNota.getAll());
            notifyObservers(ev);
        }
        return nota;
    }
    public void updateNota(int idStudent,int nrTema,double valoare,int saptamanaPredare,String obs){
        try {
            String fileName,receiver;
            NotaID idNota = new NotaID(idStudent, nrTema);
            if (saptamanaPredare > repoTema.findOne(nrTema).getDeadline()) {
                if (saptamanaPredare - repoTema.findOne(nrTema).getDeadline() <= 0)
                    valoare = 0;
                else
                    valoare = valoare - (saptamanaPredare - repoTema.findOne(nrTema).getDeadline());
            }
            //if (valoare < repoNota.findOne(idNota).getValoare())
            //else {
                //valoare = repoNota.findOne(idNota).getValoare();
            //}
            Nota nota = new Nota(idStudent, nrTema, idNota, valoare);
            repoNota.update(nota);
            ListEvent<Nota> ev = createEvent(ListEventType.UPDATE,nota,repoNota.getAll());
            notifyObservers(ev);
            writeToFileStudent("Modificare nota", idStudent, nrTema, valoare, repoTema.findOne(nrTema).getDeadline(), saptamanaPredare, obs);
            fileName=""+nota.getIdStudent()+".txt";
            receiver=repoStudent.findOne(nota.getIdStudent()).getEmail();
            Thread thread = new Thread(){
                public void run(){
                    send(fileName,receiver);
                }
            };
            thread.start();
        }
        catch(ValidationException e){
            throw e;
        }
        catch(RuntimeException e){
            throw e;
        }
    }

    public Student findStudent(int id){
        return repoStudent.findOne(id);
    }

    public Tema findTema(int id){
        return repoTema.findOne(id);
    }
    public boolean findStudentNull(int id){
        for(Student stud:repoStudent.getAll())
            if(stud.getID()==id)
                return true;
        return false;
    }

    public <E>List<E> filterAndSorter(List<E> lista, Predicate<E> pred, Comparator<E> comp){
        return lista.stream().filter(pred).sorted(comp).collect(Collectors.toList());
    }

    Comparator<Nota> compNotaIDCresc=(x,y)->x.compare(x,y);

    public List<Nota> filterNotaSpecificValue(double val,String comp){
        Predicate<Nota> pred=x->x.getValoare()==val;
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc.reversed());
        else if(comp.contentEquals("Cresc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc);
        else
            throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
    }

    public List<Nota> filterNotaTwoValues(double val1,double val2,String comp){
        Predicate<Nota> pred=x->(x.getValoare()>val1 && x.getValoare()<val2);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc.reversed());
        else if(comp.contentEquals("Cresc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc);
        else {
            throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
        }
    }

    public List<Nota> filterNotaIDStudent(int id,String comp){
        Predicate<Nota> pred=x->(x.getID().getIdStudent()==id);
        if(comp.contentEquals("Desc"))
            return filterAndSorter(toList(getAllN()),pred,compNotaIDCresc.reversed());
        else if(comp.contentEquals("Cresc")) {
            return filterAndSorter(toList(getAllN()), pred, compNotaIDCresc);
        } else throw new ValidationException("Trebuie sa alegeti dintre: Desc si Cresc!");
    }
}

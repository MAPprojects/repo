package Service;
import Domain.*;
import Repository.Repository;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.itextpdf.kernel.color.Lab;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import sample.FXExceptions;
import utils.*;


import utils.Observable;
import utils.Observer;
import java.util.*;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static jdk.nashorn.internal.objects.NativeMath.round;

public class Service extends java.util.Observable{
//    private static int currentweek=50+Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)-39;
    private static int currentweek=10;
    private Repository<Student,Integer> studRepo;
    private Repository<Tema,Integer> labRepo;
    private Repository<Nota,Integer> gradeRepo;
    private List<User> userii;
    public ArrayList<Observer<Student>> obsList;
    private HashMap<Integer,Integer> hardHome;
    private HashMap<Integer,Integer> easyHome;

    public Service(Repository<Student,Integer> studRepo, Repository<Tema,Integer> labRepo,Repository<Nota,Integer> gradeRepo){
        this.studRepo=studRepo;
        this.labRepo=labRepo;
        this.gradeRepo=gradeRepo;
        obsList=new ArrayList<>();
        try(InputStream input = new FileInputStream("useri.xml")){
            XMLInputFactory inputFactory=XMLInputFactory.newInstance();
            XMLStreamReader reader=inputFactory.createXMLStreamReader(input);
            userii=readFromXml(reader);
        }catch (IOException | XMLStreamException f){
            return;
        }
        hardHome=new HashMap<>();
        easyHome=new HashMap<>();
    }

    public void updateUser(String lastUser,String newUser){
        for(int i=0;i<userii.size();i++){
            if(userii.get(i).getUsername().equals(lastUser))
            {
                userii.get(i).setUsername(newUser);
                break;
            }
        }
    }

    public void deleteUser(String username){
        int index=-1;
        for(int i=0;i<userii.size();i++){
            if(userii.get(i).getUsername().equals(username))
            {
                index=i;
                break;
            }
        }
        if(index!=-1)
            userii.remove(index);
    }

    public List<User> getAllUsers(){
        return userii;
    }

    public void addUser(User us){
        userii.add(us);
    }

    private List<User> readFromXml(XMLStreamReader reader) throws XMLStreamException {
        List<User> totiUserii=new ArrayList<>();
        User us=null;
        while(reader.hasNext()){
            int event=reader.next();
            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    if (reader.getLocalName().equals("user")) {
                        us= createEntity(reader);
                        totiUserii.add(us);
                    }
                    break;
            }
        }
        return totiUserii;
    }

    protected User createEntity(XMLStreamReader reader) throws  XMLStreamException{
        String id=reader.getAttributeValue(null,"id");
        User us=new User();
        String currentPropertyValue="";
        while(reader.hasNext()){
            int event=reader.next();
            switch (event){
                case XMLStreamReader.END_ELEMENT:
                    if(reader.getLocalName().equals("user")){
                        return us;
                    }
                    else{
                        if(reader.getLocalName().equals("nume")){
                            us.setUsername(currentPropertyValue);
                        }
                        if(reader.getLocalName().equals("parola")){
                            us.setPassword(currentPropertyValue);
                        }
                        if(reader.getLocalName().equals("categorie")){
                            us.setCategorie(currentPropertyValue);
                        }
                        currentPropertyValue="";
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    currentPropertyValue=reader.getText().trim();
                    break;
            }
        }
        throw new XMLStreamException("Nu s-a citit userul");
    }

    /*All about Observable pattern*/
    public void addObserver(Observer observer){
        obsList.add(observer);
    }

    public void removeObserver(Observer observer){
        obsList.remove(observer);
    }

//    @Override
//    public void notifyObservers(ListEvent<Student> event){
//        obsList.forEach(x->x.update(event));
//    }

    private <E>ListEvent<E> createEvent(ListEventType type,final Iterable<E> list){
        return new ListEvent<E>(type){
            @Override
            public Iterable<E> getList(){
                return list;
            }
//            @Override
//            public E getElement(){
//                return elem;
//            }
        };
    }

    /*This includes all*/
    public <E> List<E> filterAndSorter(List<E> lista, Predicate<E> p, Comparator<E> c){
        return lista.stream().filter(p).sorted(c).collect(Collectors.toList());
    }

    /*ALL ABOUT STUDENT*/
    public Student saveStudent(Student elem){
        for(Student s:getAllStud()){
            if(s.getEmail().equals(elem.getEmail()))
                throw new ServiceException("Email existent, trebuie sa fie unic!");
        }
        Student s=studRepo.save(elem);
        //ListEvent<Student> ev = createEvent(ListEventType.ADD,studRepo.findAll());
        setChanged();
        notifyObservers();
        return s;
    }

    public void updateStudent(Student elem){
        for(Student s:getAllStud()){
            if(s.getEmail().equals(elem.getEmail()) && s.getId()!=elem.getId())
                throw new ServiceException("Email existent, trebuie sa fie unic!");
        }
        studRepo.update(elem);
        //ListEvent<Student> ev=createEvent(ListEventType.UPDATE,studRepo.findAll());
        setChanged();
        notifyObservers();
    }

    public Integer studentGetIdByName(String nume, String grupa) {
        Integer id=-1;
        for (Student s : getAllStud()) {
            if (s.getGrupa().equals(grupa) && s.getNume().equals(nume)){
                id=s.getId();
                break;
            }
        }
        return id;
    }

    public Integer labGetIdByDesc(String desc){
        Integer id=-1;
        for(Tema t:getAllLabs()){
            if(t.getDescriere().equals(desc))
                id=t.getId();
        }
        return id;
    }

    public Integer getMaxId(){
        Integer max=1;
        for(Nota n:gradeRepo.findAll()){
            if(max<n.getId())
                max=n.getId();

        }
        return max;
    }
    public Optional<Student> deleteStudent(Integer id){
        Optional<Student> deleted=null;
        if(studRepo.findOne(id)!=null){
            deleteAllGradesByStud(id);
            deleteUser(studRepo.findOne(id).getEmail());
            deleted=studRepo.delete(id);
            setChanged();
            notifyObservers();
        }

        return deleted;
    }

    public Iterable<Student> getAllStud(){
        return studRepo.findAll();
    }

    public <E>List<E> fromIterableToList(Iterable<E> iterable){
        List<E> newList=new ArrayList<>();
        for(E e:iterable){
            newList.add(e);
        }
        return newList;
    }

    public List<Student> filterAndSortSOne(String nume){
        Comparator<Student> comp=(x,y)->{
            if(x.getId()>y.getId())
                return 1;
            else if(x.getId()<y.getId())
                return -1;
            return 0;
        };
        Predicate<Student> pred=x->x.getNume().contains(nume);
        return filterAndSorter(fromIterableToList(getAllStud()),pred,comp);
    }

    public List<Student> filterAndSortSTwo(String grupa){
        Comparator<Student> comp2=(x,y)->{
            if(x.getNume().compareTo(y.getNume())<0){
                return 1;
            }
            else if(x.getNume().compareTo(y.getNume())>0)
                return -1;
            return 0;
        };
        Predicate<Student> pred2=x->x.getGrupa().equals(grupa);
        return filterAndSorter(fromIterableToList(getAllStud()),pred2,comp2);
    }

    public List<Student> filterAndSortSThree(String mail){
        return filterAndSorter(fromIterableToList(getAllStud()),x->x.getEmail().contains(mail),(x,y)->(x.getCadru().compareTo(y.getCadru())));
    }

    public List<Student> getStudentSublist(int f,int t){
        return fromIterableToList(getAllStud()).subList(f,t);
    }




    /*ALL ABOUT LABS*/
    private static class Comparatori_Predicate{
        public static int comparator1(Tema t1, Tema t2){
            if(t1.getId()<t2.getId())
                return -1;
            else if(t1.getId()>t2.getId())
                return 1;
            return 0;
        }
        public static boolean predicat1(Tema t,int dead){
            return t.getDeadline()<=dead;
        }

        public static int comparator2(Tema t1,Tema t2){
            if(t1.getDeadline()<t2.getDeadline())
                return 1;
            else if(t1.getDeadline()>t2.getDeadline())
                return -1;
            return 0;
        }

        public static boolean predicat2(Tema t,int dead){
            return t.getDeadline()>=dead;
        }

        public static int comparator3(Tema t1,Tema t2){
            if(t1.getDescriere().length()<t2.getDescriere().length())
                return 1;
            else if(t1.getDescriere().length()>t2.getDescriere().length())
                return -1;
            return 0;
        }

        public static boolean predicat3(Tema t){
            return t.getDeadline()%2==0;
        }
    }
    public Tema saveTema(Tema elem){
        Tema t=labRepo.save(elem);
        writeInfo("Am adaugat o noua tema.");
        for(Student student:fromIterableToList(getAllStud())){
            sendEmail(student,"Am adaugat o noua tema.",false);
        }
        hardHome.put(elem.getId(),0);
        setChanged();
        notifyObservers();
        return t;

    }

    public void updateDeadLineTema(Tema elem){

        if(elem.getDeadline()<currentweek || elem.getDeadline()>14){
            throw new ServiceException("Noul deadline nu este valid!");
        }
        Tema existent=labRepo.findOne(elem.getId());
        if(existent!=null)
            if(elem.getDeadline()<existent.getDeadline())
                throw new ServiceException("Noul deadline trebuie sa fie mai mare decat cel vechi!");
        labRepo.update(elem);
        writeInfo("Deadline`ul temei :'"+elem.getDescriere()+"' s-a modificat.");
        for(Student student:fromIterableToList(getAllStud())){
            sendEmail(student,"Deadline`ul temei :'"+elem.getDescriere()+"' s-a modificat.",false);
        }
        setChanged();
        notifyObservers();
    }

    public Optional<Tema> deleteTema(Integer id){
        Optional<Tema> t=null;
        if(labRepo.findOne(id)!=null){
            deleteAllGradesByLab(id);
            t=labRepo.delete(id);
            if(hardHome.containsKey(id))
                hardHome.remove(id);
            setChanged();
            notifyObservers();
        }
        return t;
    }

    public Iterable<Tema> getAllLabs(){
        return labRepo.findAll();
    }

    public List<Tema> filterAndSortTOne(int dead){
        return filterAndSorter(fromIterableToList(getAllLabs()),x->Comparatori_Predicate.predicat1(x,dead),Comparatori_Predicate::comparator1);
    }

    public List<Tema> filterAndSortTTwo(int dead){
        return filterAndSorter(fromIterableToList(getAllLabs()),x->Comparatori_Predicate.predicat2(x,dead),Comparatori_Predicate::comparator2);
    }

    public List<Tema> filterAndSortTThree(){
        return filterAndSorter(fromIterableToList(getAllLabs()),Comparatori_Predicate::predicat3,Comparatori_Predicate::comparator3);
    }

    /*ALL ABOUT GRADES*/
    private static class ComparatoriPredNote{
        private static int comp1(Nota n1,Nota n2){
            if(n1.getGrade()<n2.getGrade())
                return -1;
            else if(n1.getGrade()>n2.getGrade())
                return 1;
            return 0;
        }

        private static int comp2(Nota n1,Nota n2){
            if(n1.getTema()<n2.getTema())
                return -1;
            else if(n1.getTema()>n2.getTema())
                return 1;
            return 0;
        }

        private static boolean pred(Nota n1,int grade){
            return n1.getGrade()==grade;
        }
    }
    public boolean existingGrade(Nota gr){
        for(Nota n:gradeRepo.findAll())
            if(n.getStud()==gr.getStud() && n.getTema()==gr.getTema())
                return true;
        return false;
    }

    public boolean existingGradeId(Nota gr){
        for(Nota n:gradeRepo.findAll())
            if(n.getId()==gr.getId())
                return true;
        return false;
    }

    public void writeInfo(String descriere){
        try(PrintWriter pw=new PrintWriter(new FileWriter("info.txt",false))) {
            pw.println(descriere);
            pw.close();
        }catch (IOException ioe){
            System.err.println(ioe);
        }
    }

    public void sendEmail(Student student,String descriere,boolean continut){

        class GMailAuthenticator extends Authenticator {
            String user;
            String pw;
            public GMailAuthenticator (String username, String password)
            {
                super();
                this.user = username;
                this.pw = password;
            }
            public PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(user, pw);
            }
        }

        final String user="testulet97@gmail.com";
        final String pass="testulet1997";
        Properties props=new Properties();
        props.put("mail.smtp.user", user);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Session session=Session.getInstance(props,new GMailAuthenticator(user,pass));
        //session.setDebug(true);
        try{
            MimeMessage message=new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipients(Message.RecipientType.TO,InternetAddress.parse(student.getEmail()));
            message.setSubject("Informatie");


            BodyPart messageBodyPart = new MimeBodyPart();

            if(continut==true)
                messageBodyPart.setText("Ti-am atasat fisierul cu activitatea ta.");
            else
                messageBodyPart.setText("");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            if(continut==true) {
                DataSource dataSource = new FileDataSource("" + student.getId() + ".txt");
                messageBodyPart.setDataHandler(new DataHandler(dataSource));
            }
            else{
                DataSource dataSource=new FileDataSource("info.txt");
                messageBodyPart.setDataHandler(new DataHandler(dataSource));
            }
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.gmail.com", 465, user, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }catch (MessagingException msg){
            throw new RuntimeException(msg);
        }

    }

    public List<Tema> getLabSublist(int f,int t){
        return fromIterableToList(getAllLabs()).subList(f,t);
    }


    public Nota saveGrade(Nota gr){
        if(studRepo.findOne(gr.getStud())==null){
            throw new ServiceException("Nu se poate face asocierea notei.Studentul nu exista!");
        }

        if(labRepo.findOne(gr.getTema())==null){
            throw new ServiceException("Nu se poate face asocierea notei.Aceast lab nu exista!");
        }

        if(existingGrade(gr)){
            throw new ServiceException("Nu poti sa ii mai dai o nota. Are deja una! *__*");
        }

        int penalizari=currentweek-labRepo.findOne(gr.getTema()).getDeadline();
        if(penalizari>=2)
            gr.setGrade(1);
        else {
            while (penalizari > 0) {
                gr.setGrade(gr.getGrade() - 2);
                penalizari--;
            }
        }
        //consideram ca de notele din fisier stie, de aia fac aici scriere in id.txt+ ca de acolo nu am acces la date
        //daca as face la load in repo ar insemna sa pierd toate datele pe care deja le am
        //intr-o aplicatie reala tu adaugi nu le pui toate in fisier
        Nota n=gradeRepo.save(gr);
		String filename=""+gr.getStud()+".txt";
        try(PrintWriter pw=new PrintWriter(new FileWriter(filename,true))) {
            GradeDTO g=new GradeDTO(gr.getId(),studRepo.findOne(gr.getStud()).getNume(),gr.getTema(),labRepo.findOne(gr.getTema()).getDescriere(),gr.getGrade(),labRepo.findOne(gr.getTema()).getDeadline(),currentweek,"Nu e cazul",studRepo.findOne(gr.getStud()).getGrupa(),studRepo.findOne(gr.getStud()).getEmail());
            pw.println("Adaugare nota "+g.toFile());
            pw.close();
            sendEmail(studRepo.findOne(gr.getStud()),"Situatie",true);
        }catch (IOException ioe){
            System.err.println(ioe);
        }
        setChanged();
        notifyObservers();
        return n;
    }
    public void updateGrade(Nota gr){
        int firstGrade=gradeRepo.findOne(gr.getId()).getGrade();
        int penalizari=currentweek-labRepo.findOne(gr.getTema()).getDeadline();
        if(penalizari>=2)
            gr.setGrade(1);
        else
            while(penalizari>0){
                gr.setGrade(gr.getGrade()-2);
                penalizari--;
            }
        if(gr.getGrade()>firstGrade) {
            gradeRepo.update(gr);
			String filename=""+gr.getStud()+".txt";
            try(PrintWriter pw=new PrintWriter(new FileWriter(filename,true))) {
                GradeDTO g=new GradeDTO(gr.getId(),studRepo.findOne(gr.getStud()).getNume(),gr.getTema(),labRepo.findOne(gr.getTema()).getDescriere(),gr.getGrade(),labRepo.findOne(gr.getTema()).getDeadline(),currentweek,"Nu e cazul",studRepo.findOne(gr.getStud()).getGrupa(),studRepo.findOne(gr.getStud()).getEmail());
                pw.println("Modificare nota "+g.toFile());
                pw.close();
                sendEmail(studRepo.findOne(gr.getStud()),"Situatie",true);
            }catch (IOException ioe){
                System.err.println(ioe);
            }
            setChanged();
            notifyObservers();
        }
    }
    public GradeDTO deleteGrade(Integer id){
        Optional<Nota> deleted=gradeRepo.delete(id);
        setChanged();
        notifyObservers();
        return new GradeDTO(id,studRepo.findOne(deleted.get().getStud()).getNume(),deleted.get().getTema(),labRepo.findOne(deleted.get().getTema()).getDescriere(),deleted.get().getGrade(),labRepo.findOne(deleted.get().getTema()).getDeadline(),currentweek,"Nu e cazul",studRepo.findOne(deleted.get().getStud()).getGrupa(),studRepo.findOne(deleted.get().getStud()).getEmail());
    }
    public Iterable<GradeDTO> getAllGrades(){
        Map<Integer,GradeDTO> note=new HashMap<>();
        for(Nota gr:gradeRepo.findAll()){
            note.put(gr.getId(),new GradeDTO(gr.getId(),studRepo.findOne(gr.getStud()).getNume(),gr.getTema(),labRepo.findOne(gr.getTema()).getDescriere(),gr.getGrade(),labRepo.findOne(gr.getTema()).getDeadline(),currentweek,"Nu e cazul",studRepo.findOne(gr.getStud()).getGrupa(),studRepo.findOne(gr.getStud()).getEmail()));
        }
        return note.values();
    }
    public void deleteAllGradesByLab(Integer val){
        List<Integer> iduri=new ArrayList<>();
        for(Nota gr:gradeRepo.findAll()){
            if(gr.getTema()==val)
                iduri.add(gr.getId());
        }
        for(Integer i:iduri)
            deleteGrade(i);
    }

    public void deleteAllGradesByStud(Integer val){
        List<Integer> iduri=new ArrayList<>();
        for(Nota gr:gradeRepo.findAll()){
            if(gr.getStud()==val)
                iduri.add(gr.getId());
        }
        for(Integer i:iduri)
            deleteGrade(i);
    }
    public List<GradeDTO> filterAndSortGOne(int id){
        List<Nota> l= filterAndSorter(fromIterableToList(gradeRepo.findAll()),x->x.getTema()<id,ComparatoriPredNote::comp1);
        List<GradeDTO> newList=new ArrayList<>();
        for(Nota gr:l){
            newList.add(new GradeDTO(gr.getId(),studRepo.findOne(gr.getStud()).getNume(),gr.getTema(),labRepo.findOne(gr.getTema()).getDescriere(),gr.getGrade(),labRepo.findOne(gr.getTema()).getDeadline(),currentweek,"Nu e cazul",studRepo.findOne(gr.getStud()).getGrupa(),studRepo.findOne(gr.getStud()).getEmail()));
        }
        return newList;
    }

    public List<GradeDTO> filterAndSortGTwo(int grade){
        List<Nota> l= filterAndSorter(fromIterableToList(gradeRepo.findAll()),x->x.getGrade()<grade,ComparatoriPredNote::comp2);
        List<GradeDTO> newList=new ArrayList<>();
        for(Nota gr:l){
            newList.add(new GradeDTO(gr.getId(),studRepo.findOne(gr.getStud()).getNume(),gr.getTema(),labRepo.findOne(gr.getTema()).getDescriere(),gr.getGrade(),labRepo.findOne(gr.getTema()).getDeadline(),currentweek,"Nu e cazul",studRepo.findOne(gr.getStud()).getGrupa(),studRepo.findOne(gr.getStud()).getEmail()));
        }
        return newList;
    }

    public List<GradeDTO> filterAndSortGThree(int grade){
        List<Nota> l= filterAndSorter(fromIterableToList(gradeRepo.findAll()), x->ComparatoriPredNote.pred(x,grade),(x, y)->studRepo.findOne(x.getStud()).getNume().compareTo(studRepo.findOne(y.getStud()).getNume()));
        List<GradeDTO> newList=new ArrayList<>();
        for(Nota gr:l){
            newList.add(new GradeDTO(gr.getId(),studRepo.findOne(gr.getStud()).getNume(),gr.getTema(),labRepo.findOne(gr.getTema()).getDescriere(),gr.getGrade(),labRepo.findOne(gr.getTema()).getDeadline(),currentweek,"Nu e cazul",studRepo.findOne(gr.getStud()).getGrupa(),studRepo.findOne(gr.getStud()).getEmail()));
        }
        return newList;
    }

    public Iterable<Nota> getGrades(){
        return gradeRepo.findAll();
    }

    public List<GradeDTO> getListWithDTO(List<Nota> l){
        List<GradeDTO> newList=new ArrayList<>();
        for(Nota gr:l){
            newList.add(new GradeDTO(gr.getId(),studRepo.findOne(gr.getStud()).getNume(),gr.getTema(),labRepo.findOne(gr.getTema()).getDescriere(),gr.getGrade(),labRepo.findOne(gr.getTema()).getDeadline(),currentweek,"Nu e cazul",studRepo.findOne(gr.getStud()).getGrupa(),studRepo.findOne(gr.getStud()).getEmail()));
        }
        return newList;
    }

    public Nota getNotaFromDTO(GradeDTO g){
        int idStudent=0,idTema=0;
        for(Student s:studRepo.findAll()){
            if(s.getNume().equals(g.getNumeStud())){
                idStudent=s.getId();
                break;
            }
        }

        for(Tema t:labRepo.findAll()){
            if(t.getDescriere().equals(g.getNumeTema())){
                idTema=t.getId();
                break;
            }
        }

        return new Nota(g.getId(),idStudent,idTema,g.getGrade());
    }

    //RAPOARTE
    public double getMedie(int studId){
        int nrNote=0;
        double sumNote=0;
        double medie;
        for (Nota nota:gradeRepo.findAll()){
            if(nota.getStud()==studId){
                sumNote+=nota.getGrade();
                nrNote+=1;
            }
        }
        if(nrNote==0)
            return 0;
        medie=sumNote/nrNote;
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(medie)) ;
    }

    public List<Student> getPromovati(){
        List<Student> promovati=new ArrayList<>();
        for(Student student:fromIterableToList(studRepo.findAll())){
            if(getMedie(student.getId())>=4)
                promovati.add(student);
        }
        return promovati;
    }

    public Tema getHardestHomework(){
        if(hardHome.values().size()>0) {
            int max=0,key=-1;
            for (Integer value : hardHome.keySet()) {
                if(hardHome.get(value)>max){
                    max=hardHome.get(value);
                    key=value;
                }
            }
            if(key!=-1)
                return labRepo.findOne(key);
            return new Tema();
        }
        return new Tema();
    }

    public Tema getEasy() {
        for (Nota nota : gradeRepo.findAll()){
            int key=labRepo.findOne(nota.getTema()).getId();
            if(nota.getGrade()==10){
                if(easyHome.containsKey(key)){
                    easyHome.put(key,easyHome.get(key)+1);
                }
                else{
                    easyHome.put(key,1);
                }
            }

        }
        if(easyHome.size()>0){
            Integer keyAux=-1,max=-1;
            for(Integer key:easyHome.keySet()){
                if(easyHome.get(key)>max){
                    max=easyHome.get(key);
                    keyAux=key;
                }
            }
            return labRepo.findOne(keyAux);
        }
        return new Tema();
    }
    public List<GradeDTO> getGradeSublist(int f,int t){
        return fromIterableToList(getAllGrades()).subList(f,t);
    }
}

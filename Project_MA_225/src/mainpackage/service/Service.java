package mainpackage.service;

import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import javafx.util.Pair;
import mainpackage.domain.ExtendedGrade;
import mainpackage.domain.Grade;
import mainpackage.domain.Homework;
import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;
import mainpackage.repository.Repository;
import mainpackage.utils.ListEvent;
import mainpackage.utils.ListEventType;
import mainpackage.utils.Observable;
import mainpackage.utils.Observer;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Service implements Observable<Student> {
    private Repository<Student, Integer> student_repo;
    private Repository<Homework, Integer> homework_repo;
    private Repository<Grade, Pair<Integer,Integer>> grade_repo;
    private static Integer current_week = 1;

    ArrayList<Observer<Student>> studentObservers = new ArrayList<>();
    private SortedSetMultimap<Float, String> _top_grupe;

    public Service(Repository<Student, Integer> student_repo,
                   Repository<Homework, Integer> homework_repo,
                   Repository<Grade, Pair<Integer,Integer>> grade_repo) {
        this.student_repo = student_repo;
        this.homework_repo = homework_repo;
        this.grade_repo = grade_repo;
    }

    /**
     * Adds a new student to the repo
     * @param student the new student
     * @throws MyException if there is a student with that id
     */
    public void add_student(Student student) throws MyException {
        student_repo.save(student);
        ListEvent<Student> ev = createEvent(ListEventType.ADD, student, student_repo.get_all());
        notifyObservers(ev);
    }

    public void setCurrent_week(Integer cur)
    {
        current_week = cur;
        System.out.printf("Current week is: %d", current_week);
    }

    /**
     * Updates an existing student
     * @param student the new student
     * @throws MyException if there is no student with the same id
     */
    public void update_student(Student student) throws MyException {
        Student old_student = student_repo.update(student);
        ListEvent<Student> ev = createEvent(ListEventType.UPDATE, old_student, student_repo.get_all());
        notifyObservers(ev);
    }

    /**
     * Deletes a student by id if it exists
     * @param student_id the id of the student ot be deleted
     * @return The student that was delted or null
     */
    public Student delete_student(Integer student_id) throws  MyException{
        for(Grade grade : grade_repo.get_all())
            if (Objects.equals(grade.get_idStudent(), student_id))
                throw new MyException("Can not delete student because it has a grade.");
        Student deleted_student = student_repo.delete(student_id);
        ListEvent<Student> ev = createEvent(ListEventType.REMOVE, deleted_student, student_repo.get_all());
        notifyObservers(ev);
        return deleted_student;
    }

    /**
     * Adds a new Homework to the repo
     * @param homework the new homework
     * @throws MyException if there is a homework with that id
     */
    public void add_homework(Homework homework) throws MyException {
        if(homework.getDeadline() < current_week)
            throw new MyException("Can not add homework because current week is " + current_week);
        homework_repo.save(homework);
        notifyObservers(null);
    }

    /**
     * Updates an existing homework
     * @param homework the new homework
     * @throws MyException if there is no homework with the same id
     */
    public void update_homework(Homework homework) throws MyException {
        if (homework.getDeadline() <= current_week )
            throw new MyException("Can not update deadline beacuse current week is " + current_week.toString());
        Optional<Homework> old_hw = homework_repo.get(homework.getId());
        if (!old_hw.isPresent())
            throw new MyException("There is no homework submitted with that id.");
        if (homework.getDeadline() < old_hw.get().getDeadline())
            throw new MyException("Deadline can only be updated by adding at least a week.");
        homework_repo.update(homework);
        notifyObservers(null);
    }

    /**
     * Deletes a homework by id if it exists
     * @param homework_id the id of the homework ot be deleted
     * @return The homework that was delted or null
     */
    public Homework delete_homework(Integer homework_id){
        Homework returned = homework_repo.delete(homework_id);
        notifyObservers(null);
        return returned;
    }

    public Set<Student> get_all_student()
    {
        Set<Student> all_students  = new HashSet<Student>();
        student_repo.get_all().forEach(student -> all_students.add(new Student(student)));
        return all_students;
    }

    public Set<Homework> get_all_homeworks()
    {
        Set<Homework> all_homeworks = new HashSet<Homework>();
        homework_repo.get_all().forEach(homework -> all_homeworks.add(new Homework(homework)));
        return all_homeworks;
    }

    public Set<Grade> get_all_grades()
    {
        Set<Grade> all_grades = new HashSet<Grade>();
        grade_repo.get_all().forEach(grade-> all_grades.add(grade));
        return all_grades;
    }

    public void add_grade(Grade grade,String comment) throws MyException {
        Optional<Student > st = student_repo.get(grade.get_idStudent());
        Optional< Homework> hw = homework_repo.get(grade.get_idHomework());
        int week = current_week;
        if (!st.isPresent())
            throw new MyException("No such student!");
        if (!hw.isPresent())
            throw new MyException("No such homework!");
        Homework homework = hw.get();
        Student student = st.get();
        if(week <1 || week >14)
            throw new MyException("Invalid week, has to be between 1 and 14");
        if( homework.getDeadline() < week)
        {
            if(homework.getDeadline() + 2 < week )
            {
                grade.set_value(1.0f);
            }
            else
            {
                grade.set_value(grade.get_value() - 2 * (week - homework.getDeadline()));
            }
        }
        grade_repo.save(grade);
        String formatted_string = String.format(
                "New grade, Homework: %d, Grade: %f, Deadline: %d, Week: %d, Comments: \"%s\"\n",
                homework.getId(), grade.get_value(), homework.getDeadline(), week, comment);
        send_email(student.getEmail(),"New Grade!",formatted_string);
        String file_name = String.valueOf(student.getId()) + ".txt";
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(file_name,true));
            output.append(formatted_string);
            output.close();
        } catch (IOException e) {
            throw new MyException("Adding grade to studentfile failed! " + e.getMessage());
        }
        notifyObservers(null);
    }

    private static void send_email(String to_email, String title, String data) {
        if(to_email.equals("no-email"))
            return;
        new Thread(() ->
        {
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
                            return new PasswordAuthentication("noreply.map.ubb@gmail.com","map.ubb123");
                        }
                    });
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("noreply.map.ubb@gmail.com"));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to_email));
                message.setSubject(title);
                message.setText(data);
                Transport.send(message);
                System.out.println("DONE. MAIL SENT");
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void modify_grade(Grade grade,String comment) throws MyException {
        System.out.println("UPDATING GRADE!!!!");
        int week = current_week;
        Optional<Student> student = student_repo.get(grade.get_idStudent());
        Optional<Homework> hw = homework_repo.get(grade.get_idHomework());
        if (!student.isPresent())
            throw new MyException("No such student!");
        if (!hw.isPresent())
            throw new MyException("No such homework!");
        Homework homework = hw.get();
        if(week <1 || week >14)
            throw new MyException("Invalid week, has to be between 1 and 14");
        if( homework.getDeadline() < week)
        {
            if(homework.getDeadline() + 2 < week )
            {
                grade.set_value(1.0f);
            }
            else
            {
                grade.set_value(grade.get_value() - 2 * (week - homework.getDeadline()));
            }
        }

        if (grade_repo.get(grade.getId()).get().get_value() >= grade.get_value())
            throw new MyException("Grade not updated because i it`s value is below or equal to the current one"+ grade.get_value());
        grade_repo.update(grade);

        String formatted_string = String.format(
                "Modify grade, Homework: %d, Grade: %f, Deadline: %d, Week: %d, Comments: \"%s\"\n",
                homework.getId(), grade.get_value(), homework.getDeadline(), week, comment);
        send_email(student.get().getEmail(),"UPDATED Grade!",formatted_string);
        String file_name = String.valueOf(student.get().getId()) + ".txt";
        Writer output;
        try {
            output = new BufferedWriter(new FileWriter(file_name,true));
            output.append(formatted_string);
            output.close();
        } catch (IOException e) {
            throw new MyException("Modifying grade failed! " + e.getMessage());
        }
        notifyObservers(null);
    }

    public static <E> List<E> filter_and_sorter(List<E> list, Predicate<E> p, Comparator<E> c)
    {
        Stream<E> lst = list.stream();
        if(p != null)
             lst = lst.filter(p);
        if(c != null)
           lst = lst.sorted(c);

        return lst.collect(Collectors.toList());
    }

/*    public List student_filter(List<Student> list, Predicate<Student> p, Comparator<Student > c)
    {
        return filter_and_sorter(list, p, c);
    }*/

    public List filter_students_name(String name)
    {
        List<Student> student_list = new ArrayList<Student>();
        student_list.addAll(get_all_student());

        return filter_and_sorter(student_list ,
                (Student st)->st.getName().toLowerCase().contains(name),
                Comparator.comparing(Student::getName));
    }

    public List filter_students_email(String email)
    {
        List<Student> student_list = new ArrayList<Student>();
        student_list.addAll(get_all_student());

        return Service.filter_and_sorter(student_list ,
                (Student st)->st.getEmail().contains(email),
                Comparator.comparing(Student::getName));
    }

    public List filter_students_teacher(String name) {
        List<Student> student_list = new ArrayList<Student>();
        student_list.addAll(get_all_student());

        return Service.filter_and_sorter(student_list ,
                (Student st)->st.getTeacher().contains(name),
                Comparator.comparing(Student::getName));
    }

    public List filter_homeworks_deadline() {
        List<Homework> homework_list = new ArrayList<Homework>();
        homework_list.addAll(get_all_homeworks());

        return Service.filter_and_sorter(homework_list ,
                null,
                Comparator.comparing(Homework::getDeadline));
    }

    public List filter_homework_description(String desc) {
        List<Homework> homework_list = new ArrayList<Homework>();
        homework_list.addAll(get_all_homeworks());

        return Service.filter_and_sorter(homework_list ,
                (Homework hw)->hw.getDescription().contains(desc),
                null);
    }

    public List filter_homeworks_id() {
        List<Homework> homework_list = new ArrayList<Homework>();
        homework_list.addAll(get_all_homeworks());

        return Service.filter_and_sorter(homework_list ,
                null,
                Comparator.comparing(Homework::getId).reversed());
    }

    public List filter_grades_greater(Integer grade_cmp) {
        List<Grade> grades_list = new ArrayList<Grade>();
        grades_list.addAll(get_all_grades());

        return Service.filter_and_sorter(grades_list ,
                (Grade g)->{return g.get_value() >= grade_cmp;},
                null);
    }

    public List filter_grades_smaller(Integer grade_cmp) {
        List<Grade> grades_list = new ArrayList<Grade>();
        grades_list.addAll(get_all_grades());

        return Service.filter_and_sorter(grades_list ,
                (Grade g)->{return g.get_value() <= grade_cmp;},
                null);
    }

    public List filter_grades_student_id() {
        List<Grade> grades_list = new ArrayList<Grade>();
        grades_list.addAll(get_all_grades());

        return Service.filter_and_sorter(grades_list ,
                null,
                Comparator.comparing(Grade::get_idStudent));
    }

    @Override
    public void addObserver(Observer<Student> o) {
        studentObservers.add(o);
    }

    @Override
    public void removeObserver(Observer<Student> o) {
        studentObservers.remove(o);
    }

    @Override
    public void notifyObservers(ListEvent<Student> event) {
        studentObservers.forEach(x -> x.notifyEvent(event));
    }

    private <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> l) {
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


    public List<Student> filter_students(String nume, String profesor, String grupa)
    {
        Predicate<Student> numeP = x->true;
        Predicate<Student> profP = x->true;
        Predicate<Student> grupaP = x->true;

        if (nume != null)
            numeP = student -> student.getName().toLowerCase().contains(nume.toLowerCase());
        if (profesor != null)
            profP = student -> student.getTeacher().toLowerCase().contains(profesor.toLowerCase());
        if (grupa != null)
            grupaP = student -> student.getGroup().equals(grupa);

        Comparator<Student> c = Comparator.comparing(Student::getId);
        Predicate<Student> p = numeP.and(profP).and(grupaP);
        return  get_all_student().stream().filter(p).sorted(c).collect(Collectors.toList());
    }

    public List<Homework> filter_homeworks(String descriere, Integer deadline)
    {
        Predicate<Homework> descriereP= x->true;
        Predicate<Homework> deadlineP= x->true;

        if (descriere != null)
            descriereP = hw-> hw.getDescription().toLowerCase().contains(descriere.toLowerCase());
        if (deadline != null)
            deadlineP = hw -> hw.getDeadline() >= deadline;

        Comparator<Homework> c = Comparator.comparing(Homework::getDeadline);
        Predicate<Homework> p = descriereP.and(deadlineP);
        return  get_all_homeworks().stream().filter(p).sorted(c).collect(Collectors.toList());
    }

    public Set<String> getGroups() {
        Set<String> groups =new HashSet<String>();
        for(Student st : get_all_student())
        {
            if(! groups.contains(st.getGroup()))
            {
                groups.add(st.getGroup());
            }
        }
        return groups;
    }

    public List<ExtendedGrade> get_extended_grades()
    {
        List<ExtendedGrade> gradesEx= new ArrayList<>();
        for(Grade g: get_all_grades())
        {
            String name = get_student_name_for_id(g.get_idStudent());
            String group = get_student_group_for_id(g.get_idStudent());
            gradesEx.add(new ExtendedGrade(name, group, new Grade(g))); //MAYBE COPY CONSTRUCTOR FOR GRADE?
        }
        System.out.println(gradesEx);
        return gradesEx;
    }

    public String get_student_name_for_id(Integer id) {
        String name = null;
        for(Student s: get_all_student())
        {
            if(Objects.equals(s.getId(), id)) {
                name = s.getName();
                break;
            }
        }
        return name;
    }

    public String get_student_group_for_id(Integer id) {
        String group = null;
        for(Student s: get_all_student())
        {
            if(Objects.equals(s.getId(), id))
            {
                group =  s.getGroup();
                break;
            }
        }
        return group;
    }

    public List<Integer> get_all_student_ids() {
        List<Integer> ids= new ArrayList<>();
        for(Student st : get_all_student())
        {
                ids.add(st.getId());
        }
        return ids;
    }

    public List<Integer> get_all_homework_ids() {
        List<Integer> ids= new ArrayList<>();
        for(Homework hw : get_all_homeworks())
        {
            ids.add(hw.getId());
        }
        return ids;
    }
    public List<String> get_students_names_with_ids()
    {
        List<String> names =new ArrayList<>();
        for(Student st: get_all_student())
        {
            names.add(st.getName()+"_"+st.getId().toString());
        }
        return names;
    }

    public List<String >getHw() {
        List<String> groups =new ArrayList<>();
        for(Homework hw : get_all_homeworks())
        {
                groups.add(String.valueOf(hw.getId()));
        }
        return groups;
    }

    public List<ExtendedGrade> filter_grades(String grupa, Integer hwId, Float maxGrade)
    {
        Predicate<ExtendedGrade> grupaP= x->true;
        Predicate<ExtendedGrade> hwIdP= x->true;
        Predicate<ExtendedGrade> maxGradeP= x->true;

        if (grupa != null)
            grupaP= g-> g.getGroup().equals(grupa);
        if (hwId!= null)
            hwIdP= g -> g.getGrade().get_idHomework().equals(hwId);
        if (maxGrade!= null)
            maxGradeP= g -> g.getGrade().get_value() >= maxGrade;
        Comparator<ExtendedGrade> c = Comparator.comparing(ExtendedGrade::getName);
        Predicate<ExtendedGrade> p = grupaP.and(hwIdP).and(maxGradeP);
        return  get_extended_grades().stream().filter(p).sorted(c).collect(Collectors.toList());
    }

    public SortedSetMultimap<Float, String> get_top_studenti() {
        Map<Integer,Float> average = new HashMap<>();
        int totalHomeworks= homework_repo.size();

        SortedSetMultimap<Float,String> return_map = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());

        for(Student st : get_all_student())
        {
            average.put(st.getId(),(float)0);
        }
        for(Grade g: get_all_grades())
        {
            average.put(g.get_idStudent(),average.get(g.get_idStudent())+g.get_value());
        }

        for(Integer id: average.keySet())
            return_map.put(average.get(id)/totalHomeworks,get_student_name_for_id(id));

        return return_map;
    }

    public SortedSetMultimap<Float,String> get_top_teme() {
        Map<Integer, Float> average = new HashMap<>();
        int totalStudents = student_repo.size();

        SortedSetMultimap<Float, String> return_map = TreeMultimap.create();

        for(Homework hw: get_all_homeworks())
        {
            average.put(hw.getId(),(float)0);
        }

        for(Grade g: get_all_grades())
        {
            average.put(g.get_idHomework(), average.get(g.get_idHomework()) + g.get_value());
        }

        for(Integer id: average.keySet())
            return_map.put(average.get(id)/totalStudents, String.format("  - %d -  %s",id,homework_repo.get(id).get().getDescription()));
        return return_map;
    }

    public Float get_medie_for_student(Integer studentId)
    {
        Float media = (float)0 ;
        for(Grade g: get_all_grades())
        {
            System.out.printf("%d %d\n",g.get_idStudent(),studentId);
            if (Objects.equals(g.get_idStudent(), studentId))
                media = media+g.get_value();
        }
        return media/homework_repo.size();
    }

    public SortedSetMultimap<Float,String> get_top_grupe() {
        Map<String, Float> average = new HashMap<>();
        Map<String, Integer> studentsForGroup = new HashMap<>();

        SortedSetMultimap<Float, String> return_map = TreeMultimap.create(Ordering.natural().reverse(), Ordering.natural());

        for(Student st: get_all_student())
            average.put(st.getGroup(), (float)0);

        for(String group : average.keySet())
            studentsForGroup.put(group, 0);

        for(Student st: get_all_student()) {
            studentsForGroup.put(st.getGroup(), studentsForGroup.get(st.getGroup()) + 1);
            average.put(st.getGroup(), average.get(st.getGroup()) + get_medie_for_student(st.getId()));
        }
        for(String grupa: average.keySet())
        {
            return_map.put(average.get(grupa)/studentsForGroup.get(grupa),grupa);
        }
        return return_map;
    }


}

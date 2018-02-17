package Utils;

import Domain.Project;
import Domain.Student;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;

public class Utils {
    /**
     * gets all Student hashmap
     * @param students
     * @return HashMap<String, Student> all students
     */
    public static HashMap<UUID, Student> getStudentsHashmap(Iterable<Student> students){
        HashMap<UUID, Student> newStudents = new HashMap<>();
        for(Student stud : students){
            newStudents.put(stud.getID(), stud);
        }
        return newStudents;
    }

    /**
     * gets all Project hashmap
     * @param projects
     * @return HashMap<String, Project> all projects
     */
    public static HashMap<UUID, Project> getProjectsHashmap(Iterable<Project> projects){
        HashMap<UUID, Project> newProjects = new HashMap<>();
        for(Project proj : projects){
            newProjects.put(proj.getID(), proj);
        }
        return newProjects;
    }

    public static <E> ListEvent<E> createEvent(ListEventType type, final E elem, final Iterable<E> list){
        return new ListEvent<E>(type) {
            @Override
            public Iterable<E> getListEvent() {
                return list;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }

    public static void deleteLogINEmail(String email) {
        File inputFile = new File("studentsLOGIN.txt");
        File tempFile = new File("myTempFile.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (trimmedLine.equals(email)) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            boolean successful = tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLoggedInStudent(String email, String password){
        try(FileWriter fw = new FileWriter("studentsLOGIN.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.print(email+";"+password+"\n");
        } catch (IOException ignored) {

        }
    }

    public static void createNewStudentFile(String ID){
        if(Files.exists(Paths.get("C:\\Users\\Andreea\\Desktop\\Andreea\\Facultate\\Anul_II\\Semestrul I\\MAP\\Lab\\Lab_2"+ID))){
            return;
        }
        File f = new File(ID+".txt");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean validatePassword(String password){
        return password.compareTo("")!=0 && password.length()>=3 && !password.contains(" ");
    }

}


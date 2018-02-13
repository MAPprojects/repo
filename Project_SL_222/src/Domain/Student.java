package Domain;

import java.util.Comparator;

public class Student implements HasID<Integer>,Comparator<Student>{
        private int idStudent;
        private String nume;
        private int grupa;
        private String email;
        private String profesor;

        public Student(int idStudent, String nume, int grupa, String email, String profesor) {
            this.idStudent = idStudent;
            this.nume = nume;
            this.grupa = grupa;
            this.email = email;
            this.profesor = profesor;
        }

        @Override
        public Integer getID() {
            return idStudent;
        }

    public Integer getIdStudent() {
        return idStudent;
    }

    public String getNume() {
            return nume;
        }

        public Integer getGrupa() {
            return grupa;
        }

        public String getEmail() {
            return email;
        }

        public String getProfesor() {
            return profesor;
        }

        @Override
        public void setID(Integer idStudent) {
            this.idStudent = idStudent;
        }

        public void setNume(String nume) {
            this.nume = nume;
        }

        public void setGrupa(int grupa) {
            this.grupa = grupa;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setProfesor(String profesor) {
            this.profesor = profesor;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Student) {
                Student oth = (Student) obj;
                return this.getID() == oth.getID();
            }
            return false;
        }

        @Override
        public int compare(Student stud1,Student stud2){
            if(stud1.getID()>stud2.getID())
                return 1;
            else if(stud1.getID()<stud2.getID())
                return -1;
            else
                return 0;
        }
}

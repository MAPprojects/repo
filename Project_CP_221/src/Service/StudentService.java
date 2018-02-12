package Service;
import Validator.ValidatorException;

import Domain.Student;
import Repository.StudentRepository;

public class StudentService extends AbstractService<Integer, Student> {

    public StudentService(StudentRepository sr)
    {
        super(sr);
    }

    public void addStudent(int id,String name,String email,String group,String professor) throws ValidatorException {
        Student st=new Student(id,name,email,group,professor);
        super.absRepo.save(st);
    }

    public void deleteStudent(int id)
    {
        absRepo.delete(id);
    }

    public void updateStudent(int id,String name,String email,String group,String professor)
    {
        Student st=new Student(id,name,email,group,professor);
        absRepo.update(id,st);
    }
}
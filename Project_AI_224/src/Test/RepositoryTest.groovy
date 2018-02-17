package Test

import Domain.Project
import Domain.Student
import Domain.ValidatorOption
import Repository.AbstractRepository
import Repository.MemoRepository
import Repository.ProjectFileRepository
import Repository.StudentFileRepository

class RepositoryTest extends GroovyTestCase {
    public static void testMemoRepository(){
        def s1 = new Student("1", "bla1", 224, "bla1@scs.ubbcluj.ro", "teacher1");
        def s2 = new Student("2", "bla2", 225, "bla2@scs.ubbcluj.ro", "teacher1");
        def s3 = new Student("3", "bla3", 224, "bla3@scs.ubbcluj.ro", "teacher2");
        def s4 = new Student("4", "bla4", 224, "bla4@scs.ubbcluj.ro", "teacher2");
        AbstractRepository<Student, String> repo = new MemoRepository<Student, String>(ValidatorOption.STUDENT);
        repo.save(s1);
        repo.save(s2);
        repo.save(s3);
        repo.save(s4);
        assertEquals(4, repo.size());
        repo.save(s3);
        assertEquals(4, repo.size());
        repo.delete("3");
        assertEquals(3, repo.size());
        assertEquals(225, repo.getEntity("2").getGroup());
    }

    public static void testStudentFileRepo(){
        StudentFileRepository fileRepo = new StudentFileRepository(ValidatorOption.STUDENT, "studentsTest.txt");
        def s1 = new Student("11", "bla1", 224, "bla1@scs.ubbcluj.ro", "teacher1");
        def s2 = new Student("22", "bla2", 225, "bla2@scs.ubbcluj.ro", "teacher1");
        def s3 = new Student("3", "bla3", 224, "bla3@scs.ubbcluj.ro", "teacher2");
        def s4 = new Student("4", "bla4", 224, "bla4@scs.ubbcluj.ro", "teacher2");
        fileRepo.save(s1);
        fileRepo.save(s2);
        fileRepo.save(s3);
        assertEquals(5, fileRepo.size());
    }

    public static void testProjectFileRepo(){
        ProjectFileRepository fileRepo = new ProjectFileRepository(ValidatorOption.PROJECT, "projectsTest.txt");
        def p1 = new Project("1", "temaLab1", 1);
        def p2 = new Project("2", "temaLab2", 2);
        def p3 = new Project("3", "temaLab3", 4);
        fileRepo.save(p1);
        fileRepo.save(p2);
        fileRepo.save(p3);
        assertEquals(3, fileRepo.size());
        fileRepo.delete("2");
        assertEquals(2, fileRepo.size());
    }
}

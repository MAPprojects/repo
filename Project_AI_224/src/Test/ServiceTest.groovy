package Test

import Domain.Grade
import Domain.Project
import Domain.Student
import Service.ProjectService
import Service.StudentService

class ServiceTest extends GroovyTestCase{
    /**
     * tests ProjectService
     */
    public static void testProjectService() {
        ProjectService projectService = new ProjectService();
        projectService.save("1", "temaLab1", "1");
        projectService.save("2", "temaLab2", "2");
        projectService.save("3", "temaLab3", "4");
        assertEquals("temaLab1", projectService.getEntity("1").getDescription());
        projectService.updateProject("3", "5");
        assertEquals(2, projectService.getEntity("3").getDeadline());
        projectService.updateProject("3", "2");
        assertEquals(2, projectService.getEntity("3").getDeadline());
    }

    /**
     * tests StudentService
     */
    public static void testStudentService() {
        StudentService studentService = new StudentService();
        studentService.save("1", "bla1", "224", "bla1@scs.ubbcluj.ro", "teacher1");
        studentService.save("2", "bla2", "225", "bla2@scs.ubbcluj.ro", "teacher1");
        studentService.save("3", "bla3", "224", "bla3@scs.ubbcluj.ro", "teacher2");
        assertEquals(224, studentService.getEntity("1").getGroup());
        studentService.delete("3");
        assertEquals(null, studentService.getEntity("3"));
        assertEquals("bla2@scs.ubbcluj.ro", studentService.getEntity("2").getEmail());
    }

    /**
     * tests GradeService
     */
    public static void testGradeService() {


    }
}

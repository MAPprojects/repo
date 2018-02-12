//package entities;
//
//import org.junit.Assert;
//import org.junit.Test;
//
//public class EntitiesTestCase {
//    private Student student = new Student("1", "nume1", "222", "nume@domeniu.com", "Ion Ion");
//    private Tema tema = new Tema("1", "descriere1", 3);
//    private Nota nota = new Nota("1", student, tema, 10);
//
//    @Test
//    public void creareStudentTestCase() {
//        Assert.assertEquals(student.getId(), "1");
//        Assert.assertEquals(student.getNume(), "nume1");
//        Assert.assertEquals(student.getGrupa(), "222");
//        Assert.assertEquals(student.getEmail(), "nume@domeniu.com");
//        Assert.assertEquals(student.getCadruDidacticIndrumator(), "Ion Ion");
//    }
//
//    @Test
//    public void creareTemaTestCase() {
//        Assert.assertEquals(tema.getId(), "1");
//        Assert.assertEquals(tema.getCerinta(), "descriere1");
//        Assert.assertEquals((int) tema.getTermenPredare(), 3);
//    }
//
//    @Test
//    public void settereStudentTestCase() {
//        student.setId("2");
//        student.setNume("nume2");
//        student.setGrupa("223");
//        student.setCadruDidacticIndrumator("Grigore");
//        student.setEmail("email");
//        Assert.assertEquals(student.getId(), "2");
//        Assert.assertEquals(student.getNume(), "nume2");
//        Assert.assertEquals(student.getGrupa(), "223");
//        Assert.assertEquals(student.getEmail(), "email");
//        Assert.assertEquals(student.getCadruDidacticIndrumator(), "Grigore");
//    }
//
//    @Test
//    public void settereTemaTestCase() {
//        tema.setId("2");
//        tema.setTermenPredare(14);
//        tema.setCerinta("derp");
//        Assert.assertEquals(tema.getId(), "2");
//        Assert.assertEquals(tema.getCerinta(), "derp");
//        Assert.assertEquals((int) tema.getTermenPredare(), 14);
//    }
//
//    @Test
//    public void gettereNotaTestCase() {
//        Assert.assertEquals(nota.getStudent().getId(), "1");
//        Assert.assertEquals(nota.getTema().getId(), "1");
//        Assert.assertEquals(nota.getValoare(), (Integer) 10);
//    }
//
//    @Test
//    public void settereNotaTestCase() {
//        nota.getStudent().setId("2");
//        nota.getTema().setId("2");
//        nota.setValoare(8);
//        Assert.assertEquals(nota.getStudent().getId(), "2");
//        Assert.assertEquals(nota.getTema().getId(), "2");
//        Assert.assertEquals(nota.getValoare(), (Integer) 8);
//    }
//
//
//}

package com.company.Teste;

import com.company.Domain.Student;

import static org.junit.Assert.*;

public class StudentTest {

    private Student student;

    @org.junit.Before
    public void setUp() throws Exception {
        student = new Student(1,"Ion",12,"a@c","Popescu");
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void getID() throws Exception {
        //assertEquals("getID cu bai",1,student.getID());
        assertEquals("getID cu bai", (Integer) 1, student.getID());
    }

    @org.junit.Test
    public void getNume() throws Exception {
        assertEquals("getNume cu bai","Ion",student.getNume());
    }

    @org.junit.Test
    public void getGrupa() throws Exception {
        assertEquals("getGrupa cu bai",12,student.getGrupa());
    }

    @org.junit.Test
    public void getEmail() throws Exception {
        assertEquals("getEmail cu bai", "a@c",student.getEmail());
    }

    @org.junit.Test
    public void getProfLab() throws Exception {
        assertEquals("getProfLab cu bai", "Popescu", student.getProfLab());
    }

    @org.junit.Test
    public void setID() throws Exception {
        student.setID(2);
        assertEquals("setID cu bai", (Integer) 2, student.getID());
    }

    @org.junit.Test
    public void setNume() throws Exception {
        student.setNume("Abdul");
        assertEquals("setNume cu bai", "Abdul", student.getNume());
    }

    @org.junit.Test
    public void setGrupa() throws Exception {
        student.setGrupa(100);
        assertEquals("setGrupa cu bai", 100, student.getGrupa());
    }

    @org.junit.Test
    public void setEmail() throws Exception {
        student.setEmail("p@a");
        assertEquals("setEmail cu bai", "p@a", student.getEmail());
    }

    @org.junit.Test
    public void setProfLab() throws Exception {
        student.setProfLab("Mircea");
        assertEquals("setProfLab cu bai", "Mircea", student.getProfLab());
    }

}
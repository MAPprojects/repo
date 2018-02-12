package services;

import entities.Nota;
import entities.Student;
import entities.Tema;
import net.sf.jasperreports.engine.JRDataSource;
import repository.NotaRepository;
import repository.StudentRepository;
import repository.TemaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class StatisticsService {
    private NotaRepository notaRepository;
    private StudentRepository studentRepository;
    private TemaRepository temaRepository;

    public StatisticsService(NotaRepository notaRepository, StudentRepository studentRepository, TemaRepository temaRepository) {
        this.notaRepository = notaRepository;
        this.studentRepository = studentRepository;
        this.temaRepository = temaRepository;
    }

    public void setNotaRepository(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void setTemaRepository(TemaRepository temaRepository) {
        this.temaRepository = temaRepository;
    }

    public HashMap<Student, Double> getMediileStudentilor() {
        List<Student> students = (List<Student>) studentRepository.findAll();
        List<Nota> note = (List<Nota>) notaRepository.findAll();
        List<Tema> teme = (List<Tema>) temaRepository.findAll();
        HashMap<Student, Double> result = new HashMap<>();
        students.forEach(s -> {
            Double sumaNote = 0.0;
            Integer nrTeme = teme.size();
            for (Nota n : note) {
                if (n.getIdStudent().equals(s.getId())) {
                    sumaNote += n.getValoare();
                }
            }
            Double media = sumaNote / nrTeme;
            result.put(s, media);
        });
        return result;
    }

    public HashMap<Student, Double> getStudentiiCareIntraExamen() {
        List<Student> students = (List<Student>) studentRepository.findAll();
        List<Nota> note = (List<Nota>) notaRepository.findAll();
        List<Tema> teme = (List<Tema>) temaRepository.findAll();
        HashMap<Student, Double> result = new HashMap<>();
        students.forEach(s -> {
            Double sumaNote = 0.0;
            for (Nota n : note) {
                if (n.getIdStudent().equals(s.getId())) {
                    sumaNote += n.getValoare();
                }
            }
            Double media = sumaNote / teme.size();
            if (media > 4) {
                result.put(s, media);
            }
        });
        return result;
    }

    public HashMap<Tema, Double> getMediileNotelorLaFiecareTema() {
        List<Tema> teme = (List<Tema>) temaRepository.findAll();
        List<Student> students = (List<Student>) studentRepository.findAll();
        List<Nota> note = (List<Nota>) notaRepository.findAll();
        HashMap<Tema, Double> result = new HashMap<>();
        teme.forEach(t -> {
            int nrElevi = students.size();
            Double sumaNote = 0.0;
            for (Nota n : note) {
                if (n.getIdTema().equals(t.getId())) {
                    sumaNote += n.getValoare();
                }
            }
            Double media = sumaNote / nrElevi;
            result.put(t, media);
        });
        return result;
    }

}

package Services;

import Domain.Nota;
import Domain.Student;
import Domain.Task;
import Exceptions.RepositoryException;
import Exceptions.ValidationException;
import Repository.GenericRepository;
import Utilities.GradeExporter;
import Utilities.ListEvent;
import Utilities.ListEventType;

import java.util.ArrayList;
import java.util.List;

public class NoteService extends AbstractService<Nota> {
    GenericRepository<Integer, Nota> repository;

    public NoteService(GenericRepository<Integer, Nota> repository) {
        this.repository = repository;
    }

    private int penalize(int valoare, int week) {
        int newValue = valoare;

        if (week > getCurrentWeek()) {
            if (week > getCurrentWeek() + 1) {
                newValue = 1;
            } else {
                for (int i = week; i < getCurrentWeek(); i++) {
                    newValue -= 2;
                }
            }

            if (newValue < 1) {
                newValue = 1;
            }

        }
        return newValue;
    }

    public void add(int idNota, int idStudent, int idTema, int valoare, int week, String observatie,
                    GenericRepository<Integer, Task> temeRepo,
                    GenericRepository<Integer, Student> studentRepo) {
        if (repository.find(idNota)) {
            throw new ValidationException("Nota id already in repository");
        }

        if (!studentRepo.find(idStudent) || !temeRepo.find(idTema)) {
            throw new RepositoryException("student id or tema id doesn't exist");
        }

        //aplica penalizare
        //valoare = penalize(valoare, week);


        Iterable<Nota> allNote = repository.getAll();
        for (Nota nota : allNote) {
            //System.out.println(nota);
            if (nota.getIdStudent() == idStudent && nota.getNrTema() == idTema) {
                throw new RepositoryException("Student already has tema given");
            }
        }
        Nota n = new Nota(idNota, idStudent, idTema, valoare);

        repository.add(n);
        ListEvent<Nota> ev = createEvent(ListEventType.ADD, n, getAllAsList());
        notifyObservers(ev);

        Task t = temeRepo.get(idTema);

        int deadline = t.getDeadline();

        GradeExporter exporter = new GradeExporter(String.valueOf(idStudent));
        exporter.exportGrade("Adaugare nota, ",idTema, valoare, deadline, week, observatie);
    }

    public Nota remove(int id) {
        Nota deleted = repository.delete(id);
        if (deleted != null) {
            ListEvent<Nota> ev = createEvent(ListEventType.REMOVE, deleted, getAllAsList());
            notifyObservers(ev);
            return deleted;
        }
        throw new ValidationException("grade does not exist");
    }

    public void update(int id, int valoare, int week, String observatie, TaskService temeService) {
        Nota oldNota = repository.get(id);
        if (oldNota != null) {
            int newValue = penalize(valoare, week);

            if (newValue < valoare) {
                throw new ValidationException("nota noua este mai mica decat cea veche si nu se schimba");
            }
            else {
                oldNota.setValoare(newValue);
                repository.update(id, oldNota);
                ListEvent<Nota> ev = createEvent(ListEventType.ADD, oldNota, getAllAsList());
                notifyObservers(ev);

                Task t = temeService.get(oldNota.getNrTema());

                GradeExporter exporter = new GradeExporter(String.valueOf(oldNota.getIdStudent()));
                int deadline = t.getDeadline();

                exporter.exportGrade("Modificare nota, ", oldNota.getNrTema(), newValue, deadline, week, observatie);


            }
        }

    }

    public Nota findByStudentAndTask(int studentId, int taskId) {
        for (Nota n : repository.getAll()) {
            if (n.getIdStudent() == studentId && n.getNrTema() == taskId) {
                return n;
            }
        }
        throw new RepositoryException("student does not have grade for task");
    }

    public List<Nota> getAllAsList() {
        List<Nota> all = new ArrayList<>();
        repository.getAll().forEach(all::add);
        return all;
    }

    //filtrari note
    public List<Nota> fillterValoare(int value) {
        return fillter(
                getAllAsList(),
                x -> x.getValoare() >= value,
                (x,y) -> x.getValoare() - y.getValoare()
        );
    }

    public List<Nota> fillterTema(int idTema) {
        return fillter(
                getAllAsList(),
                x -> x.getNrTema() == idTema,
                (x,y) -> x.getValoare() - y.getValoare()
        );
    }

    public List<Nota> fillterValoareEgal(int value) {
        return fillter(
                getAllAsList(),
                x -> x.getValoare() == value,
                (x,y) -> x.getValoare() - y.getValoare()
        );
    }
}

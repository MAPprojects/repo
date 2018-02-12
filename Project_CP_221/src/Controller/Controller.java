package Controller;

import Domain.Grade;
import Domain.LabHomework;
import Domain.Student;
import Service.ServiceManagerObservable;
import Utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Controller implements Observer<Event> {
    private ServiceManagerObservable smo;
    private ObservableList<Student> studentModel=FXCollections.observableArrayList();
    private ObservableList<LabHomework> homeworkModel=FXCollections.observableArrayList();
    private ObservableList<Grade> gradeModel=FXCollections.observableArrayList();

    public Controller(ServiceManagerObservable smo) {
        this.smo = smo;
        smo.getAllStudents().forEach(x->studentModel.add(x));
        smo.getAllHomework().forEach(x->homeworkModel.add(x));
        smo.getAllGrades().forEach(x->gradeModel.add(x));
    }

    public Controller()
    {
    }


    public ServiceManagerObservable getServiceManagerObs() {
        return smo;
    }

    public void setServieManagerObs(ServiceManagerObservable smo) {
        this.smo = smo;
    }

    private void notifyEvent(StudentEvent el) {
        studentModel.clear();
        smo.getAllStudents().forEach(x->studentModel.add(x));
    }

    private void notifyEvent(HomeworkEvent el) {
        homeworkModel.clear();
        smo.getAllHomework().forEach(x->homeworkModel.add(x));
    }
    private void notifyEvent(GradeEvent el) {
        gradeModel.clear();
        smo.getAllGrades().forEach(x->gradeModel.add(x));
    }

    public ObservableList<LabHomework> getHomeworkModel() {
        return homeworkModel;
    }

    public ObservableList<Grade> getGradeModel() {
        return gradeModel;
    }

    public ObservableList<Student> getStudentModel() {
        return studentModel;
    }

    public void setStudentModel(ObservableList<Student> model){studentModel=model;}

    @Override
    public void notify(Event el) {

        if (el instanceof StudentEvent)
            notifyEvent((StudentEvent) el);
        else if(el instanceof HomeworkEvent)
            notifyEvent((HomeworkEvent)el);
        else
            notifyEvent((GradeEvent)el);

    }
}

package controller;

import domain.TemaLaborator;
import domain.User;
import utils.ListEvent;
import java.util.List;

public class ControllerTemeNepredate extends ControllerTemeLaborator {

    private User userStudent;

    public void setUserStudent(User userStudent) {
        this.userStudent = userStudent;
    }

    @Override
    public void initialize(){
        super.initialize();
        tableViewTeme.setEditable(false);
        imageAdd.setVisible(false);
        imageUpdate.setVisible(false);
    }

    @Override
    public void setDataInit(){
        this.data.setAll(service.getTemeNepredateForEmail(userStudent.getEmail()));
    }

    @Override
    protected List<TemaLaborator> getDataInit(){
        return service.getTemeNepredateForEmail(userStudent.getEmail());
    }

}

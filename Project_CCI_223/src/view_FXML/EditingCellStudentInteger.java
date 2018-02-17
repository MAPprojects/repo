package view_FXML;

import domain.Student;

public class EditingCellStudentInteger extends EditingCell<Student,Integer> {
    @Override
    public Integer covertToType(String text) {
        return Integer.parseInt(text);
    }
}

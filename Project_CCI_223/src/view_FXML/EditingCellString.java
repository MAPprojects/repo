package view_FXML;

import domain.Student;

public class EditingCellString extends EditingCell<Student,String> {
    @Override
    public String covertToType(String text) {
        return text;
    }
}

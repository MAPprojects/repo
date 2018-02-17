package domain;

import javafx.scene.control.CheckBox;

public class StudentWithCheckBox extends Student {

    private CheckBox selectedCheckBox;

    /**
     * Constructor
     *
     * @param idStudent                              int nr matricol al studentului
     * @param nume                                   String
     * @param grupa                                  int
     * @param email                                  String
     * @param cadru_didactic_indrumator_de_laborator String
     */
    public StudentWithCheckBox(int idStudent, String nume, int grupa, String email, String cadru_didactic_indrumator_de_laborator) {
        super(idStudent, nume, grupa, email, cadru_didactic_indrumator_de_laborator);
        selectedCheckBox=new CheckBox();
    }

    public CheckBox getSelectedCheckBox() {
        return selectedCheckBox;
    }

    public void setSelectedCheckBox(CheckBox selectedCheckBox) {
        this.selectedCheckBox = selectedCheckBox;
    }
}

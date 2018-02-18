package mainpackage.view;

import com.google.common.collect.SortedSetMultimap;
import com.itextpdf.kernel.pdf.PdfDocument;

import com.itextpdf.text.*;
import com.itextpdf.text.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import mainpackage.domain.Student;
import mainpackage.exceptions.MyException;
import mainpackage.service.Service;
import mainpackage.utils.ListEvent;
import mainpackage.utils.Observer;


import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainController implements Observer<Student> {
    private Service service;
    private StudentController studentController;
    private HomeworkController homeworkController;
    private GradeController gradeController;

    @FXML
    private AnchorPane globalWindow;

    public MainController() {
    }

    public void initSubControllers() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/mainpackage/view/StudentWindow.fxml"));
        AnchorPane rootLayout = null;
        try
        {
            rootLayout = (AnchorPane) loader.load();
            studentController= loader.getController();
            studentController.setService(service);
            service.addObserver(studentController);
            studentController.initData();
            globalWindow.getChildren().clear();
            globalWindow.getChildren().add(rootLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(getClass().getResource("/mainpackage/view/HomeworkWindow.fxml"));
        AnchorPane rootLayout2 = null;
        try
        {
            rootLayout2 = (AnchorPane) loader2.load();
            homeworkController= loader2.getController();
            homeworkController.setService(service);
            service.addObserver(homeworkController);
            homeworkController.initData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FXMLLoader loader3 = new FXMLLoader();
        loader3.setLocation(getClass().getResource("/mainpackage/view/GradeWindow.fxml"));
        AnchorPane rootLayout3 = null;
        try
        {
            rootLayout3 = (AnchorPane) loader3.load();
            gradeController= loader3.getController();
            gradeController.setService(service);
            service.addObserver(gradeController);
            gradeController.initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setService(Service ser){
        service=ser;
    }

    @Override
    public void notifyEvent(ListEvent<Student> e) {
//        student_model.setAll(StreamSupport.stream(e.getList().spliterator(), false).collect(Collectors.toList()));
    }

//    private Student getStudentFromView()
//    {
//        String id = view.student_id_field.getText().trim();
//        String name = view.student_name_field.getText();
//        String teacher = view.student_teacher_field.getText();
//        String email = view.student_email_field.getText();
//
//        return new Student(name, Integer.parseInt(id), email, teacher);
//    }

//    public void handleAddStudent(ActionEvent actionEvent) {
//
//        try {
//            Student new_student = getStudentFromView();
//            Student st =getStudentFromView();
//            service.add_student(st);
//        } catch (MyException e) {
//
//            showErrorMessage("Student could not be added!, error: " + e.getMessage());
//        }
//    }
//
//    public void handleDeleteStudent(ActionEvent actionEvent) {
//        Student selected_item =  view.student_table.getSelectionModel().getSelectedItem();
//        if (selected_item != null) {
//            Integer student_id = selected_item.getId();
//            Student old_student;
//            try {
//                old_student = service.delete_student(student_id);
//            } catch (MyException e) {
//                showErrorMessage("Student could not be deleted!, error: " + e.getMessage());
//                return;
//            }
//            if (old_student == null)
//                showErrorMessage("There is no student with that id.");
//        }
//    }
//
//    public void handleUpdateStudent(ActionEvent actionEvent) {
//        Student new_student = getStudentFromView();
//        Student st =getStudentFromView();
//        try {
//            service.update_student(st);
//        } catch (MyException e) {
//            showErrorMessage("Student could not be updated!, error: " + e.getMessage());
//        }
//    }
//
//    public void handleClearFields(ActionEvent actionEvent) {
//        view.student_id_field.clear();
//        view.student_teacher_field.clear();
//        view.student_email_field.clear();
//        view.student_name_field.clear();
//        view.student_id_field.setDisable(false);
//    }
//
//    public void showStudent(Student newValue) {
//        if(newValue != null)
//        {
//            view.student_id_field.setText(newValue.getId().toString());
//            view.student_id_field.setDisable(true);
//            view.student_name_field.setText(newValue.getName());
//            view.student_email_field.setText(newValue.getEmail());
//            view.student_teacher_field.setText(newValue.getTeacher());
//        }
//    }

    static void showErrorMessage(String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error");
        message.setContentText(text);
        message.showAndWait();
    }

    static void showMessage(Alert.AlertType type, String header, String text){
        Alert message=new Alert(type);
        message.setHeaderText(header);
        message.setContentText(text);
        message.showAndWait();
    }

    public void showStudentWindow(ActionEvent actionEvent) {
        System.out.println("SHOW STUDENT WINDOW");
        globalWindow.getChildren().clear();
        globalWindow.getChildren().add(studentController.getWindow());
        studentController.lastOperationLabel.setText("");
    }

    public void showHomeworkWindow(ActionEvent actionEvent) {
        System.out.println("SHOW Homework WINDOW");
        globalWindow.getChildren().clear();
        globalWindow.getChildren().add(homeworkController.getWindow());
        homeworkController.lastOperationLabel.setText("");
    }

    public void showGradeWindow(ActionEvent actionEvent) {
        System.out.println("SHOW Grade WINDOW");
        gradeController.refreshBoxes();
        globalWindow.getChildren().clear();
        globalWindow.getChildren().add(gradeController.getWindow());
        gradeController.lastOperationLabel.setText("");
    }

    public void pdf_top_studenti(ActionEvent actionEvent) {
        String file_name = "top_studenti_map.pdf";

        PdfWriter writer = null;
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file_name));
            document.open();
            document.add(new Paragraph("Topul Studentilor la laboratorul disciplinei METODE AVANSATE DE PROGRAMARE"));
            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );

            List list1 = new List(List.ORDERED);
            list1.setFirst(1);

//            SortedSet<Pair<Float,String>> average = service.get_top_studenti();

            SortedSetMultimap<Float,String> average = service.get_top_studenti();
            for(Float grade: average.keySet())
                for(String name: average.get(grade))
                list1.add(String.format("  %.2f   %s", grade,name));

            document.add(list1);
            document.close();
        }
        catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void pdf_teme(ActionEvent mouseEvent) {
        System.out.println("GENERATING PDF TEME");
        String file_name = "top_teme.pdf";

        PdfWriter writer = null;
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file_name));
            document.open();
            document.add(new Paragraph("Cele mai grele teme la MAP:"));
            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );

            List list1 = new List(List.ORDERED);
            list1.setFirst(1);

            SortedSetMultimap<Float,String> average = service.get_top_teme();
            for(Float grade: average.keySet())
                for(String name: average.get(grade))
                    list1.add(String.format(" %.2f   %s", grade, name));

            document.add(list1);
            document.close();
        }
        catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void top_grupe(ActionEvent actionEvent) {
        System.out.println("GENERATING PDF TOP GRUPE");
        String file_name = "top_grupe.pdf";

        PdfWriter writer = null;
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file_name));
            document.open();
            document.add(new Paragraph("Topul grupelor la disciplina MAP:"));
            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );

            List list1 = new List(List.ORDERED);
            list1.setFirst(1);

            SortedSetMultimap<Float,String> average = service.get_top_grupe();
            for(Float grade: average.keySet())
                for(String name: average.get(grade))
                    list1.add(String.format("Grupa %s are media  %.2f", name, grade));

            document.add(list1);
            document.close();
        }
        catch (FileNotFoundException | DocumentException e) {
            e.printStackTrace();
        }
    }
}

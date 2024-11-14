package com.imranpranto;

import com.imranpranto.data.AppQuery;
import com.imranpranto.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class StudentController implements Initializable {
    private static Stage stage;
    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        StudentController.stage = stage;
    }

    @Override
    public void initialize(URL ul, ResourceBundle rb) {
        showStudents();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        fieldSearch.textProperty().addListener((ObservableList, oldValue,newValue)->{
            filterData(newValue);
        });
    }

    @FXML
    public TextField fieldFirstname;

    @FXML
    public TextField fieldMiddlename;

    @FXML
    public TextField fieldLastname;

    @FXML
    public TextField fieldSearch;

    @FXML
    public Button btnNew;

    @FXML
    public Button btnSave;

    @FXML
    public Button btnUpdate;

    @FXML
    public Button btnDelete;

        @FXML
    private Button btnPdf; // PDF Button

    @FXML
    public TableView<Student> tableView;

    @FXML
    public TableColumn<Student, Integer> colId;

    @FXML
    public TableColumn<Student, String> colFirstname;
    @FXML
    public TableColumn<Student, String> colMiddlename;
    @FXML
    public TableColumn<Student, String> colLastname;

    private Student student;

    @FXML
    private void addStudent() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add confirmation");
        dialog.setHeaderText("Are you sure you want to save?");
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getStage());
        Label label = new Label("Name: " + fieldFirstname.getText() + " " + fieldLastname.getText());
        dialog.getDialogPane().setContent(label);
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == okButton) {

            Student student = new Student(fieldFirstname.getText(), fieldMiddlename.getText(), fieldLastname.getText());
            AppQuery query = new AppQuery();
            query.addStudent(student);
            showStudents();
        }

    }

    @FXML
    private void showStudents() {
        AppQuery query = new AppQuery();
        ObservableList<Student> list = query.getStudentList();
        colId.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<Student, String>("firstname"));
        colMiddlename.setCellValueFactory(new PropertyValueFactory<Student, String>("middlename"));
        colLastname.setCellValueFactory(new PropertyValueFactory<Student, String>("lastname"));
        tableView.setItems(list);
    }
 @FXML
    private void generatePdf() {
        // File chooser dialog to select location to save PDF
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("StudentData.pdf");

        // Get file path from user
        java.io.File file = fileChooser.showSaveDialog(getStage());
        if (file != null) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                Document document = new Document();
                PdfWriter.getInstance(document, fos);
                document.open();

                // Adding title
                document.add(new Paragraph("Student Data"));
                document.add(new Paragraph(" ")); // Blank line for spacing

                // Creating a table with 4 columns for Student data
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                
                // Adding table headers
                Stream.of("ID", "First Name", "Middle Name", "Last Name").forEach(header -> {
                    PdfPCell cell = new PdfPCell(new Phrase(header));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(cell);
                });

                // Adding rows from tableView data
                for (Student student : tableView.getItems()) {
                    table.addCell(String.valueOf(student.getId()));
                    table.addCell(student.getFirstname());
                    table.addCell(student.getMiddlename());
                    table.addCell(student.getLastname());
                }

                // Add table to document
                document.add(table);
                document.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PDF Export");
                alert.setHeaderText(null);
                alert.setContentText("PDF file generated successfully!");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    public void mouseClicked(MouseEvent e) {
        try {
            // Directly assign the selected student from the TableView
            this.student = tableView.getSelectionModel().getSelectedItem();

            // Check if student is selected to avoid NullPointerException
            if (this.student != null) {
                fieldFirstname.setText(this.student.getFirstname());
                fieldMiddlename.setText(this.student.getMiddlename());
                fieldLastname.setText(this.student.getLastname());
                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
                btnSave.setDisable(true);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void updateStudent() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update confirmation");
            dialog.setHeaderText("Are you sure you want to update?");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(getStage());

            Label label = new Label("Name: " + fieldFirstname.getText() + " " + fieldLastname.getText());
            dialog.getDialogPane().setContent(label);
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == okButton) {

                // if (this.student != null) {
                AppQuery query = new AppQuery();

                // Update student details in the current object
                this.student.setFirstname(fieldFirstname.getText());
                this.student.setMiddlename(fieldMiddlename.getText());
                this.student.setLastname(fieldLastname.getText());

                query.updateStudent(this.student);
                showStudents();
                clearFields();
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
                btnSave.setDisable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteStudent() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Delete confirmation");
            dialog.setHeaderText("Are you sure you want to delete?");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(getStage());

            Label label = new Label("Name: " + fieldFirstname.getText() + " " + fieldLastname.getText());
            dialog.getDialogPane().setContent(label);
            ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == okButton) {

                // if (this.student != null) {
                AppQuery query = new AppQuery();
                query.deleteStudent(this.student);
                showStudents();
                clearFields();
                btnUpdate.setDisable(true);
                btnDelete.setDisable(true);
                btnSave.setDisable(false);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        fieldFirstname.setText("");
        fieldMiddlename.setText("");
        fieldLastname.setText("");
        this.student = null;
    }

    @FXML
    private void clickNew() {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        clearFields();
        btnSave.setDisable(false);
    }

    private void filterData(String searchName) {
        ObservableList<Student> filterData = FXCollections.observableArrayList();
        AppQuery query = new AppQuery();
        ObservableList<Student> list = query.getStudentList();
        for (Student student : list) {
            if (student.getFirstname().toLowerCase().contains(searchName.toLowerCase())
                    || student.getMiddlename().toLowerCase().contains(searchName.toLowerCase()) ||
                    student.getLastname().toLowerCase().contains(searchName.toLowerCase())) {
                            filterData.add(student);
            }

        }
        tableView.setItems(filterData);
    }
}

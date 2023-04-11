package main.otherPage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.Utils;
import main.models.Student;
import main.profilePage.StudentDAO;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;

import java.net.URL;
import java.util.ResourceBundle;

public class EmailController implements Initializable {
    @FXML
    private TextArea msgBox;

    @FXML
    private TextField toTF;

    @FXML
    private TextField subjectTF;

    @FXML
    private Button reciptBtn;

    @FXML
    private Button sendBtn;

    @FXML
    private ListView<Student> reciptListView;

    private StringBuffer recipients = new StringBuffer();

    // Host email credential for sending email
    final String FROM = "algoclub.cosc3506@gmail.com";
    final String USERNAME = "algoclub.cosc3506@gmail.com";
    final String PASSWORD = "qsgzvjuttoltwiwr";

    private ObservableList<Student> studList = FXCollections.observableArrayList();

    private ObservableList<String> selectedStuds = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuration for email properties
        config();

        // display recipients lists
        displayReciptLists();

        // add new recipients
        reciptBtn.setOnMouseClicked((MouseEvent event) -> {
            // add selected email to text-field
            addToRecipients();
        });
    }

    private void addToRecipients() {
        // clear recipients
        recipients = new StringBuffer();

        // append each email address selected to recipients
        selectedStuds.forEach((stud_email) -> {
            recipients.append(stud_email).append(", ");
        });

        // set to toTF
        toTF.setText(String.valueOf(recipients));
    }

    private void config() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        sendBtn.setOnAction(event -> sendEmail(session));
    }

    private void sendEmail(Session session) {
        // prepare to check and insert to database
        String reciptList = toTF.getText();
        String subText = subjectTF.getText();
        String msg = msgBox.getText();

        // simple validation on inputs not empty
        if (reciptList.isEmpty() || msg.isEmpty() || subText.isEmpty()) {
            // promote error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Missing Contents", ButtonType.OK);
            alert.showAndWait();
        } else {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(FROM));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(reciptList)
                );
                message.setSubject(subText);
                message.setText(msg);

                Transport.send(message);

                // display message
                Utils.showInfo("Sent Mail", "You are successful sent a email !");
                // close this window
                Stage stage = (Stage) sendBtn.getScene().getWindow();
                // do what you have to do
                stage.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    // display lists
    private void displayReciptLists() {
        try {
            //Get all students
            studList = StudentDAO.displayStudents();
            //Populate Students on Listview
            reciptListView.setItems(studList);

            // Customize list which checkbox, and save to selected list
            reciptListView.setCellFactory((ListView<Student> param) -> new ListCell<Student>() {
                @Override
                public void updateItem(Student stud, boolean empty) {
                    super.updateItem(stud, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        CheckBox checkBox = new CheckBox(stud.getStudentName() + " <" + stud.getStudentEmail() + ">");

                        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                            if (isNowSelected) {
                                selectedStuds.add(stud.getStudentEmail());
                            } else {
                                selectedStuds.remove(stud.getStudentEmail());
                            }
                        });
                        setGraphic(checkBox);
                    }
                }
            });
        } catch(SQLException e) {
            System.out.println("Error occurred while getting events information from DB.\n" + e);
        }
    }

    public void setRecipients(ObservableList<String> emailLists) {
        selectedStuds = emailLists;

        //addToRecipients()
        addToRecipients();
    }
}

package com.example.mentorr;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class FXMLDocumentController implements Initializable {
    @FXML
    private TextField Color;

    
    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane login_form;

    @FXML
    private TextField login_username;

    @FXML
    private PasswordField login_password;

    @FXML
    private TextField login_showPassword;

    @FXML
    private CheckBox login_selectShowPassword;

    @FXML
    private Button login_btn;

    @FXML
    private Button login_createAccount;

    @FXML
    private Hyperlink login_forgotPassword;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private TextField signup_email;

    @FXML
    private TextField signup_username;

    @FXML
    private PasswordField signup_password;

    @FXML
    private PasswordField signup_cPassword;

    @FXML
    private Button signup_btn;

    @FXML
    private Button signup_loginAccount;

    @FXML
    private ComboBox<?> roleComboBox;

    @FXML
    private TextField FieldAnswerr;

    @FXML
    private AnchorPane forgot_form;

    @FXML
    private TextField forgot_answer;

    @FXML
    private Button forgot_proceedBtn;

    @FXML
    private Button forgot_backBtn;

    @FXML
    private ComboBox<?> forgot_selectQuestion;

    @FXML
    private TextField forgot_username;

    @FXML
    private AnchorPane changePass_form;

    @FXML
    private Button changePass_proceedBtn;

    @FXML
    private Button changePass_backBtn;

    @FXML
    private PasswordField changePass_password;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField changePass_cPassword;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    public Connection connectDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect
                    = DriverManager.getConnection("jdbc:mysql://localhost:3306/mentor", "root", "1404@Mimi");
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void login() {
        alertMessage alert = new alertMessage();

        // CHECK IF ALL OR SOME OF THE FIELDS ARE EMPTY
        if (login_username.getText().isEmpty() || login_password.getText().isEmpty()) {
            alert.errorMessage("Incorrect Username/Password");
        } else {
            String selectData = "SELECT * FROM users WHERE username = ? and password = ?"; // users IS OUR TABLE NAME

            connect = connectDB();

            if(login_selectShowPassword.isSelected()){
                login_password.setText(login_showPassword.getText());
            }else{
                login_showPassword.setText(login_password.getText());
            }

            try {
                prepare = connect.prepareStatement(selectData);
                prepare.setString(1, login_username.getText());
                prepare.setString(2, login_password.getText());

                result = prepare.executeQuery();

                if (result.next()) {
                    // Set the username in UserSession after successful login
                    UserSession.setUsername(login_username.getText());

                    // ONCE ALL DATA THAT USERS INSERT ARE CORRECT, THEN WE WILL PROCEED TO OUR MAIN FORM
                    alert.successMessage("Successfully Login!");
                    // TO LINK THE MAIN FORM
                    Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    // TO SHOW OUR MAIN FORM
                    stage.show();

                    // TO HIDE WINDOW OF LOGIN FORM
                    ((Stage) login_btn.getScene().getWindow()).hide();

                } else {
                    // ELSE, THEN ERROR MESSAGE WILL APPEAR
                    alert.errorMessage("Incorrect Username/Password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void showPassword() {

        if (login_selectShowPassword.isSelected()) {
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_password.setVisible(true);
        }

    }

    public void forgotPassword() {

        alertMessage alert = new alertMessage();

        if (forgot_username.getText().isEmpty()
                || forgot_selectQuestion.getSelectionModel().getSelectedItem() == null
                || forgot_answer.getText().isEmpty()) {
            alert.errorMessage("Please fill all blank fields");
        } else {

            String checkData = "SELECT username, question, answer FROM users "
                    + "WHERE username = ? AND question = ? AND answer = ?";

            connect = connectDB();

            try {

                prepare = connect.prepareStatement(checkData);
                prepare.setString(1, forgot_username.getText());
                prepare.setString(2, (String) forgot_selectQuestion.getSelectionModel().getSelectedItem());
                prepare.setString(3, forgot_answer.getText());

                result = prepare.executeQuery();
                // IF CORRECT
                if (result.next()) {
                    // PROCEED TO CHANGE PASSWORD
                    signup_form.setVisible(false);
                    login_form.setVisible(false);
                    forgot_form.setVisible(false);
                    changePass_form.setVisible(true);
                } else {
                    alert.errorMessage("Incorrect information");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void forgotListQuestion(){

        List<String> listQ = new ArrayList<>();

        for(String data : questionList){
            listQ.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listQ);
        forgot_selectQuestion.setItems(listData);

    }

    public void register() {
        alertMessage alert = new alertMessage();

        // Validation for required fields
        if (signup_email.getText().isEmpty() || signup_username.getText().isEmpty() || nameField.getText().isEmpty() ||
                signup_password.getText().isEmpty() || signup_cPassword.getText().isEmpty() ||
                roleComboBox.getSelectionModel().getSelectedItem() == null ||
                FieldAnswerr.getText().isEmpty() || Color.getText().isEmpty()) {
            alert.errorMessage("All fields are necessary to be filled");
            return;
        }

        // Check if password matches confirmation password
        if (!signup_password.getText().equals(signup_cPassword.getText())) {
            alert.errorMessage("Passwords do not match");
            return;
        }

        // Check password length
        if (signup_password.getText().length() < 8) {
            alert.errorMessage("Invalid Password, at least 8 characters needed");
            return;
        }

        String selectedRole = (String) roleComboBox.getSelectionModel().getSelectedItem();
        String role = selectedRole.equals("I AM A MENTOR") ? "Mentor" : "Student";

        // Check if the username is already taken
        connect = connectDB();
        try {
            String checkUsername = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = connect.prepareStatement(checkUsername);
            checkStmt.setString(1, signup_username.getText());
            ResultSet result = checkStmt.executeQuery();

            if (result.next()) {
                alert.errorMessage(signup_username.getText() + " is already taken");
                return;
            }

            // Insert new user data
            String insertData = "INSERT INTO users (email, username, password, role, name) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = connect.prepareStatement(insertData, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, signup_email.getText());
            insertStmt.setString(2, signup_username.getText());
            insertStmt.setString(3, signup_password.getText());
            insertStmt.setString(4, role);
            insertStmt.setString(5, nameField.getText());
            insertStmt.executeUpdate();

            // Handle mentor specific data
            if (role.equals("Mentor")) {
                ResultSet keys = insertStmt.getGeneratedKeys();
                if (keys.next()) {
                    long userId = keys.getLong(1);
                    String insertMentor = "INSERT INTO Mentors (mentor_id, field, color) VALUES (?, ?, ?)";
                    PreparedStatement mentorStmt = connect.prepareStatement(insertMentor);
                    mentorStmt.setLong(1, userId);
                    mentorStmt.setString(2, FieldAnswerr.getText());
                    mentorStmt.setString(3, Color.getText());
                    mentorStmt.executeUpdate();
                }
            }

            alert.successMessage("Registered Successfully!");
            registerClearFields();
            signup_form.setVisible(false);
            login_form.setVisible(true);

        } catch (SQLException e) {
            alert.errorMessage("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (connect != null) connect.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }



    // TO CLEAR ALL FIELDS OF REGISTRATION FORM
    public void registerClearFields() {
        signup_email.setText("");
        signup_username.setText("");
        signup_password.setText("");
        signup_cPassword.setText("");
        roleComboBox.getSelectionModel().clearSelection();
        FieldAnswerr.setText("");
    }

    public void changePassword(){

        alertMessage alert = new alertMessage();
        // CHECK ALL FIELDS IF EMPTY OR NOT
        if(changePass_password.getText().isEmpty() || changePass_cPassword.getText().isEmpty()){
            alert.errorMessage("Please fill all blank fields");
        }else if(!changePass_password.getText().equals(changePass_cPassword.getText())){
            // CHECK IF THE PASSWORD AND CONFIRMATION ARE NOT MATCH
            alert.errorMessage("Password does not match");
        }else if(changePass_password.getText().length() < 8){
            // CHECK IF THE LENGTH OF PASSWORD IS LESS THAN TO 8
            alert.errorMessage("Invalid Password, at least 8 characters needed");
        }else{
            // USERNAME IS OUR REFERENCE TO UPDATE THE DATA OF THE USER
            String updateData = "UPDATE users SET password = ?, update_date = ? "
                    + "WHERE username = '" + forgot_username.getText() + "'";

            connect = connectDB();

            try{

                prepare = connect.prepareStatement(updateData);
                prepare.setString(1, changePass_password.getText());

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                prepare.setString(2, String.valueOf(sqlDate));

                prepare.executeUpdate();
                alert.successMessage("Succesfully changed Password");

                // LOGIN FORM WILL APPEAR
                signup_form.setVisible(false);
                login_form.setVisible(true);
                forgot_form.setVisible(false);
                changePass_form.setVisible(false);

                login_username.setText("");
                login_password.setVisible(true);
                login_password.setText("");
                login_showPassword.setVisible(false);
                login_selectShowPassword.setSelected(false);

                changePass_password.setText("");
                changePass_cPassword.setText("");

            }catch(Exception e){e.printStackTrace();}

        }

    }

    public void switchForm(ActionEvent event) {

        // THE REGISTRATION FORM WILL BE VISIBLE
        if (event.getSource() == signup_loginAccount || event.getSource() == forgot_backBtn) {
            signup_form.setVisible(false);
            login_form.setVisible(true);
            forgot_form.setVisible(false);
            changePass_form.setVisible(false);
        } else if (event.getSource() == login_createAccount) { // THE LOGIN FORM WILL BE VISIBLE
            signup_form.setVisible(true);
            login_form.setVisible(false);
            forgot_form.setVisible(false);
            changePass_form.setVisible(false);
        } else if (event.getSource() == login_forgotPassword) {
            signup_form.setVisible(false);
            login_form.setVisible(false);
            forgot_form.setVisible(true);
            changePass_form.setVisible(false);
            // TO SHOW THE DATA TO OUR COMBOBOX
            forgotListQuestion();
        } else if (event.getSource() == changePass_backBtn) {
            signup_form.setVisible(false);
            login_form.setVisible(false);
            forgot_form.setVisible(true);
            changePass_form.setVisible(false);
        }

    }

    private String[] questionList = {"I AM A MENTOR", "I AM A STUDENT"};

    public void questions() {
        List<String> listQ = new ArrayList<>();

        for (String data : questionList) {
            listQ.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listQ);
        roleComboBox.setItems(listData);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        questions();

        forgotListQuestion();
    }

}

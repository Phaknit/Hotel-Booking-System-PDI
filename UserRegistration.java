package PDIproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class UserRegistration extends Application {
    private TextField fnameField, lnameField, emailField, phoneNumField, dateOfBirthField, usernameField;
    private PasswordField passwordField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sign Up");
        primaryStage.setFullScreen(true); // Enable full-screen mode

        Label titleLabel = new Label("Sign up Your Account");
        titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #323296;");

        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setVgap(15);
        formGrid.setHgap(15);
        formGrid.setAlignment(Pos.CENTER);

        // Add form fields
        fnameField = addTextField(formGrid, "First Name:", 0);
        lnameField = addTextField(formGrid, "Last Name:", 1);
        emailField = addTextField(formGrid, "Email:", 2);
        phoneNumField = addTextField(formGrid, "Phone Number:", 3);
        dateOfBirthField = addTextField(formGrid, "Date of Birth (yyyy-MM-dd):", 4);
        usernameField = addTextField(formGrid, "Username:", 5);

        formGrid.add(new Label("Password:"), 0, 6);
        passwordField = new PasswordField();
        passwordField.setStyle("-fx-font-size: 22px;");
        formGrid.add(passwordField, 1, 6);

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        registerButton.setStyle("-fx-background-color: #22A7F0; -fx-text-fill: white; -fx-font-size: 24px;");
        backButton.setStyle("-fx-background-color: #F22613; -fx-text-fill: white; -fx-font-size: 24px;");

        HBox buttonBox = new HBox(30, registerButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(30, titleLabel, formGrid, buttonBox);
        layout.setPadding(new Insets(50));
        layout.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(layout);
        StackPane.setAlignment(layout, Pos.CENTER);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> layout.setPrefWidth(newVal.doubleValue()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> layout.setPrefHeight(newVal.doubleValue()));

        registerButton.setOnAction(e -> {
            if (validateUserInput()) {
                if (saveToDatabase()) {
                    showAlert("Registration Successful!", Alert.AlertType.INFORMATION);
                    new UserLogin().start(new Stage()); // Open LoginGUI after registration
                    primaryStage.close();
                }
            }
        });

        backButton.setOnAction(e -> primaryStage.close());
    }

    private TextField addTextField(GridPane grid, String label, int row) {
        grid.add(new Label(label), 0, row);
        TextField textField = new TextField();
        grid.add(textField, 1, row);
        return textField;
    }

    private boolean validateUserInput() {
        String fname = fnameField.getText().trim();
        String lname = lnameField.getText().trim();
        String email = emailField.getText().trim();
        String phoneNum = phoneNumField.getText().trim();
        String dateOfBirth = dateOfBirthField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (!fname.matches("^[A-Za-z]+$")) return showError("Invalid first name.");
        if (!lname.matches("^[A-Za-z]+$")) return showError("Invalid last name.");
        if (!Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$").matcher(email).matches())
            return showError("Invalid email format.");
        if (!phoneNum.matches("^\\d{8,10}$")) return showError("Invalid phone number.");
        if (!dateOfBirth.matches("^\\d{4}-\\d{2}-\\d{2}$")) return showError("Invalid date format (yyyy-MM-dd).");
        if (username.isEmpty()) return showError("Username cannot be empty.");
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"))
            return showError("Password must be at least 8 characters with letters & numbers.");

        return true;
    }

    private boolean showError(String message) {
        showAlert(message, Alert.AlertType.ERROR);
        return false;
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }

    private boolean saveToDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:RegistrationInfo.db")) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "firstname TEXT, " +
                    "lastname TEXT, " +
                    "email TEXT UNIQUE, " +
                    "phone_number TEXT, " +
                    "date_of_birth TEXT, " +
                    "username TEXT UNIQUE, " +
                    "password TEXT)";
            conn.createStatement().execute(createTableSQL);

            String insertSQL = "INSERT INTO users (firstname, lastname, email, phone_number, date_of_birth, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, fnameField.getText().trim());
                pstmt.setString(2, lnameField.getText().trim());
                pstmt.setString(3, emailField.getText().trim());
                pstmt.setString(4, phoneNumField.getText().trim());
                pstmt.setString(5, dateOfBirthField.getText().trim());
                pstmt.setString(6, usernameField.getText().trim());
                pstmt.setString(7, passwordField.getText().trim());
                pstmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                showError("The email or username already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

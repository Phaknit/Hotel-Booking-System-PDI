package PDIproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class UserLogin extends Application {
    public static TextField usernameField;
    private PasswordField passwordField;
    private static final String DB_URL = "jdbc:sqlite:RegistrationInfo.db";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("User Login");
        primaryStage.setFullScreen(true);

        createTableIfNotExists();

        Label titleLabel = new Label("Login to Your Account");
        titleLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: #323296;");

        // Username Row
        Label userLabel = new Label("Username:");
        userLabel.setStyle("-fx-font-size: 24px;");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-font-size: 22px;");
        usernameField.setPrefWidth(400);

        HBox userRow = new HBox(20, userLabel, usernameField);
        userRow.setAlignment(Pos.CENTER);

        // Password Row
        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-font-size: 24px;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-font-size: 22px;");
        passwordField.setPrefWidth(400);

        HBox passRow = new HBox(20, passLabel, passwordField);
        passRow.setAlignment(Pos.CENTER);

        // Buttons Row
        Button loginButton = new Button("Login");
        Button backButton = new Button("Back");

        loginButton.setStyle("-fx-background-color: #22A7F0; -fx-text-fill: white; -fx-font-size: 24px;");
        loginButton.setPrefSize(200, 60);

        backButton.setStyle("-fx-background-color: #F22613; -fx-text-fill: white; -fx-font-size: 24px;");
        backButton.setPrefSize(200, 60);

        HBox buttonRow = new HBox(50, loginButton, backButton);
        buttonRow.setAlignment(Pos.CENTER);

        // Button Actions
        loginButton.setOnAction(e -> authenticateUser(primaryStage));
        backButton.setOnAction(e -> {
            new HotelWelcomePage().start(new Stage());
            primaryStage.close();
        });

        VBox layout = new VBox(40, titleLabel, userRow, passRow, buttonRow);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));

        StackPane root = new StackPane(layout);
        StackPane.setAlignment(layout, Pos.CENTER);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ensure full-screen responsiveness
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> layout.setPrefWidth(newVal.doubleValue()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> layout.setPrefHeight(newVal.doubleValue()));
    }

    private void authenticateUser(Stage primaryStage) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (validateUsername(username) && validatePassword(password)) {
            if (checkCredentials(username, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful! Welcome, " + username);
                logUserActivity(username, "Login SUCCESS");

                new HotelMainPage().start(new Stage());;
                primaryStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid username or password!");
                logUserActivity(username, "Login FAILED");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Username and password must be valid!");
        }
    }

    private void logUserActivity(String username, String activity) {
        String query = "INSERT INTO login_activity (username, activity) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, activity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS login_activity (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "activity TEXT NOT NULL, " +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateUsername(String username) {
        return username != null && !username.trim().isEmpty();
    }

    private boolean validatePassword(String password) {
        return password != null && password.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

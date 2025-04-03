package PDIproject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HotelWelcomePage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Welcome Page");
        primaryStage.setFullScreen(true); // Make window full screen
        primaryStage.setFullScreenExitHint(""); // Hide exit message

        // Set custom logo (handle invalid path gracefully)
        try {
            primaryStage.getIcons().add(new Image("file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-26_23-22-57.jpg"));
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Logo image not found!");
        }

        // Header Panel (Full Width)
        HBox headerBox = new HBox();
        headerBox.setStyle("-fx-background-color: #222222;");
        headerBox.setPrefHeight(100);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new javafx.geometry.Insets(20));

        Text titleLabel = new Text("APSARA PARADISE RESIDENCE");
        titleLabel.setFill(Color.WHITE);
        titleLabel.setFont(Font.font("Times New Roman", 36));
        headerBox.getChildren().add(titleLabel);

        // Banner Image (Fit to Screen Width)
        ImageView bannerImageView = new ImageView();
        try {
            Image backgroundImage = new Image("file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-26_23-21-28.jpg");
            bannerImageView.setImage(backgroundImage);
            bannerImageView.setPreserveRatio(true);
            bannerImageView.setSmooth(true);
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Banner image not found!");
        }

        StackPane bannerPane = new StackPane(bannerImageView);
        bannerPane.setPrefHeight(400);
        bannerImageView.fitWidthProperty().bind(primaryStage.widthProperty());

        // Welcome Panel (Centered Text)
        VBox welcomePanel = new VBox(20);
        welcomePanel.setAlignment(Pos.CENTER);
        welcomePanel.setStyle("-fx-background-color: white;");
        welcomePanel.setPadding(new javafx.geometry.Insets(50));

        Text welcomeLabel = new Text("Apsara Paradise Residence Welcomes You, Traveller!");
        welcomeLabel.setFont(Font.font("Times New Roman", 48));
        welcomeLabel.setFill(Color.web("#222222"));

        Text subtitleLabel = new Text("Experience a cultural heritage filled with luxury, comfort, and tranquility.");
        subtitleLabel.setFont(Font.font("Times New Roman", 22));
        subtitleLabel.setFill(Color.web("#646464"));

        // Buttons
        HBox buttonPanel = new HBox(50);
        buttonPanel.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Log In");
        loginButton.setStyle("-fx-font-size: 20; -fx-background-color: #A9A9A9; -fx-text-fill: black;");
        loginButton.setPrefSize(200, 60);
        loginButton.setOnAction(e -> {
            new UserLogin().start(new Stage());
            primaryStage.close();
        });

        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-font-size: 20; -fx-background-color: #A9A9A9; -fx-text-fill: black;");
        signUpButton.setPrefSize(200, 60);
        signUpButton.setOnAction(e -> {
            new UserRegistration().start(new Stage());
            primaryStage.close();
        });

        buttonPanel.getChildren().addAll(loginButton, signUpButton);
        welcomePanel.getChildren().addAll(welcomeLabel, subtitleLabel, buttonPanel);

        // Main Layout
        VBox root = new VBox(headerBox, bannerPane, welcomePanel);
        root.setPrefSize(primaryStage.getWidth(), primaryStage.getHeight());

        // Bind size dynamically to fit full screen
        root.prefWidthProperty().bind(primaryStage.widthProperty());
        root.prefHeightProperty().bind(primaryStage.heightProperty());

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

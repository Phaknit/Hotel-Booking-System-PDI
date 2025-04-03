package PDIproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ContactPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Root Layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // Header Panel
        Label titleLabel = new Label("APSARA PARADISE RESIDENCE");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox headerPanel = new HBox(titleLabel);
        headerPanel.setAlignment(Pos.CENTER);
        headerPanel.setPadding(new Insets(10, 20, 10, 20));
        headerPanel.setStyle("-fx-background-color: #222222;");

        root.setTop(headerPanel);

        // Background Banner Image
        BackgroundImage bannerImage = new BackgroundImage(
                new Image("file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-26_23-21-28.jpg"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(100, 100, true, true, true, true));

        VBox bannerPanel = new VBox();
        bannerPanel.setPrefSize(900, 300);
        bannerPanel.setBackground(new Background(bannerImage));

        root.setCenter(bannerPanel);

        // Contact Information Panel
        VBox contactPanel = new VBox(10);
        contactPanel.setPadding(new Insets(50));
        contactPanel.setAlignment(Pos.CENTER);
        contactPanel.setStyle("-fx-background-color: white;");

        Label contactLabel = new Label("Contact Us Now!");
        contactLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-font-style: italic; -fx-text-fill: #222222;");

        Label addressLabel = new Label("Address: Apsara Street, Siem Reap");
        Label emailLabel = new Label("Email: @apsaraparadise.com");
        Label phoneLabel = new Label("Phone: +855 684 456 291");

        addressLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #323232;");
        emailLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #323232;");
        phoneLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #323232;");

        contactPanel.getChildren().addAll(contactLabel, addressLabel, emailLabel, phoneLabel);

        // Back Button
        Button backButton = new Button("⬅");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #333; -fx-text-fill: white; -fx-padding: 10px 20px;");
        backButton.setOnAction(e -> {
            primaryStage.close(); // Close current window
            new HotelMainPage().start(new Stage()); // Open Main Page
        });

        HBox backButtonPanel = new HBox(backButton);
        backButtonPanel.setPadding(new Insets(20));
        backButtonPanel.setAlignment(Pos.BOTTOM_LEFT); // Center the button

        // Bottom Panel Combining Contact Info & Back Button
        VBox bottomPanel = new VBox(contactPanel, backButtonPanel);
        bottomPanel.setAlignment(Pos.CENTER);

        root.setBottom(bottomPanel);

        // Set Scene & Stage
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hotel Contact Page");
        primaryStage.getIcons().add(new Image("file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-26_23-22-57.jpg"));
        primaryStage.setScene(scene);

        // Full-Screen Mode
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

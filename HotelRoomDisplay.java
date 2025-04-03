package PDIproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HotelRoomDisplay extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Room Display Page");
        primaryStage.setFullScreen(true); // Enable full-screen mode

        // Root Layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // === HEADER SECTION ===
        HBox headerPanel = new HBox();
        headerPanel.setStyle("-fx-background-color: #222222;");
        headerPanel.setPadding(new Insets(15, 30, 15, 30));
        headerPanel.setAlignment(Pos.CENTER_LEFT);

        Text titleLabel = new Text("Room Inventory ");
        titleLabel.setFont(Font.font("Serif", 28));
        titleLabel.setStyle("-fx-fill: white; -fx-font-weight: bold;");

        headerPanel.getChildren().add(titleLabel);
        root.setTop(headerPanel);

        // === BANNER IMAGE ===
        ImageView bannerImage = new ImageView();
        try {
            Image image = new Image("file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-26_23-21-28.jpg");
            bannerImage.setImage(image);
            bannerImage.setPreserveRatio(false);
            bannerImage.setFitWidth(primaryStage.getWidth());
            bannerImage.setFitHeight(primaryStage.getHeight() * 0.4);
        } catch (Exception e) {
            bannerImage.setStyle("-fx-background-color: lightgray;");
        }

        StackPane bannerPane = new StackPane(bannerImage);
        root.setCenter(bannerPane);

        // === SERVICE CARDS PANEL ===
        HBox servicesPanel = new HBox(40);
        servicesPanel.setPadding(new Insets(20));
        servicesPanel.setAlignment(Pos.CENTER);

        String[] serviceTitles = {"STANDARD ROOM", "DOUBLE BED", "DELUXE ROOM", "LUXURY SUITE", "PRIVATE VILLA"};
        String[] serviceDescriptions = {
            "A comfortable room with basic amenities.",
            "A cozy room with a large double bed.",
            "A spacious room with premium amenities.",
            "A lavish suite with high-end furnishings.",
            "A secluded villa offering total privacy."
        };
        String[] imagePaths = {
            "file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-27_12-23-58.jpg",
            "file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-27_13-47-57.jpg",
            "file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-27_13-48-57.jpg",
            "file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-27_13-50-16.jpg",
            "file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-27_13-52-57.jpg"
        };

        for (int i = 0; i < serviceTitles.length; i++) {
            VBox serviceCard = createServiceCard(serviceTitles[i], serviceDescriptions[i], imagePaths[i]);
            servicesPanel.getChildren().add(serviceCard);
        }

        // === BACK BUTTON AT BOTTOM-LEFT ===
        Button backButton = new Button("⬅");
        backButton.setStyle("-fx-font-size: 18px; -fx-background-color: #333; -fx-text-fill: white; -fx-padding: 10px 20px;");
        backButton.setOnAction(e -> {
            primaryStage.close(); // Close current window
            new HotelMainPage().start(new Stage()); // Open Main Page
        });

        HBox backButtonPanel = new HBox(backButton);
        backButtonPanel.setPadding(new Insets(20));
        backButtonPanel.setAlignment(Pos.BOTTOM_LEFT);

        // Combine Services and Back Button into a Single Panel
        VBox bottomPanel = new VBox(servicesPanel, backButtonPanel);
        bottomPanel.setAlignment(Pos.CENTER);
        root.setBottom(bottomPanel);

        // === FULL-SCREEN RESPONSIVENESS ===
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> bannerImage.setFitWidth(newVal.doubleValue()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> bannerImage.setFitHeight(newVal.doubleValue() * 0.4));

        primaryStage.show();
    }

    // === METHOD TO CREATE A SERVICE CARD ===
    private VBox createServiceCard(String title, String description, String imagePath) {
        VBox serviceCard = new VBox(10);
        serviceCard.setPadding(new Insets(10));
        serviceCard.setStyle("-fx-border-color: lightgray; -fx-border-width: 2px;");
        serviceCard.setAlignment(Pos.CENTER);

        // Load and scale the image dynamically
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(200);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        Button imgButton = new Button("", imageView);
        imgButton.setStyle("-fx-background-color: transparent;");
        imgButton.setOnAction(e -> showAlert(title));

        Text titleLabel = new Text(title);
        titleLabel.setFont(Font.font("Times New Roman", 18));
        titleLabel.setStyle("-fx-fill: #323232;");

        Text descriptionLabel = new Text(description);
        descriptionLabel.setFont(Font.font("Times New Roman", 14));
        descriptionLabel.setStyle("-fx-fill: #646464;");

        serviceCard.getChildren().addAll(imgButton, titleLabel, descriptionLabel);
        return serviceCard;
    }

    // === ALERT FOR ROOM SELECTION ===
    private void showAlert(String roomType) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Room Selection");
        alert.setHeaderText(null);
        alert.setContentText("Redirecting to " + roomType + " page.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

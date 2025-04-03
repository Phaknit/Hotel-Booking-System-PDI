package PDIproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HotelMainPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Main Page");
        primaryStage.setFullScreen(true);

        // Root Layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        // === HEADER PANEL WITH NAVIGATION BAR ===
        HBox headerPanel = new HBox();
        headerPanel.setStyle("-fx-background-color: #111111;");
        headerPanel.setPadding(new Insets(15, 30, 15, 30));
        headerPanel.setAlignment(Pos.CENTER_LEFT);
        headerPanel.setSpacing(20);

        Text titleLabel = new Text("APSARA PARADISE RESIDENCE");
        titleLabel.setFont(Font.font("Serif", 28));
        titleLabel.setStyle("-fx-fill: white; -fx-font-weight: bold;");

        // Spacer to push buttons to right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // === NAVIGATION BUTTONS AT TOP RIGHT ===
        HBox navPanel = new HBox();
        navPanel.setSpacing(15);
        navPanel.setAlignment(Pos.TOP_RIGHT);

        String[] menuItems = {"About Us", "Contact", "Booking"};
        for (String item : menuItems) {
            Button navButton = new Button(item);
            navButton.setStyle("-fx-background-color: #444444; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px;");

            navButton.setOnAction(e -> {
                switch (item) {
                    case "About Us":
                        new TeamMember().start(new Stage());
                        break;
                    case "Contact":
                        new ContactPage().start(new Stage());
                        break;
                    case "Booking":
                        new BookingFrame().start(new Stage());
                        break;
                }
            });

            navPanel.getChildren().add(navButton);
        }

        headerPanel.getChildren().addAll(titleLabel, spacer, navPanel);
        root.setTop(headerPanel);

        // === BANNER IMAGE ===
        ImageView bannerImage = new ImageView();
        try {
            Image image = new Image("file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-26_23-21-28.jpg");
            bannerImage.setImage(image);
            bannerImage.setPreserveRatio(false);
            bannerImage.setFitWidth(primaryStage.getWidth());
            bannerImage.setFitHeight(primaryStage.getHeight() * 0.5);
        } catch (Exception e) {
            bannerImage.setStyle("-fx-background-color: lightgray;");
        }

        StackPane bannerPane = new StackPane(bannerImage);
        root.setCenter(bannerPane);

        // === SERVICE CARDS PANEL ===
        HBox servicesPanel = new HBox(40);
        servicesPanel.setPadding(new Insets(20, 50, 20, 50));
        servicesPanel.setAlignment(Pos.CENTER);

        String[] serviceTitles = {"ROOM INVENTORY", "DINING HALL", "CONFERENCE"};
        String[] serviceDescriptions = {
            "Room types to choose based on preferences.",
            "Dine with the ancients of our country.",
            "Productively execute your business meetings."
        };
        String[] imagePaths = {
            "file:C:/Users/panha/Downloads/Telegram Desktop/hotel.jpg",
            "file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-26_23-29-11.jpg",
            "file:C:/Users/panha/Downloads/Telegram Desktop/photo_2025-03-26_23-27-36.jpg"
        };

        for (int i = 0; i < 3; i++) {
            VBox serviceCard = createServiceCard(serviceTitles[i], serviceDescriptions[i], imagePaths[i]);
            servicesPanel.getChildren().add(serviceCard);
        }

        root.setBottom(servicesPanel);

        // === FULL-SCREEN RESPONSIVENESS ===
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> bannerImage.setFitWidth(newVal.doubleValue()));
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> bannerImage.setFitHeight(newVal.doubleValue() * 0.5));

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
        imageView.setFitWidth(400);
        imageView.setFitHeight(250);
        imageView.setPreserveRatio(false);

        Button imgButton = new Button("", imageView);
        imgButton.setStyle("-fx-background-color: transparent;");
        imgButton.setOnAction(e -> {
            switch (title) {
                case "ROOM INVENTORY":
                    new HotelRoomDisplay().start(new Stage());
                    break;
                case "DINING HALL":
                    new DiningHallDisplay().start(new Stage());
                    break;
                case "CONFERENCE":
                    new ConferenceGUI().start(new Stage());
                    break;
            }
        });

        Text titleLabel = new Text(title);
        titleLabel.setFont(Font.font("Serif", 18));
        titleLabel.setStyle("-fx-fill: #323232; -fx-font-weight: bold;");

        Text descriptionLabel = new Text(description);
        descriptionLabel.setFont(Font.font("Serif", 14));
        descriptionLabel.setStyle("-fx-fill: #646464;");

        serviceCard.getChildren().addAll(imgButton, titleLabel, descriptionLabel);
        return serviceCard;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

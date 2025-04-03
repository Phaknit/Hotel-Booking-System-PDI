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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TeamMember extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Set up main layout with background image
        BorderPane root = new BorderPane();
        BackgroundImage bgImage = new BackgroundImage(
                new Image("file:C:\\Users\\panha\\Downloads\\Telegram Desktop\\photo_2025-03-26_23-21-28.jpg"),  // Update path
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        root.setBackground(new Background(bgImage));

        // Title section with shadow effect
        Label titleLabel = new Label("Meet Our Team");
        titleLabel.setFont(Font.font("Arial Black", FontWeight.BOLD, 50)); // Increased font size
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 15, 0, 0, 0);");

        StackPane titlePane = new StackPane(titleLabel);
        titlePane.setPadding(new Insets(40));
        root.setTop(titlePane);

        // Team members section with uniform card sizes
        HBox teamContainer = new HBox(50);
        teamContainer.setAlignment(Pos.CENTER);
        teamContainer.setPadding(new Insets(50));

        teamContainer.getChildren().addAll(
                createTeamMemberCard("Neth Barom Phaknit", "Responsible for GUI creation and logic implementation", "file:C:\\Users\\panha\\Downloads\\Telegram Desktop\\i1.jpg"),
                createTeamMemberCard("Eng Soklang", "Developed the calendar feature for check-in/out logic", "file:C:\\Users\\panha\\Downloads\\Telegram Desktop\\IMG_20250111_104356_292.jpg"),
                createTeamMemberCard("Van Sotheany", "Handles user authentication and system integration", "file:C:/Users/panha/Pictures/Camera Roll/image.jpg")
        );

        root.setCenter(teamContainer);

        // Back button setup
        Button backButton = new Button("← Back");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 8;");
        backButton.setOnMouseEntered(event -> backButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3); -fx-text-fill: white;"));
        backButton.setOnMouseExited(event -> backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white;"));
        backButton.setOnAction(event -> {
            new HotelMainPage();
            primaryStage.close();
        });

        HBox bottomPane = new HBox(backButton);
        bottomPane.setPadding(new Insets(30));
        bottomPane.setAlignment(Pos.CENTER_LEFT);
        root.setBottom(bottomPane);

        // Create Scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Our Team");
        primaryStage.getIcons().add(new Image("file:C:/Users/panha/Downloads/team_icon.jpg"));
        primaryStage.setScene(scene);
        
        // Enable Full-Screen Mode
        primaryStage.setFullScreen(true);   // Activate full-screen view
        primaryStage.setMaximized(true);    // Ensure maximized window
        primaryStage.show();
    }

    private VBox createTeamMemberCard(String name, String role, String imagePath) {
        // Load image with fixed size
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(180);
        imageView.setFitHeight(180);
        imageView.setClip(new javafx.scene.shape.Circle(90, 90, 90)); // Circular shape

        // Name label with bold font and shadow effect
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Larger font
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 12, 0, 0, 0);");

        // Role label with stronger contrast and wrapping enabled
        Label roleLabel = new Label(role);
        roleLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));
        roleLabel.setTextFill(Color.LIGHTYELLOW);
        roleLabel.setWrapText(true);
        roleLabel.setMaxWidth(300);
        roleLabel.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 10, 0, 0, 0);");

        VBox textPane = new VBox(8, nameLabel, roleLabel);
        textPane.setAlignment(Pos.CENTER);

        // Card styling to ensure equal sizes with semi-transparent background
        VBox card = new VBox(20, imageView, textPane);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(330);
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3); "
                    + "-fx-background-radius: 18px; -fx-border-color: white; -fx-border-width: 2px;"
                    + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 18, 0, 0, 0);");

        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

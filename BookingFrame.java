package PDIproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookingFrame extends Application {
    private DatePicker checkInDatePicker;
    private DatePicker checkOutDatePicker;
    private Label totalCostLabel;
    private int totalPrice = 0;
    private final Map<String, int[]> selectedRoomDetails = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        createDatabaseTables(); // Ensure tables exist

        primaryStage.setTitle("Book Your Room Now");

        BorderPane root = new BorderPane();

        // Header Panel
        Label titleLabel = new Label("APSARA PARADISE RESIDENCE");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        HBox headerPanel = new HBox(titleLabel);
        headerPanel.setPadding(new Insets(10));

        // Date Selection Panel
        HBox datePanel = new HBox(15);
        datePanel.setPadding(new Insets(10));
        Label checkInLabel = new Label("Check-In Date:");
        checkInDatePicker = new DatePicker();

        Label checkOutLabel = new Label("Check-Out Date:");
        checkOutDatePicker = new DatePicker();

        datePanel.getChildren().addAll(checkInLabel, checkInDatePicker, checkOutLabel, checkOutDatePicker);

        // Top Panel (Header + Date Selection)
        VBox topPanel = new VBox(10, headerPanel, datePanel);
        root.setTop(topPanel);

        // Room Selection Panel
        GridPane roomSelectionPanel = new GridPane();
        roomSelectionPanel.setHgap(10);
        roomSelectionPanel.setVgap(10);
        roomSelectionPanel.setPadding(new Insets(20));

        // Set grid to have 2 columns
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        roomSelectionPanel.getColumnConstraints().addAll(col1, col2);

        String[][] roomData = {
            {"STANDARD", "Max Occupancy: 1", "56", "10", "file:C:\\Users\\panha\\Downloads\\Telegram Desktop\\photo_2025-03-27_14-09-38.jpg"},
            {"DOUBLE BED", "Max Occupancy: 2", "96", "8", "file:C:\\Users\\panha\\Downloads\\Telegram Desktop\\photo_2025-03-27_14-12-03.jpg"},
            {"DELUXE", "Max Occupancy: 4", "143", "6", "file:C:\\Users\\panha\\Downloads\\Telegram Desktop\\photo_2025-03-27_14-13-29.jpg"},
            {"SUITE", "Max Occupancy: 4", "200", "4", "file:C:\\Users\\panha\\Downloads\\Telegram Desktop\\photo_2025-03-27_14-16-35.jpg"},
            {"VILLA", "Max Occupancy: 6", "350", "2", "file:C:\\Users\\panha\\Downloads\\Telegram Desktop\\photo_2025-03-27_13-52-57.jpg"}
        };

        for (int i = 0; i < roomData.length; i++) {
            // Add rooms to the grid, two per row
            int row = i / 2;
            int col = i % 2;
            roomSelectionPanel.add(createRoomPanel(
                roomData[i][0], roomData[i][1],
                Integer.parseInt(roomData[i][2]),
                Integer.parseInt(roomData[i][3]),
                roomData[i][4]
            ), col, row);
        }

        // Wrap the room selection panel in a ScrollPane to make it scrollable
        ScrollPane scrollPane = new ScrollPane(roomSelectionPanel);
        scrollPane.setFitToWidth(true);  // Ensures content scales with the window size
        root.setCenter(scrollPane);

        // Control Panel (Total Cost + Book Now Button)
        HBox controlPanel = new HBox(15);
        controlPanel.setPadding(new Insets(15));
        controlPanel.setAlignment(Pos.CENTER_RIGHT);

        Label totalCostTextLabel = new Label("Total Cost: ");
        totalCostLabel = new Label("$0");

        Button bookNowButton = new Button("BOOK NOW");
        bookNowButton.setStyle("-fx-font-size: 16px; -fx-background-color: lightgreen;");
        bookNowButton.setOnAction(e -> handleBooking(primaryStage));

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
        VBox bottomPanel = new VBox(bookNowButton, backButtonPanel);
        bottomPanel.setAlignment(Pos.CENTER);
        root.setBottom(bottomPanel);

        controlPanel.getChildren().addAll(totalCostTextLabel, totalCostLabel, bookNowButton);
        root.setBottom(controlPanel);

        Scene scene = new Scene(root, 900, 800);
        primaryStage.setScene(scene);

        // Set full-screen mode
        primaryStage.setFullScreen(true);

        primaryStage.show();
    }

    private void createDatabaseTables() {
        String roomsTable = "CREATE TABLE IF NOT EXISTS rooms (room_type TEXT PRIMARY KEY, total_rooms INT)";
        String reservationsTable = "CREATE TABLE IF NOT EXISTS reservations (id INTEGER PRIMARY KEY AUTOINCREMENT, room_type TEXT, check_in_date DATE, check_out_date DATE, rooms_booked INT)";
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:HotelDB.db");
             Statement stmt = conn.createStatement()) {
            stmt.execute(roomsTable);
            stmt.execute(reservationsTable);
            initializeRooms(conn);  // Set initial room limits
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeRooms(Connection conn) throws SQLException {
        Map<String, Integer> roomLimits = new HashMap<>();
        roomLimits.put("STANDARD", 10);
        roomLimits.put("DOUBLE BED", 8);
        roomLimits.put("DELUXE", 6);
        roomLimits.put("SUITE", 4);
        roomLimits.put("VILLA", 2);

        String checkQuery = "SELECT COUNT(*) FROM rooms";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkQuery)) {
            if (rs.next() && rs.getInt(1) == 0) {  // If table is empty, insert data
                String insertQuery = "INSERT INTO rooms (room_type, total_rooms) VALUES (?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    for (Map.Entry<String, Integer> entry : roomLimits.entrySet()) {
                        pstmt.setString(1, entry.getKey());
                        pstmt.setInt(2, entry.getValue());
                        pstmt.executeUpdate();
                    }
                }
            }
        }
    }

    private VBox createRoomPanel(String roomType, String occupancyInfo, int pricePerNight, int totalRooms, String imagePath) {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        // Load image for the room type
        Image roomImage = new Image(imagePath);
        ImageView imageView = new ImageView(roomImage);
        imageView.setFitWidth(250);
        imageView.setPreserveRatio(true);

        Label typeLabel = new Label(roomType);
        typeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label occupancyLabel = new Label(occupancyInfo);
        Label priceLabel = new Label("Price per Night: $" + pricePerNight);

        HBox selectionPanel = new HBox(10);
        selectionPanel.setAlignment(Pos.CENTER_LEFT);

        Label nightsLabel = new Label("Nights:");
        ComboBox<Integer> nightsBox = new ComboBox<>();
        for (int i = 0; i <= 5; i++) nightsBox.getItems().add(i);
        nightsBox.getSelectionModel().select(0);

        Label roomsLabel = new Label("Rooms:");
        ComboBox<Integer> roomsBox = new ComboBox<>();
        for (int i = 0; i <= 5; i++) roomsBox.getItems().add(i);
        roomsBox.getSelectionModel().select(0);

        selectionPanel.getChildren().addAll(nightsLabel, nightsBox, roomsLabel, roomsBox);

        nightsBox.setOnAction(e -> updateTotal(roomType, pricePerNight, nightsBox, roomsBox));
        roomsBox.setOnAction(e -> updateTotal(roomType, pricePerNight, nightsBox, roomsBox));

        panel.getChildren().addAll(imageView, typeLabel, occupancyLabel, priceLabel, selectionPanel);
        return panel;
    }

    private void updateTotal(String roomType, int pricePerNight, ComboBox<Integer> nightsBox, ComboBox<Integer> roomsBox) {
        int nights = nightsBox.getValue();
        int rooms = roomsBox.getValue();
        int totalRoomCost = nights * rooms * pricePerNight;

        if (rooms > 0) {
            selectedRoomDetails.put(roomType, new int[]{nights, rooms, totalRoomCost});
        } else {
            selectedRoomDetails.remove(roomType);
        }

        totalPrice = selectedRoomDetails.values().stream().mapToInt(details -> details[2]).sum();
        totalCostLabel.setText("$" + totalPrice);
    }

    private void handleBooking(Stage stage) {
        if (checkInDatePicker.getValue() == null || checkOutDatePicker.getValue() == null) {
            showAlert("Please select both Check-In and Check-Out dates.");
            return;
        }

        if (checkOutDatePicker.getValue().isBefore(checkInDatePicker.getValue())) {
            showAlert("Check-Out date must be after Check-In date.");
            return;
        }

        for (var entry : selectedRoomDetails.entrySet()) {
            if (!isRoomAvailable(entry.getKey(), entry.getValue()[1])) {
                showAlert("Not enough " + entry.getKey() + " rooms available!");
                return;
            }
        }

        saveBooking();
        showAlert("Booking successful!");

        // Pass selectedRoomDetails (bookedRooms) and totalPrice to PaymentGUI
        PaymentGUI paymentGUI = new PaymentGUI(selectedRoomDetails, totalPrice);
        stage.close();  // Close the booking window
    }


    private void launchPaymentGUI() {
        // Create a new PaymentGUI window
        PaymentGUI paymentGUI = new PaymentGUI(selectedRoomDetails, totalPrice);
        Stage paymentStage = new Stage();
      
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isRoomAvailable(String roomType, int roomsRequested) {
        String query = "SELECT total_rooms FROM rooms WHERE room_type = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:HotelDB.db");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, roomType);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && roomsRequested <= rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveBooking() {
        String query = "INSERT INTO reservations (room_type, check_in_date, check_out_date, rooms_booked) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:HotelDB.db");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            for (var entry : selectedRoomDetails.entrySet()) {
                stmt.setString(1, entry.getKey());
                stmt.setString(2, checkInDatePicker.getValue().toString());
                stmt.setString(3, checkOutDatePicker.getValue().toString());
                stmt.setInt(4, entry.getValue()[1]);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

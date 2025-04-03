package PDIproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ReceiptGUI {
    private Stage stage;

    public ReceiptGUI(Map<String, int[]> bookedRooms, int totalPrice, String paidBy) {
        stage = new Stage();
        stage.setTitle("Booking Receipt");

        VBox receiptPanel = new VBox(10);
        receiptPanel.setPadding(new Insets(20));
        receiptPanel.setAlignment(Pos.CENTER_LEFT);
        receiptPanel.setStyle("-fx-background-color: white;");

        // Title
        Label titleLabel = new Label("Apsara Paradise Residence");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        receiptPanel.getChildren().add(titleLabel);

        // Paid By
        receiptPanel.getChildren().add(createRow("Paid By:", paidBy));

        // Booking Details
        int totalRooms = 0;
        int totalNights = 0;
        StringBuilder receiptText = new StringBuilder();

        for (Map.Entry<String, int[]> entry : bookedRooms.entrySet()) {
            String roomType = entry.getKey();
            int[] details = entry.getValue(); // {rooms, nights}
            int rooms = details[0];
            int nights = details[1];

            totalRooms += rooms;
            totalNights += nights;

            receiptPanel.getChildren().add(createRow("Room Type:", roomType));
            receiptPanel.getChildren().add(createRow("Nights:", String.valueOf(nights)));
            receiptPanel.getChildren().add(createRow("Rooms:", String.valueOf(rooms)));

            // Build text receipt
            receiptText.append("Room Type: ").append(roomType).append("\n");
            receiptText.append("Nights: ").append(nights).append("\n");
            receiptText.append("Rooms: ").append(rooms).append("\n");
        }

        // Total Amount
        receiptPanel.getChildren().add(createRow("Total Amount:", "$" + totalPrice));

        // Paid Label
        Label paidLabel = new Label("PAID");
        paidLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: green; -fx-border-color: green; -fx-border-width: 2; -fx-padding: 5px;");
        VBox paidBox = new VBox(paidLabel);
        paidBox.setAlignment(Pos.CENTER);

        // Save receipt to file
        saveReceiptToFile(receiptText.toString(), totalPrice);

        VBox root = new VBox(20, receiptPanel, paidBox);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createRow(String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold;");
        Label valueNode = new Label(value);
        HBox row = new HBox(10, labelNode, valueNode);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void saveReceiptToFile(String receiptText, int totalPrice) {
        try (FileWriter writer = new FileWriter("receipt.txt")) {
            writer.write("Apsara Paradise Residence\n");
            writer.write(receiptText);
            writer.write("Total Amount: $" + totalPrice + "\n");
            writer.write("PAID\n");
            System.out.println("Receipt saved to receipt.txt");
        } catch (IOException e) {
            System.err.println("Error saving receipt: " + e.getMessage());
        }
    }
}
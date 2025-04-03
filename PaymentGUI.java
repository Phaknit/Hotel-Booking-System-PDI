package PDIproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class PaymentGUI {
    private Stage stage;
    private Map<String, int[]> bookedRooms;
    private int totalPrice;

    // Card Number Fields
    private TextField[] cardNumberFields = new TextField[4];

    public PaymentGUI(Map<String, int[]> bookedRooms, int totalPrice) {
        this.bookedRooms = bookedRooms;
        this.totalPrice = totalPrice;

        stage = new Stage();
        stage.setTitle("Payment");

        VBox paymentPanel = new VBox(15);
        paymentPanel.setPadding(new Insets(20));
        paymentPanel.setAlignment(Pos.CENTER);

        Label totalLabel = new Label("Total Amount: $" + totalPrice);
        totalLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Payment method selection
        ComboBox<String> paymentMethodBox = new ComboBox<>();
        paymentMethodBox.getItems().addAll("Credit Card", "PayPal", "Cash");
        paymentMethodBox.setValue("Credit Card");

        // GridPane for Credit Card Details
        GridPane cardDetailsGrid = new GridPane();
        cardDetailsGrid.setHgap(20);
        cardDetailsGrid.setVgap(15);
        cardDetailsGrid.setPadding(new Insets(20));
        cardDetailsGrid.setAlignment(Pos.CENTER);

        Label cardTypeLabel = new Label("Card Type:");
        ComboBox<String> cardTypeBox = new ComboBox<>();
        cardTypeBox.getItems().addAll("Visa", "MasterCard", "American Express");
        cardTypeBox.setValue("Visa");

        Label cardNameLabel = new Label("Cardholder Name:");
        TextField cardNameField = new TextField();

        Label cardNumberLabel = new Label("Card Number:");

        HBox cardNumberBox = new HBox(10);
        cardNumberBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) {
            cardNumberFields[i] = new TextField();
            cardNumberFields[i].setMaxWidth(60);
            cardNumberFields[i].setPromptText("####");
            final int index = i;

            cardNumberFields[i].textProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue.length() == 4 && index < 3) {
                    cardNumberFields[index + 1].requestFocus();
                }
            });

            cardNumberBox.getChildren().add(cardNumberFields[i]);
        }

        Label expDateLabel = new Label("Expiry Date (MM/YY):");
        TextField expDateField = new TextField();
        expDateField.setPromptText("MM/YY");

        Label cvcLabel = new Label("CVC:");
        TextField cvcField = new TextField();
        cvcField.setPromptText("###");

        cardDetailsGrid.addRow(0, cardTypeLabel, cardTypeBox);
        cardDetailsGrid.addRow(1, cardNameLabel, cardNameField);
        cardDetailsGrid.addRow(2, cardNumberLabel, cardNumberBox);
        cardDetailsGrid.addRow(3, expDateLabel, expDateField);
        cardDetailsGrid.addRow(4, cvcLabel, cvcField);

        cardDetailsGrid.setVisible(true);

        paymentMethodBox.setOnAction(e -> cardDetailsGrid.setVisible(paymentMethodBox.getValue().equals("Credit Card")));

        Button payButton = new Button("Confirm Payment");
        payButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 18px;");
        payButton.setOnAction(e -> handlePayment(paymentMethodBox.getValue(), cardTypeBox.getValue(),
                cardNameField.getText(), getFullCardNumber(), expDateField.getText(), cvcField.getText()));

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 18px;");
        closeButton.setOnAction(e -> stage.close());

        paymentPanel.getChildren().addAll(totalLabel, paymentMethodBox, cardDetailsGrid, payButton, closeButton);

        Scene scene = new Scene(paymentPanel);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.show();
    }

    private String getFullCardNumber() {
        StringBuilder fullNumber = new StringBuilder();
        for (TextField field : cardNumberFields) {
            fullNumber.append(field.getText());
        }
        return fullNumber.toString();
    }

    private void handlePayment(String paymentMethod, String cardType, String cardName, String cardNumber, String expDate, String cvc) {
        if (paymentMethod.equals("Credit Card")) {
            if (cardName.isEmpty() || cardNumber.length() != 16 || expDate.isEmpty() || cvc.isEmpty()) {
                showAlert("Please fill in all credit card details correctly.");
                return;
            }
            if (!expDate.matches("\\d{2}/\\d{2}")) {
                showAlert("Invalid expiry date format. Use MM/YY.");
                return;
            }
            if (!cvc.matches("\\d{3}")) {
                showAlert("Invalid CVC. It must be 3 digits.");
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Payment successful! Generating receipt...");
        alert.showAndWait();

        new ReceiptGUI(bookedRooms, totalPrice, paymentMethod);
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

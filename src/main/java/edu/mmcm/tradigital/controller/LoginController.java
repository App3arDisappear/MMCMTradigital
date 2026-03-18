package edu.mmcm.tradigital.controller;

import edu.mmcm.tradigital.model.User;
import edu.mmcm.tradigital.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    private UserService userService = new UserService();

    @FXML
    public void handleLogin(ActionEvent event) {
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        User user = userService.authenticate(email, pass);

        if (user != null) {
            boolean isAdmin = user.getRole().equalsIgnoreCase("ADMIN");
            loadMainWindow(isAdmin);
        } else {
            showAlert("Login Error", "Invalid credentials. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        if (email.isEmpty() || pass.isEmpty()) {
            showAlert("Input Required", "Please enter an email and password to register.", Alert.AlertType.WARNING);
            return;
        }

        // We'll default new registrations to CUSTOMER role
        boolean success = userService.register(email, pass, "CUSTOMER");

        if (success) {
            showAlert("Success", "Account created! You can now login.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Email is already registered.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleGuest(ActionEvent event) {
        loadMainWindow(false); // Guest role (hides Admin)
    }

    private void loadMainWindow(boolean isAdmin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/mmcm/tradigital/fxml/main_view.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.setUserRole(isAdmin);

            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("MMCM Tradigital - Welcome");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
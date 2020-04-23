package chat.client;

import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import java.net.*;
import java.io.*;

import network.pckg.*;

/** @author nuald Основной класс для клиентского окна */
public class ClientWin extends Application {

  public static void main(String[] args) {
    // new ClientWin();
    Application.launch(args);
  }

  public ClientWin() {}

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("primaryStage.fxml"));
    primaryStage.setTitle("Client for Chat");
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }
}

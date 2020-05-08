package chat.client;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Control;
import javafx.stage.*;
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
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("primaryStage.fxml"));
    Parent root = loader.load();
//    Parent root = FXMLLoader.load(getClass().getResource("primaryStage.fxml"));
    primaryStage.setTitle("Client for Chat");
    primaryStage.setScene(new Scene(root));


    //Control control = loader.getController();
   // primaryStage.setOnShowing(control);

    //primaryStage.setOnShowing(controller.getOpenEventHandler());

    primaryStage.show();
  }
}

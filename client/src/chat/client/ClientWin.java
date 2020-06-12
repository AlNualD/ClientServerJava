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

  private Stage primaryStage;
  private controller mainController;

  public static void main(String[] args) {
    Application.launch(args);
  }

  public ClientWin() {}

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getResource("primaryStage.fxml"));
    Parent root = loader.load();
    primaryStage.setTitle("Client for Chat");
    primaryStage.setScene(new Scene(root));

    controller control = loader.getController();
    control.setMainApp(this);
    primaryStage.setOnCloseRequest(control.getCloseEventHandler());
    mainController = control;

    primaryStage.show();
  }

  public void showGroupsMenu(TCPConnection connection){
     try {
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(getClass().getResource("GroupsMenu.fxml"));
       Parent root = loader.load();
       Stage groupsMenuStage = new Stage();
       groupsMenuStage.setTitle("Groups Menu");
       groupsMenuStage.initOwner(primaryStage);
       groupsMenuStage.initModality(Modality.NONE);
       Scene scene =new Scene(root);
       groupsMenuStage.setScene(scene);
       GroupsMenuController GMcontroller = loader.getController();
       GMcontroller.setStage(groupsMenuStage);
       GMcontroller.setConnection(connection);
       setGMcontroller(GMcontroller);
       groupsMenuStage.show();


    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setGMcontroller(  GroupsMenuController  GMcontroller) {
    mainController.setMenuController(GMcontroller);
  }
}


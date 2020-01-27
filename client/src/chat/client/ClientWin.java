package chat.client;

import javafx.application.Application;
import javafx.stage.Stage;

import java.net.*;
import java.io.*;

import network.pckg.*;

/**
 * @author nuald
 * Основной класс для клиентского окна
 */
public class ClientWin extends Application {
    public static void main(String[] args) {
        new ClientWin();
        Application.launch();

    }
    public ClientWin(){


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/primaryStage.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

}

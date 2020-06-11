package chat.client;

import common.commands;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import network.pckg.TCPConnection;

public class GroupsMenuController {
 private Stage stage;
 private TCPConnection connection;
 private boolean isAdmin = false;
 private String[] gInf;
 private String group;
 private ObservableList<String> players = FXCollections.observableArrayList();
    @FXML private TextField gnameTF;
    @FXML private Label amountLabel;
    @FXML private TextField adminTF;
    @FXML private CheckBox openGroupCheckBox;
    @FXML private TextField newPlayerTF;
    @FXML private ListView<String> playersList;
    @FXML private Button connectButton;
    @FXML private Button newGroupButton;
    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML public void initialize(){
        playersList.setItems(players);
    }

    public void setConnection(TCPConnection connection) {
        this.connection = connection;
    }

    public String getGroup(){
        return group;
    }

    public void getInf(String gname) {
        String msg = commands.returnCommand(commands.Get_GROUP_INF) + gname;
        connection.sendMsg(msg);
    }
    public void connectionSuccessful() {
        connectButton.setText("disconnect");
        newGroupButton.setVisible(false);
        refreshButtonClicked();
    }

    public  void connectToGroupButtonClicked() {


    //    newUserButton.setVisible(false);
    //    nickname = nicknameTF.getText();
    //    if (checkNickname(nickname)) {
    //      try {
    //
    //        nicknameTF.setEditable(false);
    //        connection = new TCPConnection(this, "127.0.0.1", 8000); // "192.168.99.101", 8080);
    //        messageArea.setEditable(true);
    //
    //      } catch (IOException e) {
    //        e.printStackTrace();
    //        System.out.println("smthng wrong");
    //        printInChatArea("Connection failed");
    //      }


          if (connectButton.getText().equals("disconnect")) {

              connection.sendMsg(commands.returnCommand(commands.DISCONNECT_GROUP) + group);
              connectButton.setText("connect");
              newGroupButton.setVisible(true);
              return;
            }

    String gname;
       if((gname = gnameTF.getText()).equals("")) {
           Platform.runLater(() -> {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error alert");
               alert.setHeaderText("ErrorWithConnection");
               alert.setContentText("Enter group name");
               alert.showAndWait();
           });
           return;
       }
       group = gname;
       String msg = commands.returnCommand(commands.CONNECT_GROUP) + gname + "@!";
       connection.sendMsg(msg);


    }

    public void newGroupButtonClicked() {
        String gname;
        if((gname = gnameTF.getText()).equals("")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error alert");
                alert.setHeaderText("ErrorWithConnection");
                alert.setContentText("Enter group name");
                alert.showAndWait();
            });
            return;
        }
        group = gname;
        String msg = commands.returnCommand(commands.GROUP_NEW) + gname + "@!";
        connection.sendMsg(msg);
        getInf(gname);
        refreshButtonClicked();
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        if(admin) {
            System.out.println("im admin");
            adminTF.setEditable(true);
            openGroupCheckBox.setDisable(false);
            newPlayerTF.setEditable(true);
        } else {
            adminTF.setEditable(false);
            openGroupCheckBox.setDisable(true);
            newPlayerTF.setEditable(false);
        }
    }

    public void setgInf(String[] gInf) {
        this.gInf = gInf;
        if(gInf[0].equals("1")) setAdmin(true);
        adminTF.setText(gInf[1]);
        Platform.runLater(() -> {
            amountLabel.setText(gInf[2]);
        });
        if(gInf[3].equals("1")) {
            openGroupCheckBox.setSelected(true);
        } else {
            openGroupCheckBox.setSelected(false);
        }
    }

    public void addPlayer(String newPlayer) {
        Platform.runLater(() -> {
            players.add(newPlayer);
            amountLabel.setText(String.valueOf(players.size()));
        });
    }

    public void refreshButtonClicked() {
        players.clear();
        String msq = commands.returnCommand(commands.Get_GROUP_MEMBERS) + group;
        connection.sendMsg(msq);
    }

    public void addButtonClicked() {
        String newPlayer = "";
        if((newPlayer = newPlayerTF.getText()).equals("")){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error alert");
                alert.setHeaderText("Error");
                alert.setContentText("Enter user name");
                alert.showAndWait();
            });
            return;
        }
        String msg = commands.returnCommand(commands.ADD_TO_GROUP) + group + "@" + newPlayer;
    }

    public void groupTypeChangeWrong() {
        if (openGroupCheckBox.isSelected()) {
            openGroupCheckBox.setSelected(false);
        } else {
            openGroupCheckBox.setSelected(true);
        }
    }

    public void groupTypeChange(){
        String command = commands.returnCommand(commands.CHANGE_GROUP_TYPE) + group + "@";
        if(openGroupCheckBox.isSelected()) {
            command += "true";
        } else {
            command += "false";
        }
        connection.sendMsg(command);

    }
    public void changeAdminButtonCkicked(){
        if(!isAdmin) return;
        String admname = "";
        if((admname = adminTF.getText()).equals("")){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error alert");
                alert.setHeaderText("Error");
                alert.setContentText("Enter user name");
                alert.showAndWait();
            });
            return;
        }
        String command = commands.returnCommand(commands.CHANGE_ADMIN) + group + "@" + admname;
        connection.sendMsg(command);
    }

}

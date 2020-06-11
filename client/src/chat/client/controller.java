package chat.client;

import java.io.IOException;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.WindowEvent;
import network.pckg.TCPConnection;
import network.pckg.TCPConnectionListener;
import common.commands;

public class controller implements TCPConnectionListener {

//TODO: сделать броски с преимуществом и помехой
  private TCPConnection connection;
    //private ObservableList<String> dices  = FXCollections.observableArrayList("d4", "d6", "d8", "d10", "d12", "20");
    private ObservableList<String> dices = FXCollections.observableArrayList("d4", "d6", "d8", "d10", "d12", "d20");
  private Pattern numberPattern = Pattern.compile("((\\+|-)?\\d*)");
  private Pattern numberABSPattern = Pattern.compile("(\\d*)");
  private commands command = commands.LOGIN;
  private ClientWin mainApp;
  private String nickname;
  private GroupsMenuController MenuController = null;
  @FXML private Button connectionButton;
  @FXML private Button newUserButton;
  @FXML private TextArea chatArea;
  @FXML private TextArea groupChatArea;
  @FXML private TextArea messageArea;
  @FXML private TextField nicknameTF;
  @FXML private PasswordField passwdPF;
  @FXML private  TextField modifTF;
  @FXML private TextField diceAmountTF;
  @FXML private ComboBox<String> rollMenu;// = new ComboBox<>(dices);
  @FXML private Slider rollModeSlider;
  @FXML private Tab groupChatTab;
  @FXML public void initialize(){
    //
    rollMenu.setItems(dices);
    rollMenu.setValue("d20");

    modifTF.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!numberPattern.matcher(newValue).matches()) modifTF.setText(oldValue);
    });
    diceAmountTF.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!numberABSPattern.matcher(newValue).matches()) diceAmountTF.setText(oldValue);
    });
  }

  @FXML private void groupsButtonClicked(){
    mainApp.showGroupsMenu(connection);
  }

  public void rollButtonClicked() {
    String msg = commands.returnCommand(commands.ROLL_ME);
    String buf = diceAmountTF.getText();
    if(buf.equals("")) buf = "1";
    msg = msg + buf + "@";
    buf = rollMenu.getValue();
    msg += buf.substring(1) + "@";
    buf = modifTF.getText();
    if(buf.equals("")) buf = "0";
    msg += buf;
    // помеха/преимущество
    msg += "@" + (int)rollModeSlider.getValue();
    if(groupChatTab.isSelected()) {
      msg += "@" + MenuController.getGroup();
    } else {
      msg += "@a";
    }
    connection.sendMsg(msg);
  }

  public void sendButtonClicked() {
    System.out.println("Its work!");
    String msg = messageArea.getText();
    if (msg.equals("")) return;
    if(groupChatTab.isSelected()) {
      msg = commands.returnCommand(commands.SEND_GROUP) + MenuController.getGroup() + "@" + msg;
    } else {
      msg = commands.returnCommand(commands.SEND_ALL) + msg;
    }

    //msg = nickname + ": " + msg;

    connection.sendMsg(msg);
    // chatArea.appendText(messageArea.getText() + "\n");
    messageArea.setText("");
  }

  private synchronized void changer(commands command)
  {
    this.command = command;
  }
  public void newUserButtonClicked(){
    //TODO сделать невидимой и неактивной newUserButton.
    if(newUserButton.isVisible())
    {
//      Platform.runLater(
//              () -> {
//                newUserButton.setVisible(false);
//              });
      command = commands.NEW_USER;
      newUserButton.setVisible(false);
     // changer(commands.NEW_USER);
      connectionButtonClicked();
    }

  }

  public void connectionButtonClicked() {
    // if (connectionButton.getText().equals("connection")){
    if (connectionButton.getText().equals("disconnection")) {
        connection.sendMsg(commands.returnCommand(commands.EXIT));
        connection.disconnect();
        connectionButton.setText("connection");
        messageArea.setEditable(false);
        modifTF.setEditable(false);
        diceAmountTF.setEditable(false);
        nicknameTF.setEditable(true);
        command = commands.LOGIN;
        //  changer(commands.LOGIN);
        newUserButton.setVisible(true);
//      Platform.runLater(
//              () -> {
//                newUserButton.setVisible(true);
//              });
        return;
    }
    newUserButton.setVisible(false);
    nickname = nicknameTF.getText();
    if (checkNickname(nickname)) {
      try {

        nicknameTF.setEditable(false);
        connection = new TCPConnection(this, "127.0.0.1", 8000);     // "192.168.99.101", 8080);
        messageArea.setEditable(true);

      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Connection failed");
        printInChatArea("Connection failed");
      }
    }
   //
    // }
    // else {
    //    connection.disconnect();
    // messageArea.setEditable(false);
    // }
  }

  private boolean checkNickname(String nickname) {
    if (nickname == "") {
      return false;
    } else {
      return true;
    }
  }

  private void printInChatArea(String msg) {
    chatArea.appendText(msg + "\n");
  }

  private void printInGroupArea(String msg) {
    groupChatArea.appendText(msg + "\n");
  }


  @Override
  public void onConnectionReady(TCPConnection tcpConnection) {
    printInChatArea("connection is successful");
    Platform.runLater(
        () -> {
          connectionButton.setText("disconnection");
        });
    String passwd = passwdPF.getText();
    //авторизация или отключение зависит от command
    connection.sendMsg(commands.returnCommand(command) + nickname + "@" + passwd);
    modifTF.setEditable(true);
    diceAmountTF.setEditable(true);

    //rollMenu.setValue("d20");
//    modifTF.textProperty().addListener((observable, oldValue, newValue) -> {
//      if (!numberPattern.matcher(newValue).matches()) modifTF.setText(oldValue);
//    });
//    diceAmountTF.textProperty().addListener((observable, oldValue, newValue) -> {
//      if (!numberABSPattern.matcher(newValue).matches()) diceAmountTF.setText(oldValue);
//    });
  }
//TODO: make onClose Request to Disconnect
  private javafx.event.EventHandler<WindowEvent> closeEventHandler = new javafx.event.EventHandler<WindowEvent>() {
    @Override
    public void handle(WindowEvent event) {
      if(!groupChatTab.isDisable()) {
        connection.sendMsg(commands.returnCommand(commands.DISCONNECT_GROUP) + MenuController.getGroup());

      }

      if (connectionButton.getText().equals("disconnection")) {
        connection.sendMsg(commands.returnCommand(commands.EXIT));
        connection.disconnect(); }

      }

  };

  public javafx.event.EventHandler<WindowEvent> getCloseEventHandler(){
    return closeEventHandler;
  }

  @Override
  public void onReceiveString(TCPConnection tcpConnection, String value) {
    if (value == null) {
      //System.out.println("nullpointer");
      return;
    }
    String[] inf = value.split("@");
    if(inf[0].equals(commands.returnCommand(commands.SEND_ALL))){
      printInChatArea(inf[1]);
      return;
    }
    if(inf[0].equals(commands.returnCommand(commands.SEND_GROUP))) {
      printInGroupArea(inf[1]);
      return;
    }
    if(inf[0].equals(commands.returnCommand(commands.Get_GROUP_INF))) {
      MenuController.setgInf(inf[1].split("%"));
      return;
    }
    if(inf[0].equals(commands.returnCommand(commands.Get_GROUP_MEMBERS))) {
     Platform.runLater(() -> {
       MenuController.addPlayer(inf[1]);
     });
      return;
    }
    if(inf[0].equals(commands.returnCommand(commands.DISCONNECT_GROUP))) {
      groupChatTab.setDisable(true);
      return;
    }
    if (inf[0].equals(commands.returnCommand(commands.CHANGE_GROUP_TYPE))) {
      MenuController.groupTypeChangeWrong();
      Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText("ErrorWithChange");
        alert.setContentText(inf[1]);
        alert.showAndWait();
      });
      return;
    }
    if (inf[0].equals(commands.returnCommand(commands.CHANGE_ADMIN))) {
      if(inf[1].equals("OK")) {
        Platform.runLater(() -> {
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Information");
          alert.setHeaderText("Information");
          alert.setContentText("You should re-connect to see changes");
          alert.showAndWait();
        });
      } else {
        Platform.runLater(() -> {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error alert");
          alert.setHeaderText("ErrorWithChange");
          alert.setContentText(inf[1]);
          alert.showAndWait();
        });
      }
      return;
    }
    if(inf[0].equals(commands.returnCommand(commands.GROUP_NEW))) {
      Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Information");
        alert.setContentText(inf[1]);
        alert.showAndWait();
    });
      MenuController.connectToGroupButtonClicked();
    }
    if(inf[0].equals(commands.returnCommand(commands.CONNECT_GROUP))) {
      MenuController.getInf(MenuController.getGroup());
      groupChatTab.setDisable(false);
    }
    if(inf[0].equals(commands.returnCommand(commands.ERROR))) {
      Platform.runLater(() -> {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error alert");
        alert.setHeaderText("ErrorWithConnection");
        alert.setContentText(inf[1]);
        alert.showAndWait();
      });
      //connectionButtonClicked();
      return;
    }
      printInChatArea(inf[0]);
      printInGroupArea(inf[0]);




  }

  @Override
  public void onDisconnect(TCPConnection tcpConnection) {
    printInChatArea("connection is over");

  }

  @Override
  public void onException(TCPConnection tcpConnection, Exception e)
  {
    System.out.println("Exception: " + e);
  }

  public void setMainApp(ClientWin mainApp) {
    this.mainApp = mainApp;
  }

  public void setMenuController(GroupsMenuController menuController) {
    MenuController = menuController;
  }
}

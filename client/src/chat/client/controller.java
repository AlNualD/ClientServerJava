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

  private TCPConnection connection;
    //private ObservableList<String> dices  = FXCollections.observableArrayList("d4", "d6", "d8", "d10", "d12", "20");
    private ObservableList<String> dices = FXCollections.observableArrayList("d4", "d6", "d8", "d10", "d12", "d20");
  Pattern numberPattern = Pattern.compile("((\\+|-)?\\d*)");
  Pattern numberABSPattern = Pattern.compile("(\\d*)");
  commands command = commands.LOGIN;
  private String nickname;
  @FXML private Button connectionButton;
  @FXML private Button newUserButton;
  @FXML private TextArea chatArea;
  @FXML private TextArea messageArea;
  @FXML private TextField nicknameTF;
  @FXML private PasswordField passwdPF;
  @FXML private  TextField modifTF;
  @FXML private TextField diceAmountTF;
  @FXML private ComboBox<String> rollMenu;// = new ComboBox<>(dices);

  public void rollButtonClicked() {
    //TODO: обработать когда ничо не выбрано
    String msg = commands.returnCommand(commands.ROLL_ME);
    String buf = diceAmountTF.getText();
    if(buf.equals("")) buf = "1";
    msg = msg + buf + "@";
    buf = rollMenu.getValue();
    msg += buf.substring(1) + "@";
    buf = modifTF.getText();
    if(buf.equals("")) buf = "0";
    msg += buf;
    connection.sendMsg(msg);
  }

  public void sendButtonClicked() {

    System.out.println("Its work!");
    String msg = messageArea.getText();
    if (msg.equals("")) return;
    //msg = nickname + ": " + msg;
    msg = commands.returnCommand(commands.SEND_ALL) + msg;
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
        connection = new TCPConnection(this, "127.0.0.1", 8000); // "192.168.99.101", 8080);
        messageArea.setEditable(true);

      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("smthng wrong");
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

  @Override
  public void onConnectionReady(TCPConnection tcpConnection) {
    printInChatArea("connection is successful");
    Platform.runLater(
        () -> {
          connectionButton.setText("disconnection");
        });
    String passwd = passwdPF.getText();
    connection.sendMsg(commands.returnCommand(command) + nickname + "@" + passwd);
    rollMenu.setItems(dices);
    //rollMenu.
    modifTF.setEditable(true);
    diceAmountTF.setEditable(true);

    //rollMenu.setValue("d20");
    //TODO: убрать наложение листнеров. Сделать однократное
    modifTF.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!numberPattern.matcher(newValue).matches()) modifTF.setText(oldValue);
    });
    diceAmountTF.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!numberABSPattern.matcher(newValue).matches()) diceAmountTF.setText(oldValue);
    });
  }

  private javafx.event.EventHandler<WindowEvent> openEventHandler = new javafx.event.EventHandler<WindowEvent>() {
    @Override
    public void handle(WindowEvent event) {
      //ТУТ НЕОБХОДИМАЯ ЛОГИКА
      System.out.println("Stage is openningfdsdf");

      }
  };

  public javafx.event.EventHandler<WindowEvent> getOpenEventHandler(){
    return openEventHandler;
  }

  @Override
  public void onReceiveString(TCPConnection tcpConnection, String value) {
    printInChatArea(value);
  }

  @Override
  public void onDisconnect(TCPConnection tcpConnection) {
    printInChatArea("connection is over");
  }

  @Override
  public void onException(TCPConnection tcpConnection, Exception e)
  {
    //printInChatArea("Exception: " + e);
  }
}

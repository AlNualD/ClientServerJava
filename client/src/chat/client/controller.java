package chat.client;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import network.pckg.TCPConnection;
import network.pckg.TCPConnectionListener;

public class controller implements TCPConnectionListener {

  private TCPConnection connection;

  private String nickname;
  @FXML private Button connectionButton;
  @FXML private TextArea chatArea;
  @FXML private TextArea messageArea;
  @FXML private TextField nicknameTF;

  public void sendButtonClicked() {

    System.out.println("Its work!");
    String msg = messageArea.getText();
    if (msg.equals("")) return;
    msg = nickname + ": " + msg;
    connection.sendMsg(msg);
    // chatArea.appendText(messageArea.getText() + "\n");
    messageArea.setText("");
  }

  public void connectionButtonClicked() {
    // if (connectionButton.getText().equals("connection")){
    if (connectionButton.getText().equals("disconnection")) {
      connection.disconnect();
      connectionButton.setText("connection");
      messageArea.setEditable(false);
      nicknameTF.setEditable(true);
      return;
    }
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
  public void onException(TCPConnection tcpConnection, Exception e) {
    printInChatArea("Exception: " + e);
  }
}

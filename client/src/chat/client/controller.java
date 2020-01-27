package chat.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import network.pckg.TCPConnection;
import network.pckg.TCPConnectionListener;

import java.io.IOException;


public class controller implements TCPConnectionListener {

    private TCPConnection connection;

    @FXML
    private TextArea chatArea;
    @FXML
    private TextArea messageArea;
    @FXML
    private TextField nicknameTF;
    public void sendButtonClicked(){
        System.out.println("Its work!");
        chatArea.appendText(messageArea.getText() + "\n");
        messageArea.setText("");
    }
    public void connectionButtonClicked(){
        String nickname = nicknameTF.getText();
        if (checkNickname(nickname)) {
            try {
                connection = new TCPConnection(this, "127.0.0.1",8000);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("smthng wrong");
            }
        }
    }

    private boolean checkNickname(String nickname){
        if(nickname == "") {
            return false;
        }
        else {
            return true;
        }
    }

    private void printInChatArea(String msg){
        
    }


    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {

    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {

    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {

    }
}

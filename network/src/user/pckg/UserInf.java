package user.pckg;

import network.pckg.TCPConnection;

public class UserInf {
    private int id;
    private String nickname;
    private TCPConnection tcpConnection;

    public UserInf(int id, String nickname, TCPConnection tcpConnection){
        this.id = id;
        this.nickname = nickname;
        this.tcpConnection = tcpConnection;
    }

    public UserInf(TCPConnection tcpConnection){
        this.tcpConnection = tcpConnection;
    }


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public TCPConnection getTcpConnection() {
        return tcpConnection;
    }

    public void setTcpConnection(TCPConnection tcpConnection) {
        this.tcpConnection = tcpConnection;
    }
}

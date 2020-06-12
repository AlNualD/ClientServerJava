package user.pckg;

import network.pckg.TCPConnection;

public class UserInf {
  private int id = "name".hashCode()%10000;
  private String nickname = "name";
  private  String passwd = "passwd";
  private TCPConnection tcpConnection;
  private int curGroup = 0;

  public boolean openToAll(){
    return curGroup == 0;
  }
  public UserInf(int id, String nickname, String passwd) {
    this.id = id;
    this.nickname = nickname;
    this.passwd = passwd;
  }


  public UserInf(TCPConnection tcpConnection) {
    this.tcpConnection = tcpConnection;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
    id = nickname.hashCode() % 100000;
  }

  public int getId() {
    return id;
  }
  public void setId(int id){
    this.id = id;
  }

  public TCPConnection getTcpConnection() {
    return tcpConnection;
  }

  public void setTcpConnection(TCPConnection tcpConnection) {
    this.tcpConnection = tcpConnection;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    if (passwd.length() > 0 && passwd.length() < 30)
    this.passwd = passwd;
  }

  public int getCurGroup() {
    return curGroup;
  }

  public void setCurGroup(int curGroup) {
    this.curGroup = curGroup;
  }
}

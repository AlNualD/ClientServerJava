package common;

public enum commands {
  SEND_ALL,
  SEND_GROUP,
  SEND_SINGLE,
  LOGIN,
  EXIT,
  ROLL_ME,
  NEW_USER,
  GROUP_NEW,
  CONNECT_GROUP,
  ERROR;

  public static String returnRegex(commands command) {
    switch (command) {
      case LOGIN:
        return "^##LOGIN##.*";
      case SEND_ALL:
        return "^##SEND_ALL##.*";
      case SEND_GROUP: return "^##GROUP_SEND##.*";
      case SEND_SINGLE:
        return "^##SEND_SINGLE##.*";
      case EXIT: return "^##DISCONNECT##";
      case ROLL_ME: return  "^##ROLL_DICE##.*";
      case CONNECT_GROUP: return "^##CONNECTGROUP##.*";
      case NEW_USER: return "^##NEW_USER##.*";
      case GROUP_NEW: return "^##NEW_GROUP##.*";
      case ERROR:
        return "ERR";
    }
    return "ERR2";
  }

  public  static String returnCommand (commands command){
    switch (command) {
      case LOGIN:
        return "##LOGIN##";
      case SEND_ALL:
        return "##SEND_ALL##";
      case SEND_GROUP: return  "##GROUP_SEND##";
      case SEND_SINGLE:
        return "##SEND_SINGLE##";
      case EXIT:
        return "##DISCONNECT##";
      case ROLL_ME: return "##ROLL_DICE##";
      case CONNECT_GROUP: return "##CONNECTGROUP##";
      case NEW_USER: return "##NEW_USER##";
      case GROUP_NEW: return "##NEW_GROUP##";
    }
    return  "ERR";
  }
}

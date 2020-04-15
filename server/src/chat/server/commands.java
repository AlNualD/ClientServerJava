package chat.server;

public enum commands {
    SEND_ALL,
    SEND_SINGLE,
    LOGIN,
    ERROR;

    public static String returnRegex(commands command){
        switch (command){
            case LOGIN: return "^##LOGIN##.*";
            case SEND_ALL: return "^##SEND_ALL##.*";
            case SEND_SINGLE: return "^##SEND_SINGLE##.*";
            case ERROR: return "";
        }
        return "";
    }
   }

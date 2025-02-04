package hr.java.game.hex_projekt_maksrezek.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatService extends Remote{

    String REMOTE_OBJECT_NAME = "hr.tvz.rmi.service";
    //String REMOTE_OBJECT_NAME = "java.rmi.server.hostname";
    void sendChatMessage(String chatMessage) throws RemoteException;
    List<String> returnChatHistory() throws RemoteException;

}

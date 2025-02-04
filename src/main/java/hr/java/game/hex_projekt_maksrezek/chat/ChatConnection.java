package hr.java.game.hex_projekt_maksrezek.chat;

import hr.java.game.hex_projekt_maksrezek.HexApplication;
import hr.java.game.hex_projekt_maksrezek.model.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ChatConnection {

    public static ChatService connectToChat(TextArea messageArea){
        ChatService stub;
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", ChatServer.RMI_PORT);
            stub = (ChatService) registry.lookup(ChatService.REMOTE_OBJECT_NAME);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> refreshChatTextArea(stub,messageArea)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();

        return stub;
    }

    public static void refreshChatTextArea(ChatService stub, TextArea messageArea) {
        List<String> chatHistory  = null;
        try {
            chatHistory = stub.returnChatHistory();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sb = new StringBuilder();

        for(String message : chatHistory) {
            sb.append(message);
            sb.append("\n");
        }

        messageArea.setText(sb.toString());
    }

    public static void sendChatMessage(TextField chatField,TextArea messageArea, ChatService stub) {
        String chatMessage = chatField.getText();
        String playerName = HexApplication.player.name();

        if(HexApplication.player.name().equals(Player.PLAYER_SINGLE.name())){
            messageArea.appendText(playerName + ": " + chatMessage + "\n");
            chatField.clear();
        }
        else {
            try {
                stub.sendChatMessage(playerName + ": " + chatMessage);
                ChatConnection.refreshChatTextArea(stub,messageArea);
                chatField.clear();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

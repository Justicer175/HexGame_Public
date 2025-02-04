package hr.java.game.hex_projekt_maksrezek.winnerChecker;

import hr.java.game.hex_projekt_maksrezek.model.Colors;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.Serializable;

public class PrintOutWinner implements Serializable {

    public static void printOut(Colors color){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("We have a winner!");
        alert.setHeaderText("Winner is " + color.toString() + " !");
        alert.setContentText("Congradulations!");
        alert.show();
    }
}

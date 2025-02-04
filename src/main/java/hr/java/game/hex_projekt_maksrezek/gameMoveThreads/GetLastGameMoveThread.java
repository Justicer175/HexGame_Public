package hr.java.game.hex_projekt_maksrezek.gameMoveThreads;

import hr.java.game.hex_projekt_maksrezek.HexGameLogic;
import hr.java.game.hex_projekt_maksrezek.model.GameMove;
import javafx.scene.control.Label;

public class GetLastGameMoveThread extends GameMoveThread implements Runnable {

    private Label label;

    public GetLastGameMoveThread(Label label) {
        this.label = label;
    }

    @Override
    public void run() {
        if(HexGameLogic.numberOfTurnsPlayed == 0 || HexGameLogic.disableDuringReplay){
            label.setText("No new moves played");
        }
        else {
            GameMove lastGameMove = getLastGameMoveFromFile();

            label.setText("Last game move: Color: "
                    + lastGameMove.getColor() + " | Position: ("
                    + lastGameMove.getPositionX() + ", "
                    + lastGameMove.getPositionY() + ") | Time: "
                    + lastGameMove.getLocalDateTime());
        }
        }
}

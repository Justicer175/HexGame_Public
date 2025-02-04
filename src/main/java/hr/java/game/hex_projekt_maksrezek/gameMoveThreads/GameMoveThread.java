package hr.java.game.hex_projekt_maksrezek.gameMoveThreads;

import hr.java.game.hex_projekt_maksrezek.model.GameMove;
import hr.java.game.hex_projekt_maksrezek.utils.GameMoveUtils;

public abstract class GameMoveThread {

    private static Boolean gameMoveFileAccessInProgress = false;

    protected synchronized void saveNewGameMoveToFile(GameMove gameMove) {
        while(gameMoveFileAccessInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        gameMoveFileAccessInProgress = true;

        GameMoveUtils.saveNewGameMove(gameMove);

        gameMoveFileAccessInProgress = false;

        notifyAll();
    }

    protected synchronized GameMove getLastGameMoveFromFile() {
        while(gameMoveFileAccessInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        gameMoveFileAccessInProgress = true;

        GameMove lastGameMove = GameMoveUtils.getLastGameMove();

        gameMoveFileAccessInProgress = false;

        notifyAll();

        return lastGameMove;
    }

}

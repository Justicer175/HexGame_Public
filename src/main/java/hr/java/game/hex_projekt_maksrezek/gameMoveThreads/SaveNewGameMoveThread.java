package hr.java.game.hex_projekt_maksrezek.gameMoveThreads;

import hr.java.game.hex_projekt_maksrezek.model.GameMove;

public class SaveNewGameMoveThread extends GameMoveThread implements Runnable {

    private GameMove gameMove;

    public SaveNewGameMoveThread(GameMove newGameMove) {
        this.gameMove = newGameMove;
    }

    @Override
    public void run() {
        saveNewGameMoveToFile(gameMove);
    }
}
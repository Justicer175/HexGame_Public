package hr.java.game.hex_projekt_maksrezek.model;

import lombok.Getter;

@Getter
public enum Keys {
    RMI_HOST("rmi.host"), RMI_PORT("rmi.port"),
    START_OF_ROWS("START_OF_ROWS");

    private String key;

    private Keys(String key) {
        this.key = key;
    }

}

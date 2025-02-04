package hr.java.game.hex_projekt_maksrezek.model;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GameMove implements Serializable {
    private Colors color;
    private Integer positionX;
    private Integer positionY;
    private String localDateTime;
}

package game;

import java.util.ArrayList;
import java.util.List;

public interface Move {
    /**
     * Convert the move to a char to reduce space
     * @return The corresponding char
     */
    char toChar();
}

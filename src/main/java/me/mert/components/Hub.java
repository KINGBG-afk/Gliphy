package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.game.CurrencyManager;
import me.mert.game.LevelManager;
import me.mert.glyph.Glyph;

public class Hub extends Component {
    private static int count = 0;

    public Hub(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.HUB);

        // hub accepts from all 4 directions
        addinput(Direction.NORTH);
        addinput(Direction.SOUTH);
        addinput(Direction.EAST);
        addinput(Direction.WEST);
    }

    public static void addCount() {
        if (count < 5)
            count++;
    }
    
    public static void subtractCount() {
        if (count > 0)
            count--;
    }

    public static boolean canPlace() {
        return count < 5;
    }

    @Override
    public void update() {
        for (Port in : inputs) {
            if (in.hasItem()) {
                Glyph g = in.eject();

                LevelManager.getInstance().addToStored(g);
                CurrencyManager.getInstance().add(1);
            }
        }
    }
}

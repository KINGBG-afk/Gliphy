package me.mert.components;

import me.mert.core.enums.ComponentType;
import me.mert.core.enums.Direction;
import me.mert.game.CurrencyManager;
import me.mert.game.LevelManager;
import me.mert.glyph.Glyph;

public class Hub extends Component {
    public Hub(int i, int j, Direction dir) {
        super(i, j, dir, new int[] { 1, 1 }, ComponentType.HUB);

        // hub accepts from all 4 directions
        addinput(Direction.NORTH);
        addinput(Direction.SOUTH);
        addinput(Direction.EAST);
        addinput(Direction.WEST);
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

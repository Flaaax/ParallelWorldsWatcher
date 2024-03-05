package ParallelWatcher;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;

import java.util.ArrayList;
import java.util.Iterator;

public class WatcherHelper {

    public static AbstractOrb getOrbChannelPos() {
        ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;
        if (orbs.isEmpty()) {
            return null;
        }
        Iterator<AbstractOrb> var3 = orbs.iterator();
        while (var3.hasNext()) {
            AbstractOrb orb = var3.next();
            if (orb instanceof EmptyOrbSlot) {
                return orb;
            }
        }
        return orbs.get(orbs.size() - 1);
    }

}

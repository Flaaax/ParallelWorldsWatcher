package ParallelWatcher.patches;

import ParallelWatcher.ParallelWatcherMod;
import ParallelWatcher.cardmods.SelfChannelMod;
import ParallelWatcher.orbs.SpiritOrb;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

@SpirePatch(clz = UseCardAction.class, method = "update")
public class GoSomewhereElsePatch {
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals(CardGroup.class.getName()) && m.getMethodName().equals("moveToDiscardPile")) {
                    m.replace("if (" + GoSomewhereElsePatch.class.getName() + ".Do($1)) {$_ = $proceed($$);}");
                }
            }
        };
    }

    public static boolean Do(final AbstractCard card) {
        if (card.purgeOnUse) {
            return true;
        }
        if (CardModifierManager.hasModifier(card, SelfChannelMod.ID)) {
            //card.target_x = (FunctionHelper.cardPositions[FunctionHelper.held.size()]).x;
            //card.target_y = (FunctionHelper.cardPositions[FunctionHelper.held.size()]).y;
            //AbstractDungeon.player.limbo.addToTop(card);
            //AbstractDungeon.player.limbo.removeCard(card);
            ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;
            if (!orbs.isEmpty()) {
                //card.target_x = orbs.get(orbs.size() - 1).cX;
                //card.target_y = orbs.get(orbs.size() - 1).cY;
                //Hitbox hb=AbstractDungeon.player.hb;
                //card.target_x=hb.cX;
                //card.target_y=hb.cY;
                //card.targetDrawScale = card.drawScale;
                AbstractDungeon.player.limbo.addToTop(card);
                card.tags.remove(ParallelWatcherMod.SELF_CHANNEL);
                AbstractDungeon.actionManager.addToTop(new ChannelAction(new SpiritOrb(card, AbstractDungeon.player.limbo)));
                return false;
            } else return true;
        }
        return true;
    }
}

package ParallelWatcher.cards;

import ParallelWatcher.Actions.PlaceActualCardIntoOrb;
import ParallelWatcher.cards.SoulCards.SoulDefend;
import ParallelWatcher.cards.SoulCards.SoulDraw;
import ParallelWatcher.cards.SoulCards.SoulStrike;
import ParallelWatcher.character.ParallelWatcher;
import ParallelWatcher.util.CardStats;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;

public class SoulCall extends BaseSpiritorCard {

    public static final String ID = makeID("SoulCall");
    private static final int CARDS_TO_CHANNEL = 1;
    private static final int UPG = 1;
    private static final CardStats info = new CardStats(
            ParallelWatcher.Enums.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or something similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.BASIC, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1 //The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    );

    private static final ArrayList<AbstractCard> cardPreviewList = new ArrayList<>();
    private int currentCard = 0;
    private float timer = 0;
    private final float displayInterval = 1.2F;     // 设置卡牌预览切换的时间间隔为1.5秒

    private boolean resetTimer = false;
    private final Random rng = AbstractDungeon.cardRandomRng;

    //todo channel 2 random soul card. upg: select 2
    public SoulCall() {
        super(ID, info);
        setMagic(CARDS_TO_CHANNEL);
        cardPreviewList.add(new SoulStrike());
        cardPreviewList.add(new SoulDefend());
        cardPreviewList.add(new SoulDraw());
        this.cardsToPreview = cardPreviewList.get(0);
    }

    @Override
    public void update() {
        super.update();
        float deltaTime = Gdx.graphics.getDeltaTime();
        timer += deltaTime; // 累加时间

        if (this.hb.hovered) {
            if (!resetTimer) {
                resetTimer = true;
                timer = 0;
            }
        } else {
            resetTimer = false;
        }

        if (timer >= displayInterval) {
            currentCard = (currentCard + 1) % cardPreviewList.size(); // 更新预览卡牌索引，如果到达列表末尾则循环回第一个
            this.cardsToPreview = cardPreviewList.get(currentCard); // 更新预览的卡牌
            timer = 0; // 重置计时器
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG);
            upgradeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < this.magicNumber; i++) {
            this.addToBot(new PlaceActualCardIntoOrb(getRandomCard()));
        }
    }

    static public AbstractCard getRandomCard(Random rng) {
        return cardPreviewList.get(rng.random(cardPreviewList.size() - 1)).makeStatEquivalentCopy();
    }

    public AbstractCard getRandomCard() {
        return cardPreviewList.get(this.rng.random(cardPreviewList.size() - 1)).makeStatEquivalentCopy();
    }
}

package ParallelWatcher;

import ParallelWatcher.character.ParallelWatcher;
import ParallelWatcher.orbs.SpiritOrb;
import ParallelWatcher.powers.SoulTargetPower;
import ParallelWatcher.relics.BaseRelic;
import ParallelWatcher.util.GeneralUtils;
import ParallelWatcher.util.KeywordInfo;
import ParallelWatcher.util.TextureLoader;
import ParallelWatcher.cards.BaseSpiritorCard;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;
import com.badlogic.gdx.graphics.Color;

import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class ParallelWatcherMod implements
        EditRelicsSubscriber,
        EditCardsSubscriber,
        EditCharactersSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        //OnPlayerTurnStartSubscriber,
        OnPlayerTurnStartPostDrawSubscriber {
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this

    static {
        loadModInfo();
    }

    private static final String resourcesFolder = checkResourcesPath();

    private static final Color cardColor = new Color(128f / 255f, 128f / 255f, 128f / 255f, 1f);
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.

    //Images
    private static final String BG_ATTACK = characterPath("cardback/bg_attack.png");
    private static final String BG_ATTACK_P = characterPath("cardback/bg_attack_p.png");
    private static final String BG_SKILL = characterPath("cardback/bg_skill.png");
    private static final String BG_SKILL_P = characterPath("cardback/bg_skill_p.png");
    private static final String BG_POWER = characterPath("cardback/bg_power.png");
    private static final String BG_POWER_P = characterPath("cardback/bg_power_p.png");
    private static final String ENERGY_ORB = characterPath("cardback/energy_orb.png");
    private static final String ENERGY_ORB_P = characterPath("cardback/energy_orb_p.png");
    private static final String SMALL_ORB = characterPath("cardback/small_orb.png");

    private static final String CHAR_SELECT_BUTTON = characterPath("select/button.png");
    private static final String CHAR_SELECT_PORTRAIT = characterPath("select/portrait.png");

    @SpireEnum
    public static AbstractCard.CardTags SOUL_CARD;

    @SpireEnum
    public static AbstractCard.CardTags SELF_CHANNEL;

    @SpireEnum
    public static AbstractCard.CardTags RETURN;

    @SpireEnum
    public static CardGroup.CardGroupType SOUL_CARDS;

    //This is used to prefix the IDs of various objects like cards and relics,
    //to avoid conflicts between different mods using the same name for things.
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new ParallelWatcherMod();
        BaseMod.addColor(ParallelWatcher.Enums.CARD_COLOR, cardColor, BG_ATTACK, BG_SKILL, BG_POWER, ENERGY_ORB,
                BG_ATTACK_P, BG_SKILL_P, BG_POWER_P, ENERGY_ORB_P,
                SMALL_ORB);
    }

    public ParallelWatcherMod() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");
    }

    @Override
    public void receivePostInitialize() {
        //This loads the image used as an icon in the in-game mods menu.
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        //Set up the mod information displayed in the in-game mods menu.
        //The information used is taken from your pom.xml file.

        //If you want to set up a config panel, that will be done here.
        //The Mod Badges page has a basic example of this, but setting up config is overall a bit complex.
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString() {
        return Settings.language.name().toLowerCase();
    }

    private static String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
                //defaultLanguage = getLangString();
            } catch (GdxRuntimeException e) {
                e.printStackTrace();
                loadLocalization(defaultLanguage);
            }
        } else {
            loadLocalization(defaultLanguage); //no exception catching for default localization; you better have at least one that works.
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(CardStrings.class,
                localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,
                localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);

        if (!defaultLanguage.equals(getLangString())) {
            try {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                keywords = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            } catch (Exception e) {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        } else {
            for (KeywordInfo keyword : keywords) {
                keyword.prep();
                registerKeyword(keyword);
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
        /*System.out.println("Adding keyword " + modID.toLowerCase() + " " + info.PROPER_NAME + "\n");
        System.out.println("NAMES:");
        for (int i = 0; i < info.NAMES.length; i++) {
            System.out.println(info.NAMES[i] + " ");
        }
        System.out.println("\n" + info.DESCRIPTION + "\n");*/
        if (!info.ID.isEmpty()) {
            keywords.put(info.ID, info);
        }
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }

    public static String characterPath(String file) {
        return resourcesFolder + "/images/character/" + file;
    }

    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }

    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    /**
     * Checks the expected resources path based on the package name.
     */
    private static String checkResourcesPath() {
        String name = ParallelWatcherMod.class.getName(); //getPackage can be iffy with patching, so class name is used instead.
        int separator = name.indexOf('.');
        name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);
        if (resources.child("images").exists() && resources.child("localization").exists()) {
            return name;
        }

        throw new RuntimeException("\n\tFailed to find resources folder; expected it to be named \"" + name + "\"." +
                " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                "\tat the top of the " + ParallelWatcherMod.class.getSimpleName() + " java file.");
    }

    /**
     * This determines the mod's ID based on information stored by ModTheSpire.
     */
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(ParallelWatcherMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new ParallelWatcher(),
                CHAR_SELECT_BUTTON, CHAR_SELECT_PORTRAIT, ParallelWatcher.Enums.PARALLEL_WATCHER);
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BaseSpiritorCard.class) //In the same package as this class
                .setDefaultSeen(true) //And marks them as seen in the compendium
                .cards(); //Adds the cards
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID) //Loads files from this mod
                .packageFilter(BaseRelic.class) //In the same package as this class
                .any(BaseRelic.class, (info, relic) -> { //Run this code for any classes that extend this class
                    if (relic.pool != null)
                        BaseMod.addRelicToCustomPool(relic, relic.pool); //Register a custom character specific relic
                    else
                        BaseMod.addRelic(relic, relic.relicType); //Register a shared or base game character specific relic

                    //If the class is annotated with @AutoAdd.Seen, it will be marked as seen, making it visible in the relic library.
                    //If you want all your relics to be visible by default, just remove this if statement.
                    //if (info.seen)
                    UnlockTracker.markRelicAsSeen(relic.relicId);
                });
    }

    public static boolean canSpawnSpiritOrb(boolean ignoreOtherOrb) {
        if (!ignoreOtherOrb) {
            return AbstractDungeon.player.hasEmptyOrb();
        } else {
            Iterator<AbstractOrb> var3 = AbstractDungeon.player.orbs.iterator();
            while (var3.hasNext()) {
                AbstractOrb orb = var3.next();
                if (!(orb instanceof SpiritOrb)) {
                    return true;
                }
            }
            return false;
        }
         /* if (AbstractDungeon.player.hasEmptyOrb() || (AbstractDungeon.player.masterMaxOrbs == 0 && AbstractDungeon.player.maxOrbs == 0)) {
               return true;
           }
         for (AbstractOrb o : AbstractDungeon.player.orbs) {
                 if (!(o instanceof SpiritOrb)) {
                       return true;
                      }
               }
            // UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString("Guardian:UIOptions");
           //AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0F, "TEXT_CANT_SPAWN", true));

            return false;
         }*/
    }

    public static boolean canSpawnSpiritOrb() {
        return canSpawnSpiritOrb(true);
    }

    public static AbstractMonster getOrbRandomMonster(AbstractMonster exception) {
        MonsterGroup monsterGroup = AbstractDungeon.getCurrMapNode().room.monsters;
        Random rng = AbstractDungeon.cardRandomRng;
        if (monsterGroup.areMonstersBasicallyDead()) {
            return null;
        }
        if (monsterGroup.monsters.size() == 1) {
            return monsterGroup.monsters.get(0);
        } else {
            ArrayList<AbstractMonster> tmp = new ArrayList<>();
            Iterator<AbstractMonster> var5 = monsterGroup.monsters.iterator();

            while (var5.hasNext()) {
                AbstractMonster m = (AbstractMonster) var5.next();

                if (!(m.halfDead || m.isDying || m.isEscaping || (exception != null && exception.equals(m)))) {
                    if (m.hasPower(SoulTargetPower.POWER_ID)) {               //the only extra logic
                        return m;
                    }
                    tmp.add(m);
                }
            }

            if (tmp.size() <= 0) {
                return null;
            } else {
                return tmp.get(rng.random(0, tmp.size() - 1));
            }
        }

    }

    public static AbstractMonster getOrbRandomMonster() {
        return getOrbRandomMonster(null);
    }

    @Override
    public void receiveOnPlayerTurnStartPostDraw() {
        //todo  maybe should store card pile stat
    }

    public static CardGroup getSoulCards() {
        ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;
        CardGroup retVal = new CardGroup(SOUL_CARDS);

        Iterator<AbstractOrb> var3 = orbs.iterator();
        while (var3.hasNext()) {
            AbstractOrb orb = var3.next();
            if (orb instanceof SpiritOrb && ((SpiritOrb) orb).card != null) {
                retVal.addToTop(((SpiritOrb) orb).card);
            }
        }

        return retVal;
    }

    public static AbstractOrb getBelongedOrb(AbstractCard card) {
        if (card instanceof BaseSpiritorCard) {
            return ((BaseSpiritorCard) card).belongedOrb;
        }
        for (AbstractOrb orb : AbstractDungeon.player.orbs) {
            if (orb instanceof SpiritOrb && ((SpiritOrb) orb).card == card) {
                return orb;
            }
        }
        return null;
    }
}

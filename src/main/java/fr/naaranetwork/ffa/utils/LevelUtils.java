package fr.naaranetwork.ffa.utils;

import database.profiles.FFAProfile;
import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.scoreboard.IndexOption;
import fr.naaranetwork.ffa.scoreboard.ScoreboardHandler;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import utils.ScoreboardMaster;
import utils.TitleManager;

import java.util.Random;

public class LevelUtils {

    private final static Random RANDOM = new Random();

    // FFA naaranetwork Levels depend on the following function

    //f(x) = x^2 + 2x + 10
    // Where x is the current level, and f(x) the needed experience to success the level

    // Return a random XP Gain Between 1 and 4.
    private static int xpGain() {
        return 1 + RANDOM.nextInt(4);
    }

    public static int requiredXp(int level) {
        return (level * level) + (2 * level) + 10;
    }

    private static void sendLevelUpSound(Player player) {
        Location location = player.getLocation();
        String sound = CraftSound.getSound(Sound.LEVEL_UP);

        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(sound,
                location.getX(), location.getY() + 0.5, location.getZ(), 0.7f, 0.7f);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static String getXPBar(int currentXp, int requiredXp) {
        StringBuilder barBuilder = new StringBuilder();
        barBuilder.append("§f");
        barBuilder.append(currentXp);
        barBuilder.append("/");
        barBuilder.append(requiredXp);
        barBuilder.append(" (");

        double percentage = ((double) currentXp * 100d) / (double) requiredXp;

        barBuilder.append(Math.round(percentage));

        return barBuilder.append("%)").toString();
    }

    public static void commitXp(FFAProfile profile, Player player) {

        int currentLevel = profile.getLevel();
        int currentXp = profile.getExperience();
        int requiredXp = requiredXp(currentLevel);
        ScoreboardHandler scoreboardHandler = FFA.getInstance().getScoreboardHandler();

        // Adding a random xp
        currentXp += xpGain();

        // If enough xp, new Level !
        if (currentXp >= requiredXp) {
            profile.setLevel(currentLevel + 1);

            TitleManager.sendTitle(player, "",
                    "§cPassage au niveau " + profile.getLevel() + " !", 30);
            sendLevelUpSound(player);

            player.getBoard().setValue(IndexOption.LEVEL, profile.getLevel());
        }

        // Apply modulo to get new xp
        currentXp %= requiredXp;

        // Update required if needed
        if (currentLevel < profile.getLevel()) {
            requiredXp = requiredXp(profile.getLevel());
        }

        // Update scoreboard
        profile.setExperience(currentXp);
        player.getBoard().setValue(IndexOption.XP, LevelUtils.getXPBar(
                profile.getExperience(), LevelUtils.requiredXp(profile.getLevel())));
    }

}

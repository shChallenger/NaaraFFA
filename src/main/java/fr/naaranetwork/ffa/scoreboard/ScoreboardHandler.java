package fr.naaranetwork.ffa.scoreboard;

import database.profiles.FFAProfile;
import fr.malgret.board.IPacketBoard;
import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.utils.LevelUtils;
import fr.naaranetwork.ffa.utils.Symbols;
import fr.naaranetwork.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ScoreboardHandler {

    private final FFA ffa;
    private final int[] spacesIndex = new int[]{11, 7, 3, 1};

    public ScoreboardHandler(FFA ffa) {
        this.ffa = ffa;
    }

    public void handleScoreboard(Player player) {
        FFAProfile profile = ffa.getProfileHandler().getProfile(player.getUniqueId());
        IPacketBoard packetBoard = player.getBoard();
        packetBoard.setTitle(Utils.translate("&8" + Symbols.ARROW_RIGHT + " &6&lNaara&e&lFFA &8" + Symbols.ARROW_LEFT));

        for (int spaceIndex : spacesIndex) {
            packetBoard.setLine(spaceIndex, "");
        }

        int players = Bukkit.getOnlinePlayers().size();

        for (IndexOption option : IndexOption.values()) {
            packetBoard.createLine(option);
        }

        packetBoard.setValue(IndexOption.LEVEL, profile.getLevel());

        packetBoard.setValue(IndexOption.KILLS, profile.getKills());
        packetBoard.setValue(IndexOption.DEATHS, profile.getDeaths());
        packetBoard.setValue(IndexOption.RATIO, profile.getCalculatedRatio());

        packetBoard.setValue(IndexOption.PLAYERS, players);
        packetBoard.setValue(IndexOption.XP, LevelUtils.getXPBar(
                profile.getExperience(), LevelUtils.requiredXp(profile.getLevel())));
    }

    public int getIndex(IndexOption option) {
        return option.getIndex();
    }

}

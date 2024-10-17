package fr.naaranetwork.ffa.scoreboard;

import fr.malgret.board.IPacketBoard;
import fr.malgret.board.value.IValueIndex;
import fr.naaranetwork.ffa.utils.Utils;

public enum IndexOption implements IValueIndex {

    RANK("§bRank : §f#", 10),
    LEVEL("§bLevel : §f", 9),
    XP("§bXP : §f", 8),

    KILLS(Utils.translate("&aKills : &f"), 6),
    DEATHS(Utils.translate("&cMorts : &f"), 5),
    RATIO(Utils.translate("&7K/D : &f"), 4),

    PLAYERS(Utils.translate("&7Connectés : &f"), 2);

    private final String key;
    private final int index;

    IndexOption(String key, int index) {
        this.key = key;
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getStaticKey() {
        return key;
    }

    public static void initFor(IPacketBoard board) {
        for (IndexOption option : values()) {
            board.createLine(option);
        }
    }
}

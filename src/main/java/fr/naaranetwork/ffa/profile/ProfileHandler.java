package fr.naaranetwork.ffa.profile;

import com.google.common.base.Preconditions;
import database.ServerDatabase;
import database.profiles.FFAProfile;
import database.profiles.ProfileImpl;
import database.profiles.ProfileState;
import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.scoreboard.IndexOption;
import fr.naaranetwork.ffa.tasks.RespawnTask;
import fr.naaranetwork.ffa.utils.LevelUtils;
import fr.naaranetwork.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import utils.ActionBarManager;

import java.util.*;

public class ProfileHandler {

    private final FFA ffa;
    private final Map<UUID, FFAProfile> profiles;
    private final ServerDatabase database;
    private int spectatorsSize;

    private final String[] joinMessages = new String[]
            {
                    "§eBienvenue sur le FFA de NaaraNetwork !",
                    "§eMode Modérateur : Vous êtes spectateur."
            };

    private final List<FFAProfile> sortedProfiles;

    public ProfileHandler(FFA ffa) {
        this.ffa = ffa;
        this.database = ffa.getServerDatabase();
        this.profiles = database.loadFFAProfiles();
        this.sortedProfiles = new ArrayList<>(profiles.values());
        this.spectatorsSize = 0;
    }

    public void createProfile(UUID uniqueId) {
        FFAProfile profile = new ProfileImpl(uniqueId);

        database.insertFFAPlayer(uniqueId.toString());
        profiles.put(uniqueId, profile);
        sortedProfiles.add(profile);
    }

    public void updateProfile(UUID uniqueId) {
        if (hasProfile(uniqueId)) {
            return;
        }

        FFAProfile profile = getProfile(uniqueId);
        database.updateFFAPlayer(profile);
    }

    public void handleSpectating(Player player) {
        Preconditions.checkNotNull(player, "Player cannot be null");

        FFAProfile profile = getProfile(player.getUniqueId());

        spectatorsSize += (profile.getState() == ProfileState.SPECTATING) ? -1 : 1;

        if (profile.getState() == ProfileState.SPECTATING) {
            profile.setState(ProfileState.WAITING);
            handleSpawn(player);
            handleView(player, false);
            player.setFlying(false);
            player.setAllowFlight(false);
            ActionBarManager.sendActionBarMessage(player, "§eVous n'êtes plus en mode spectateur.");
            return;
        }

        profile.setState(ProfileState.SPECTATING);
        player.setAllowFlight(true);
        player.setFlying(true);

        handleView(player, true);
        handleInventory(player, true);
        ActionBarManager.sendActionBarMessage(player, "§eVous êtes désormais en mode spectateur.");
    }

    private void handleInventory(Player player, boolean spectating) {
        Utils.clearPlayer(player);
        PlayerInventory inventory = player.getInventory();

        if (spectating) {
            player.getInventory().setItem(8, Utils.INVENTORY[8]);
        } else {
            inventory.setArmorContents(Utils.ARMOR);
            inventory.setContents(Utils.INVENTORY);
        }

        player.updateInventory();
    }

    // It is very important to differentiate handleSpawn than resetting state for spectating Players.

    public void handleSpawn(Player player) {
        FFAProfile profile = getProfile(player.getUniqueId());
        profile.resetKillStreak();

        boolean isMod = ffa.getServer().getModPlayers().contains(player.getName());
        boolean stillSpectating = profile.getState().equals(ProfileState.SPECTATING);

        profile.setState(isMod || stillSpectating ? ProfileState.SPECTATING : ProfileState.WAITING);

        player.teleport(Utils.spawn);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

        if (!isMod)
            handleInventory(player, stillSpectating);
    }

    private void handleView(Player player, boolean spectator) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (spectator) {
                online.clearPlayer(player);
            } else {
                online.fillPlayer(player);
            }
        }

        // Optimization to avoid if (online == player) continue in the loop

        if (spectator) {
            player.fillPlayer(player);
        } else {
            player.clearPlayer(player);
        }
    }

    private void handleViews(Player newPlayer, boolean isMod) {
        Server server = ffa.getServer();

        // Make sure all players are correctly render.

        for (Player online : server.getOnlinePlayers()) {
            newPlayer.hidePlayer(online);
            online.hidePlayer(newPlayer);
        }

        for (Player online : server.getOnlinePlayers()) {
            newPlayer.showPlayer(online);
            if (!isMod) online.showPlayer(newPlayer);
        }

        // Hide spectators (but keep in the tab), and fully hide moderators.

        for (FFAProfile profile : profiles.values()) {
            if (profile.getState() != ProfileState.SPECTATING)
                continue;

            if (server.getModPlayers().contains(profile.getName())) {
                newPlayer.hidePlayer(profile.getPlayer());
            } else {
                newPlayer.clearPlayer(profile.getPlayer());
            }
        }

        // Make sure the player shows itself

        newPlayer.showPlayer(newPlayer);
    }

    public void handleJoin(Player player) {
        if (hasProfile(player.getUniqueId())) {
            createProfile(player.getUniqueId());
        }

        getProfile(player.getUniqueId()).setOnline(true);

        ffa.getScoreboardHandler().handleScoreboard(player);

        boolean isMod = player.getGameMode() == GameMode.SPECTATOR;

        if (!isMod) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        ActionBarManager.sendActionBarMessage(player, joinMessages[isMod ? 1 : 0]);

        handleSpawn(player);
        handleViews(player, isMod);
    }

    public void handleQuit(Player player) {
        updateProfile(player.getUniqueId());

        FFAProfile profile = getProfile(player.getUniqueId());
        profile.setOnline(false);

        switch (profile.getState()) {
            case PLAYING:
                handleDeath(player, profile.getLastDamager(), true);
            case SPECTATING:
                if (!ffa.getServer().getModPlayers().contains(player.getName()))
                    spectatorsSize--;
            default:
                break;
        }

        profile.setState(ProfileState.WAITING);
    }

    public synchronized void handleDeath(Player player, Player killer, boolean disconnected) {
        FFAProfile profile = getProfile(player.getUniqueId());
        profile.setDeaths(profile.getDeaths() + 1);
        player.setHealth(20.0D);

        player.getBoard().setValue(IndexOption.DEATHS, profile.getDeaths());
        player.getBoard().setValue(IndexOption.RATIO, profile.getCalculatedRatio());

        StringBuilder deathMessage = new StringBuilder();
        deathMessage.append("§c");

        if (killer == null || !killer.isOnline()) {
            deathMessage.append("Vous êtes mort !");
        } else {
            deathMessage.append("Vous avez été tué par ").append(killer.getName()).append(" !");
        }

        ActionBarManager.sendActionBarMessage(player, deathMessage.toString());

        if (!disconnected) {
            player.setGameMode(GameMode.SPECTATOR);

            RespawnTask respawnTask = new RespawnTask(ffa, player);
            respawnTask.runTaskLater(ffa, 20);
        }

        if (killer == null) {
            return;
        }

        FFAProfile profileKiller = getProfile(killer.getUniqueId());

        if (!profileKiller.isOnline() || profileKiller.getState() != ProfileState.PLAYING) {
            return;
        }

        ActionBarManager.sendActionBarMessage(killer, "§bVous avez tué " + killer.getName());

        killer.setHealth(20.0D);
        killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));

        profileKiller.setKills(profileKiller.getKills() + 1);
        profileKiller.addKillStreak();

        if (profileKiller.getKillStreak() % 5 == 0) {
            ffa.getServer().broadcastMessage("§b" + killer.getName() + "§f a une Killstreak de §c"
                    + profileKiller.getKillStreak() + "§f !");
        }

        player.getBoard().setValue(IndexOption.KILLS, profileKiller.getKills());
        player.getBoard().setValue(IndexOption.RATIO, profileKiller.getCalculatedRatio());

        LevelUtils.commitXp(profileKiller, killer);
    }

    public void updateRanks() {
        Collections.sort(sortedProfiles);

        Player currentPlayer;
        int i = 1;

        for (FFAProfile profile : sortedProfiles) {
            if (profile.isOnline())
                profile.getPlayer().getBoard().setValue(IndexOption.RANK, String.valueOf(i));
            i++;
        }
    }

    public boolean hasProfile(UUID uniqueId) {
        return !profiles.containsKey(uniqueId);
    }

    public FFAProfile getProfile(UUID uniqueId) {
        return profiles.get(uniqueId);
    }

    public Map<UUID, FFAProfile> getProfiles() {
        return profiles;
    }

    public int getSpectatorsSize() {
        return spectatorsSize;
    }
}

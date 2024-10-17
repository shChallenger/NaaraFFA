package fr.naaranetwork.ffa;

import database.ServerDatabase;
import fr.naaranetwork.ffa.command.CommandHandler;
import fr.naaranetwork.ffa.event.EventHandler;
import fr.naaranetwork.ffa.item.ItemHandler;
import fr.naaranetwork.ffa.profile.ProfileHandler;
import fr.naaranetwork.ffa.scoreboard.ScoreboardHandler;
import fr.naaranetwork.ffa.tasks.AnimationTask;
import fr.naaranetwork.ffa.tasks.FFATask;
import fr.naaranetwork.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FFA extends JavaPlugin {

    private static FFA instance;

    private ServerDatabase database;
    private ItemHandler itemHandler;
    private ProfileHandler profileHandler;
    private ScoreboardHandler scoreboardHandler;
    private CommandHandler commandHandler;
    private EventHandler eventHandler;

    public static FFA getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        instance = this;
        Utils.spawn = Utils.getLocationFromConfig(Bukkit.getWorld("spawn"), "spawn");
        Utils.loadHologram();

        FFATask ffaTask = new FFATask(this);
        ffaTask.runTaskTimer(this, 0, 20);

        AnimationTask animationTask = new AnimationTask();
        animationTask.runTaskTimer(this, 0, 2);

        this.database = ((CraftServer) getServer()).getDatabase();
        this.profileHandler = new ProfileHandler(this);

        this.itemHandler = new ItemHandler();
        Utils.loadInventory();

        this.scoreboardHandler = new ScoreboardHandler(this);
        this.commandHandler = new CommandHandler(this);
        this.eventHandler = new EventHandler(this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("§cLe serveur redémarre !");
        }
    }

    public ServerDatabase getServerDatabase() {
        return database;
    }

    public ProfileHandler getProfileHandler() {
        return profileHandler;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    public ItemHandler getItemHandler() {
        return itemHandler;
    }
}
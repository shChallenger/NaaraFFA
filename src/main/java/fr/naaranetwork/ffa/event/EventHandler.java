package fr.naaranetwork.ffa.event;

import fr.naaranetwork.ffa.FFA;
import fr.naaranetwork.ffa.event.defaults.FFAListener;
import fr.naaranetwork.ffa.event.defaults.ItemListener;
import fr.naaranetwork.ffa.event.defaults.JoinQuitListener;
import org.bukkit.event.Listener;

public class EventHandler {

    private final FFA ffa;

    public EventHandler(FFA ffa) {
        this.ffa = ffa;
        this.loadEvents();
    }

    private void loadEvents() {
        registerEvent(new JoinQuitListener(ffa));
        registerEvent(new FFAListener(ffa));
        registerEvent(new ItemListener(ffa));
    }

    public void registerEvent(Listener listener) {
        ffa.getServer().getPluginManager().registerEvents(listener, ffa);
    }

}

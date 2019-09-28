package gq.dynitios.cheatbuster;

import gq.dynitios.cheatbuster.listener.RecordListener;
import gq.dynitios.cheatbuster.recorder.PlayerRecorder;
import org.bukkit.plugin.java.JavaPlugin;

public class CheatBusterPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        PlayerRecorder recorder = new PlayerRecorder();
        RecordListener listener = new RecordListener(recorder);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {

    }
}

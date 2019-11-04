package gq.dynitios.cheatbuster;

import gq.dynitios.cheatbuster.listener.RecordListener;
import gq.dynitios.cheatbuster.recorder.PlayerRecorder;
import gq.dynitios.cheatbuster.tps.TpsMeter;
import org.bukkit.plugin.java.JavaPlugin;

public class CheatBusterPlugin extends JavaPlugin {
    private PlayerRecorder recorder;

    @Override
    public void onEnable() {
        recorder = new PlayerRecorder();
        RecordListener listener = new RecordListener(recorder);
        getServer().getPluginManager().registerEvents(listener, this);
        new TpsMeter().runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        recorder.stopRecorder();
    }
}

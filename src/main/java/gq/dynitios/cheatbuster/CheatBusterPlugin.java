package gq.dynitios.cheatbuster;

import gq.dynitios.cheatbuster.exception.LearningSetException;
import gq.dynitios.cheatbuster.listener.RecordListener;
import gq.dynitios.cheatbuster.recorder.PlayerRecorder;
import gq.dynitios.cheatbuster.tps.TpsMeter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CheatBusterPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            PlayerRecorder recorder = new PlayerRecorder();
            RecordListener listener = new RecordListener(recorder);
            getServer().getPluginManager().registerEvents(listener, this);
            new TpsMeter().runTaskTimer(this, 0, 1);
        } catch (LearningSetException e) {
            getLogger().severe("Could not load learning dataset. " +
                    "Make sure it's available at /plugins/CheatBuster/learningset.arff");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
    }
}

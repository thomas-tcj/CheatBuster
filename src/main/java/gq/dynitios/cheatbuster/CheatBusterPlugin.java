package gq.dynitios.cheatbuster;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import gq.dynitios.cheatbuster.listener.RecordListener;
import gq.dynitios.cheatbuster.recorder.PlayerRecorder;
import org.bukkit.plugin.java.JavaPlugin;

public class CheatBusterPlugin extends JavaPlugin {
    private PlayerRecorder recorder;

    @Override
    public void onEnable() {
        recorder = new PlayerRecorder();
        RecordListener listener = new RecordListener(recorder);
        getServer().getPluginManager().registerEvents(listener, this);
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                recorder.recordPacket(event.getPlayer());
                super.onPacketReceiving(event);
            }
        });
    }

    @Override
    public void onDisable() {
        recorder.stopRecorder();
    }
}

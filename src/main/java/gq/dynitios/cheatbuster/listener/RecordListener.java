package gq.dynitios.cheatbuster.listener;

import gq.dynitios.cheatbuster.recorder.PlayerRecorder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Listens for events which should start or append a recording.
 */
public class RecordListener implements Listener {
    private PlayerRecorder playerRecorder;

    public RecordListener(PlayerRecorder playerRecorder) {

        this.playerRecorder = playerRecorder;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            this.playerRecorder.recordLeftClick(event.getPlayer());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            playerRecorder.recordPerformHit((Player) event.getDamager());
        }
    }
}

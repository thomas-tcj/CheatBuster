package gq.dynitios.cheatbuster.message;

import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

public class MessageHelper {
    public static JSONMessage getPluginPrefix() {
        return JSONMessage.create("[CheaterBuster] ").color(ChatColor.GRAY);
    }

    public static void sendToAllWithPermission(JSONMessage message, String permission) {
        List<Player> playersToSendTo = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.hasPermission(permission)) {
                playersToSendTo.add(player);
            }
        }
        message.send(playersToSendTo.toArray(new Player[0]));
    }
}

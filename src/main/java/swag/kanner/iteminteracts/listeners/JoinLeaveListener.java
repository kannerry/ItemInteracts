package swag.kanner.iteminteracts.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    // player join message customization
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ChatColor bracketsColor = ChatColor.DARK_GRAY; // set color for brackets in join message
        // append some stuff.
        String joinTag = bracketsColor + "[" + ChatColor.GREEN + "CONNECT" + bracketsColor + "]";
        String colon = ChatColor.WHITE + ": ";
        // e.g gray brackets and a green "CONNECT" text in between.

        // now set the join message to that tag plus the player name, in a specified color.
        event.setJoinMessage(joinTag + colon + ChatColor.YELLOW + event.getPlayer().getName());
        // e.g "[JOIN]: kanner
    }

    // same here but for player leave message
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        ChatColor bracketsColor = ChatColor.DARK_GRAY;
        // append some stuff.
        String joinTag = bracketsColor + "[" + ChatColor.DARK_RED + "DISCONNECT" + bracketsColor + "]";
        String colon = ChatColor.WHITE + ": ";
        // e.g gray brackets and a red "DISCONNECT" text in between.

        // now set the join message to that tag plus the player name, in a specified color.
        event.setQuitMessage(joinTag + colon + ChatColor.YELLOW + event.getPlayer().getName());
        // e.g "[DISCONNECT]: kanner
    }

}

package swag.kanner.iteminteracts.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class InventoryInteractListener implements Listener {

    @EventHandler
    public void inventoryInteractEvent(InventoryClickEvent event) {
        //if player clicks outside the inventory
        //if (event.getInventory() == null) return;
        InventoryView iv = event.getView();
        if (iv.getTitle() == "Anvil Recipe") {
            event.setCancelled(true);
        }
    }

}

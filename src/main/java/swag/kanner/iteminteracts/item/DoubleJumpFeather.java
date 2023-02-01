package swag.kanner.iteminteracts.item;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import sun.nio.ch.Util;
import swag.kanner.iteminteracts.misc.UtilFunctions;

public class DoubleJumpFeather implements Listener {

    public void doubleJumpCheck(String displayName, ItemStack stack, Item item, Player plr){
        // if we have feather falling AND the name is "double jump", ignoring case sensitivity
        if(stack.getItemMeta().hasEnchant(Enchantment.PROTECTION_FALL)){
            if(!plr.isSneaking()){ // and we aren't sneaking
                float multi = 0.15f * stack.getEnchantmentLevel(Enchantment.PROTECTION_FALL); // set a var and
                // check the amount dropped. we only want to drop one, so
                if(stack.getAmount() > 1){ // if the drop amount is bigger than one,
                    ItemStack c = item.getItemStack(); // get the item stack
                    c.setAmount(c.getAmount() - 1); // and decrement the amount in the stack. we know it can't be one.
                    if(plr.getInventory().getItemInMainHand().getType() == Material.AIR){ // if the player has nothing in their hand,
                        plr.getInventory().setItemInMainHand(c); // we can set the item stack to be inside of the player's hand.
                    }else{ // otherwise,
                        plr.getInventory().addItem(c); // just add it to the inventory normally.
                        // this stops us from over-writing any items the player might have in their hand
                    }
                }
                // now! we play a sound,
                plr.getWorld().playSound(plr.getLocation(), Sound.BLOCK_CANDLE_EXTINGUISH, 1, 1);
                // add the "double jump"
                Vector vel = plr.getVelocity();
                vel.setY(0.1 + multi);
                plr.setVelocity(vel);
                // destroy the drop
                Location hideLocation = new Location(plr.getWorld(), 0,-1000,0);
                item.teleport(hideLocation);
                // and make some particles
                UtilFunctions.summonCircle(plr.getLocation(), 1, 3, Particle.REDSTONE, Color.SILVER);
                UtilFunctions.summonCircle(plr.getLocation(), 0, 2, Particle.REDSTONE, Color.SILVER);}
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        // get some variables.
        Player plr = event.getPlayer();
        Item item = event.getItemDrop();
        ItemStack stack = item.getItemStack();
        // check if the item drop is renamed, so default items should be ignored.
        if(stack.getItemMeta().hasDisplayName()){ // if it is,
            // set some variables
            String displayName = item.getItemStack().getItemMeta().getDisplayName(); // the renamed item.
            // if our item is a feather,
            if(stack.getType() == Material.FEATHER){
                // proc a double jump check
                doubleJumpCheck(displayName, stack, item, plr);
            }
        }

        // if you quickly switch off of an item after dropping it, there's a "ghost item" in your inventory
        plr.updateInventory(); // this removes that ghost item.

    }
}

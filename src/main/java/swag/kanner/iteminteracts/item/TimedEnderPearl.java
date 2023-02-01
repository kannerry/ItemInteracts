package swag.kanner.iteminteracts.item;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import swag.kanner.iteminteracts.ItemInteracts;
import swag.kanner.iteminteracts.misc.UtilFunctions;

public class TimedEnderPearl implements Listener {

    @EventHandler
    public void onThrowTimedPearl(ProjectileLaunchEvent event) {
        Entity projectile = event.getEntity();
        // if we threw an ender pearl
        if(projectile instanceof EnderPearl){
            // then we should get the player
            Player thrower = (Player)event.getEntity().getShooter();
            // and the item he's holding.
            ItemStack item = thrower.getInventory().getItemInMainHand();
            if(item.getItemMeta().hasDisplayName()){ // if there's a custom name for that item, e.g "5 seconds"
                // get the custom name of that item, and declare it.
                String timeString = item.getItemMeta().getDisplayName().toLowerCase();
                // we should split it on space. again, we're looking for something like "5 seconds"
                String[] toInterpret = timeString.split(" ");
                if(toInterpret[1].equals("seconds") || toInterpret[1].equals("minutes")){
                    // check if we have the curse.
                    if(item.getItemMeta().hasEnchant(Enchantment.VANISHING_CURSE)){ // if we do, check if the wait time is valid.
                        if(UtilFunctions.isNumeric(toInterpret[0])){ // make completely sure it's a valid integer
                            // set some data and then
                            thrower.setMetadata("threwTimedPearl", new FixedMetadataValue(ItemInteracts.plugin,true));
                            int time = Integer.parseInt(toInterpret[0]); // parse the int.
                            if(toInterpret[1].equals("minutes")){
                                // if it's in minutes, divide by 60
                                time = time * 60;
                            }
                            // and then throw that value into our player for storage.
                            thrower.setMetadata("timeInSeconds", new FixedMetadataValue(ItemInteracts.plugin, time));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(PlayerTeleportEvent event){
        // if an ender pearl was thrown,
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL){

            if(ItemInteracts.config.getBoolean("UsePearlUpdate")){
                // base upgrade
                UtilFunctions.summonCircle(event.getTo(), 0.5f, 1, Particle.REDSTONE, Color.PURPLE);
                UtilFunctions.summonCircle(event.getTo(), 0, 1.25f, Particle.REDSTONE, Color.PURPLE);

                // player
                event.getPlayer().getWorld().playSound(event.getPlayer(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 1);

                // world
                event.getPlayer().getWorld().playSound(event.getTo(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 1);
            }

            // check if it was a timed pearl.
            if(UtilFunctions.getEntityBooleanMetadata(event.getPlayer(), "threwTimedPearl")){
                // if it was, read some data and
                int timeInSeconds = UtilFunctions.getEntityIntegerMetadata(event.getPlayer(), "timeInSeconds", ItemInteracts.plugin);
                event.getPlayer().setMetadata("threwTimedPearl", new FixedMetadataValue(ItemInteracts.plugin, false));
                // stop the teleport.
                event.setCancelled(true);
                // after a length of time set in the timed pearl, teleport our player manually.

                // particles
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ItemInteracts.plugin, new Runnable() {
                    @Override
                    public void run() {
                        // some variables to use
                        Location tpTo = event.getTo();
                        World world = event.getPlayer().getWorld();
                        world.spawnParticle(Particle.PORTAL, tpTo, 50);
                        // sounds
                        event.getPlayer().getWorld().playSound(event.getTo(), Sound.BLOCK_PORTAL_TRIGGER, 0.1f, 1.5f);
                        event.getPlayer().getWorld().playSound(event.getPlayer(), Sound.BLOCK_PORTAL_TRIGGER, 0.05f, 1.5f);
                    }
                }, (20 * timeInSeconds) - 50);
                // teleport
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ItemInteracts.plugin, new Runnable() {
                    @Override
                    public void run() {
                        // some variables to use
                        Location tpTo = event.getTo();
                        World world = event.getPlayer().getWorld();
                        Player player = event.getPlayer();
                        // do stuff
                        player.teleport(tpTo);

                        // sounds
                        event.getPlayer().getWorld().playSound(event.getPlayer(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 1);

                    }
                }, 20 * timeInSeconds);
            }
        }
    }
}

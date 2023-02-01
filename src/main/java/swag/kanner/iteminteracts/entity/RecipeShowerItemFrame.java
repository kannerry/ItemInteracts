package swag.kanner.iteminteracts.entity;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import swag.kanner.iteminteracts.misc.AnvilRecipeInventory;

import java.util.List;

public class RecipeShowerItemFrame implements Listener {

    public LivingEntity spawnDataThing(World world){
        LivingEntity x = (LivingEntity)world.spawnEntity(new Location(world, 0,1000,0), EntityType.PIG);
        x.setSilent(true); // no sounds
        x.setCollidable(false); // no collision
        x.setInvisible(true); // cant see it
        x.setAI(false); // no movement
        x.setInvulnerable(true); // creative players can kill invunerable entities. not a bug.
        return x;
    }

    public boolean checkPassengersFor(Entity ent, String key){
        // check for the key
        List<Entity> passengers = ent.getPassengers();
        for(Entity entity : passengers){
            if(entity.getName().equalsIgnoreCase(key)){
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onLeftClick(EntityDamageByEntityEvent event){
        // get interactors
        Entity clicked = event.getEntity();
        Entity damager = event.getDamager();

        // if the entity damaged was an itemframe
        if(clicked instanceof ItemFrame){
            // check if its a recipe frame
            if(checkPassengersFor(clicked, "recipeframe")){
                // only if we are in creative mode and sneaking then we should be able to destroy it
                if(damager instanceof Player){
                    Player dPlayer = (Player)damager;
                    if(dPlayer.getGameMode() == GameMode.CREATIVE){
                        if(dPlayer.isSneaking()){
                            event.setCancelled(false);
                            return;
                        }
                    }
                }
                event.setCancelled(true); // then stop it from being destroyed
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event){
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        // if we are clicking an item frame
        if(entity instanceof ItemFrame){
            ItemFrame itemFrame = (ItemFrame)entity;
            if(checkPassengersFor(itemFrame, "recipeframe")){ // with the workaround
                if(itemFrame.getItem().getType() != Material.AIR) {
                    // and it has something in it,
                    event.setCancelled(true); // cancel the interaction
                    // and show the recipe
                    AnvilRecipeInventory ari = new AnvilRecipeInventory();
                    ItemStack itemStack = itemFrame.getItem();
                    ari.showRecipe(itemStack, player);
                }
            }
        }
    }

    @EventHandler
    public void onSpawn(HangingPlaceEvent event){
        // u get the jist
        Player spawner = event.getPlayer();
        Entity spawned = event.getEntity();
        if(spawned instanceof ItemFrame){ // if we spawn an item frame
            if(spawner.getGameMode() == GameMode.CREATIVE){ // while in creative
                ItemStack frame = spawner.getInventory().getItemInMainHand();
                if(frame.getItemMeta().getDisplayName().equalsIgnoreCase("recipe frame")){ // with the name recipe frame
                    LivingEntity x = spawnDataThing(spawned.getWorld());
                    x.setCustomName("recipeframe");
                    spawned.addPassenger(x);
                    // adda a little workaround to track that it's a recipe frame. don't want to fuck with normal item frames.
                }
            }
        }
    }

}

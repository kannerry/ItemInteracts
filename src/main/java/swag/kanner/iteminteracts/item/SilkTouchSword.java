package swag.kanner.iteminteracts.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import swag.kanner.iteminteracts.ItemInteracts;
import swag.kanner.iteminteracts.misc.UtilFunctions;

import java.util.Random;

public class SilkTouchSword implements Listener {

    public boolean isSword(ItemStack item){
        switch (item.getType()){
            case WOODEN_SWORD:
            case STONE_SWORD:
            case IRON_SWORD:
            case GOLDEN_SWORD:
            case DIAMOND_SWORD:
            case NETHERITE_SWORD:
                return true;
            default:
                return false;
        }
    }

    public ItemStack getHeadStack(String name){
        switch (name){
            case "Cow":
                return UtilFunctions.getHead("MHF_Cow");
            case "Mooshroom":
                return UtilFunctions.getHead("MHF_MushroomCow");
            case "Chicken":
                return UtilFunctions.getHead("MHF_Chicken");
            case "Sheep":
                return UtilFunctions.getHead("MHF_Sheep");
            case "Pig":
                return UtilFunctions.getHead("MHF_Pig");
            case "Spider":
                return UtilFunctions.getHead("MHF_Spider");
            case "Cave Spider":
                return UtilFunctions.getHead("MHF_CaveSpider");
            case "Enderman":
                return UtilFunctions.getHead("MHF_Enderman");
            case "Squid":
                return UtilFunctions.getHead("MHF_Squid");
            case "Skeleton":
                return new ItemStack(Material.SKELETON_SKULL);
            case "Creeper":
                return new ItemStack(Material.CREEPER_HEAD);
            case "Zombie":
                return new ItemStack(Material.ZOMBIE_HEAD);
            default:
                return null;
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){

        Entity killed = event.getEntity();
        Entity killer = event.getEntity().getKiller();

        // if a player killed the mob
        if(killer instanceof Player){
            Player killerPlayer = (Player)killer;
            ItemStack itemUsed = killerPlayer.getInventory().getItemInMainHand();
            if(isSword(itemUsed)){ // and if we kill with a sword
                if(itemUsed.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)){ // that has silk touch,
                    ItemStack headStack = getHeadStack(killed.getName()); // and the mob killed has a head that's possible to drop,
                    UtilFunctions.setItemName(headStack, killed.getName() + " Head");
                    if(headStack != null){
                        // 10% of the time, if enabled
                        if(ItemInteracts.config.getBoolean("DropHeadsRare")){
                            Random random = new Random();
                            if(random.nextInt(10) == 0){
                                killed.getWorld().dropItem(killed.getLocation(), headStack); // drop it
                            }
                        }else{ // otherwise always drop it
                            killed.getWorld().dropItem(killed.getLocation(), headStack); // drop it
                        }
                    }
                }
            }
        }
    }
}

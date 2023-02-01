package swag.kanner.iteminteracts.misc;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import swag.kanner.iteminteracts.ItemInteracts;

import java.util.ArrayList;
import java.util.List;

public class UtilFunctions {

    // make a player head with a specified name
    public static ItemStack getHead(String value) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD); // make player head
        SkullMeta skullMeta = (SkullMeta)skull.getItemMeta(); // get and
        skullMeta.setOwner(value); // ;-;
        skull.setItemMeta(skullMeta); // set the data
        return skull;
    }

    public static Location getDiffLoc(World world, Location loc1, Location loc2){

        Location l = new Location(world,0, 0, 0);

        double x1 = loc1.getX();
        double y1 = loc1.getY();
        double z1 = loc1.getZ();

        double x2 = loc2.getX();
        double y2 = loc2.getY();
        double z2 = loc2.getZ();

        double xdiff = x1 - x2;
        double ydiff = y1 - y2;
        double zdiff = z1 - z2;

        l.setX(xdiff);
        l.setY(ydiff);
        l.setZ(zdiff);

        return l;
    }

    public static void startWallhacksRunnable(){

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    ItemStack helm = player.getEquipment().getHelmet();
                    if(helm != null){
                        String dn = helm.getItemMeta().getDisplayName();
                        // IF WE HAVE A LEATHER HELMET WITH THE NAME WALLHACKS
                        if (helm.getType() == Material.LEATHER_HELMET && dn.equalsIgnoreCase("wallhacks")){
                            // WITH INFINITY, AND BINDING
                            if(helm.getItemMeta().hasEnchant(Enchantment.VANISHING_CURSE) &&
                                    helm.getItemMeta().hasEnchant(Enchantment.BINDING_CURSE)){
                                // get all players, and
                                for(Player other : Bukkit.getOnlinePlayers()){
                                    if(other != player){ // if the player we're in isn't the same player with our helmet,

                                        // get some variables, mainly the locations.
                                        Location me = player.getLocation();
                                        Location o = other.getLocation();

                                        // get the difference between our positions (the player we're in, and the player with the helmet)
                                        Location diff = UtilFunctions.getDiffLoc(me.getWorld(), me, o);

                                        // make a new location
                                        Location real = new Location(me.getWorld(), 0, 0, 0);
                                        real.setX(me.getX() - diff.getX() / 20);
                                        real.setY(me.getY() + 1.6F - diff.getY() / 20);
                                        real.setZ(me.getZ() - diff.getZ() / 20);

                                        if(player.isSneaking()){
                                            real.setY(real.getY() - 0.325f);
                                        }

                                        // if the difference between the player, and the "wallhacker" is within 10 blocks of both directions
                                        if(diff.getX() >= -10 && diff.getX() <= 10){
                                            if(diff.getZ() >= -10 && diff.getZ() <= 10){
                                                // spawn a particle
                                                me.getWorld().spawnParticle(Particle.REDSTONE, real, 25, new Particle.DustOptions(Color.AQUA, 0.1F));
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }

            }
        }.runTaskTimer(ItemInteracts.plugin, 1, 1);

    }

    public static boolean isNumeric(String string) {
        int intValue;
        if(string == null || string.equals("")) {
            System.out.println("isNumeric was called, but cannot parsed: null or empty.");
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("isNumeric was called, but cannot parsed: NumberFormatException.");
        }
        return false;
    }

    public static void summonCircle(Location location, float rad, float size, Particle particle, Color color) {
        for (int d = 0; d <= 90; d += 1) {
            Location particleLoc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            particleLoc.setX(location.getX() + Math.cos(d) * rad);
            particleLoc.setZ(location.getZ() + Math.sin(d) * rad);
            location.getWorld().spawnParticle(particle, particleLoc, 1, new Particle.DustOptions(color, size));
        }
    }

    public static int getEntityIntegerMetadata(Entity entity, String key, Plugin plugin) {
        // check if we should destroy it on bounce,
        List<MetadataValue> meta = entity.getMetadata(key);
        for (MetadataValue element : meta) {
            if(plugin == element.getOwningPlugin()){
                return element.asInt();
            }
        }
        return 0;
    }

    public static boolean getEntityBooleanMetadata(Entity entity, String key) {
        // check if we should destroy it on bounce,
        List<MetadataValue> meta = entity.getMetadata(key);
        for (MetadataValue element : meta) {
            if (element.asBoolean() == true) { // if we should,
                return true;
            }
        }
        return false;
    }

    public static void setItemName(ItemStack item, String name) {
        ItemMeta itemStackMeta = item.getItemMeta();
        itemStackMeta.setDisplayName(ChatColor.RESET + name);
        item.setItemMeta(itemStackMeta);
    }

    public static void setItemLore(ItemStack item, ArrayList<String> lores){
        ItemMeta itemStackMeta = item.getItemMeta();
        itemStackMeta.setLore(lores);
        item.setItemMeta(itemStackMeta);

    }
}

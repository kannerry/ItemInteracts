package swag.kanner.iteminteracts.item;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import swag.kanner.iteminteracts.ItemInteracts;

import java.util.ArrayList;
import java.util.List;

public class FrostWalkerSnowball implements Listener {

    public void procWater(Block block, int space) {

        // if we are on enchantment level 1, let's just do cardinal directions.
        if(space == 1){
            // set the base block to ice, then
            block.setType(Material.ICE);
            // get the cardinal directions. and
            Block[] adjblocks = {block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.EAST),
                    block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.WEST)};
            // if the blocks in those directions are water, then we should
            for (Block adj : adjblocks) {
                if (adj.getType() == Material.WATER) {
                    // set them to ice in a bit
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ItemInteracts.plugin, () -> {
                        adj.setType(Material.ICE);
                    }, 2);
                }
            }
        }

        // if we are on enchantment level 2, then we should do all of the block faces
        // so,
        if(space == 2){
            // set the base block to ice, then
            block.setType(Material.ICE);
            // get all block faces, and cardinal

            Block[] cardinal = {block.getRelative(BlockFace.NORTH), block.getRelative(BlockFace.EAST),
                    block.getRelative(BlockFace.SOUTH), block.getRelative(BlockFace.WEST)};

            List<Block> alladj = new ArrayList<>();
            for(BlockFace blockface : BlockFace.values()){
                alladj.add(block.getRelative(blockface));
            }

            // for cardinal,
            for (Block adj : cardinal) {
                if (adj.getType() == Material.WATER) {
                    // set them to ice in a bit
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ItemInteracts.plugin, () -> {
                        adj.setType(Material.ICE);
                    }, 2);
                }
            }

            // then all adj
            for (Block adj : alladj){
                if (adj.getType() == Material.WATER) {
                    // set them to ice in a bit
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ItemInteracts.plugin, () -> {
                        adj.setType(Material.ICE);
                    }, 5);
                }
            }
        }

    }

    // "normalize" the location to its coresponding blockXYZ
    public Location getBlockLocation(Location loc) {
        Location blockLoc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        return blockLoc;
    }

    @EventHandler
    public void onThrowSnowball(ProjectileLaunchEvent event) {

        // variable so we dont spam check inside of the ScheduleSyncRepeatingTask
        final boolean[] procIce = {false};

        Entity thrownEntity = event.getEntity();
        if (thrownEntity.getType() == EntityType.SNOWBALL) {
            // we are a snowball
            Snowball snowball = (Snowball) thrownEntity;
            ItemStack sbItem = snowball.getItem();
            // if the snowball has aqua affinity
            if (sbItem.getItemMeta().hasEnchant(Enchantment.FROST_WALKER)) {
                int ench = sbItem.getItemMeta().getEnchantLevel(Enchantment.FROST_WALKER);
                // slow the snowball down
                Vector newv = snowball.getVelocity();
                newv.setX(newv.getX() / 2);
                newv.setY(newv.getY() / 2);
                newv.setZ(newv.getZ() / 2);
                snowball.setVelocity(newv);
                // and start checking for water
                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(ItemInteracts.plugin, () -> {
                    if (procIce[0] == false) { // if we aren't bounced,
                        Location sbLoc = snowball.getLocation(); // get location
                        World world = snowball.getWorld(); // and world
                        // if the block at the snowballs position is water
                        if (snowball.getWorld().getBlockAt(getBlockLocation(sbLoc)).getType() == Material.WATER) {
                            // set variables,
                            Block block = world.getBlockAt(getBlockLocation(sbLoc));
                            Location particleLoc = snowball.getLocation();
                            particleLoc.setY(particleLoc.getY() + 0.5);
                            // play some sounds
                            world.playSound(snowball.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 0.25F, 1);
                            world.playSound(snowball.getLocation(), Sound.AMBIENT_UNDERWATER_ENTER, 0.25F, 1);
                            // add a particle
                            world.spawnParticle(Particle.SPELL_INSTANT, particleLoc, 10);
                            // destroy the snowball and,
                            snowball.teleport(new Location(snowball.getWorld(), 0, -1000, 0));
                            // start setting ice
                            procWater(block, ench);
                            procIce[0] = true; // bounce
                        }else{ // otherwise, if we hit a block and it's not water
                            if(world.getBlockAt(getBlockLocation(sbLoc)).getType() != Material.AIR){
                                // make sure we aren't checking if it's water.
                                snowball.teleport(new Location(snowball.getWorld(), 0, -1000, 0)); // this is to destroy the snowball
                                procIce[0] = true; // and this is to break the loop
                            }
                        }
                    }
                }, 0, 1);
            }
        }
    }
}

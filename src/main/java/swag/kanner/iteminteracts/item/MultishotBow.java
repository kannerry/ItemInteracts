package swag.kanner.iteminteracts.item;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import swag.kanner.iteminteracts.ItemInteracts;
import swag.kanner.iteminteracts.misc.UtilFunctions;

import java.util.List;

public class MultishotBow implements Listener {

    // multishot logic
    public void shootMultishot(Location initLoc, Vector initVel, Entity shooter, ItemStack bow) {
        // spawn an arrow
        Entity multi = shooter.getWorld().spawnEntity(initLoc, EntityType.ARROW);
        // with our original arrow's velocity,
        multi.setVelocity(initVel);
        // make it a "multishot" with the metadata. this allows it to hit players regardless of the invunerability timer.
        multi.setMetadata("multiShot", new FixedMetadataValue(ItemInteracts.plugin, true));
        // now play a sound for our arrow shot
        shooter.getWorld().playSound(shooter, Sound.ENTITY_ARROW_SHOOT, 1, 1);
        // if it's a fire bow, set our arrow alight
        if (bow.getItemMeta().hasEnchant(Enchantment.ARROW_FIRE)) {
            multi.setVisualFire(true);
        }
    }

    public void procMultishot(LivingEntity shooter, ItemStack bow, Entity arrow) {
        // BLEH cringe CRITICAL PARTICLE... Goodbye!
        AbstractArrow critVar = (AbstractArrow) arrow;
        critVar.setCritical(false);
        // set some variables,
        arrow.setMetadata("multiShot", new FixedMetadataValue(ItemInteracts.plugin, true));
        // and lower the velocity a tiny amount.
        Vector vel = arrow.getVelocity();
        vel.setX(arrow.getVelocity().getX() / 1.25);
        vel.setY(arrow.getVelocity().getY() / 1.25);
        vel.setZ(arrow.getVelocity().getZ() / 1.25);
        arrow.setVelocity(vel);
        // now store the initial location and velocity for use.
        Location initLoc = arrow.getLocation();
        Vector initVel = arrow.getVelocity();
        for (int i = 1; i < 3; i++) { // run this code 3 times. multi-shot right? like the crossbow
            int finalscale = i; // the fuck is this for man? java is so weird
            // ok so we make a new runnable, and run it 2i ticks later
            new BukkitRunnable() {
                @Override
                public void run() { // heres code we should run
                    // set and make some variables,
                    Player plr = (Player) shooter;
                    ItemStack arrowStack = new ItemStack(Material.ARROW);
                    Vector msVel = initVel;
                    msVel.setY(initVel.getY() - 0.05 * finalscale);
                    // if we are not in creative there's some inventory logic,
                    if (plr.getGameMode() != GameMode.CREATIVE) {
                        // oh we have infinity enchant nice we can override the inventory shit
                        if (bow.getItemMeta().hasEnchant(Enchantment.ARROW_INFINITE)) {
                            shootMultishot(initLoc, msVel, shooter, bow);
                        } else {
                            // ok here we go. // if we have at LEAST 1 arrow, we can shoot that arrow,
                            if (plr.getInventory().containsAtLeast(arrowStack, 1)) {
                                // lets look for it. loop for all items in our inventory,
                                for (ItemStack i : plr.getInventory().getContents()) {
                                    if (i.getType() == Material.ARROW) { // to find that arrow.
                                        i.setAmount(i.getAmount() - 1); // now we can shoot. remove one arrow from the stack
                                        shootMultishot(initLoc, msVel, shooter, bow);
                                    }
                                }
                            }
                        }
                    } else { // otherwise woo hoo !! no need to fuck with the inventory. shoot away
                        shootMultishot(initLoc, msVel, shooter, bow);
                    }
                }
            }.runTaskLater(ItemInteracts.plugin, 2 * i /*<-- the "ticks later", 2i */);
        }
    }

    @EventHandler // on bow shoot,
    public void onShootBow(EntityShootBowEvent event) {
        LivingEntity shooter = event.getEntity();
        // check if we are a player,
        if (shooter instanceof Player) {
            // if we are, set some variables and
            ItemStack bow = event.getBow();
            Entity arrow = event.getProjectile();
            // IF WE ARE ON A DEFAULT BOW, NOT A CROSSBOW, THEN
            if(bow.getType() == Material.BOW){
                // check if we have the multi shot enchant,
                if (bow.getItemMeta().hasEnchant(Enchantment.MULTISHOT)) {
                    // if we do, proc the multishot thing
                    procMultishot(shooter, bow, arrow);
                }
            }

        }
    }

    @EventHandler // on projectile hit,
    public void projectileHit(ProjectileHitEvent event) {
        // check if its a multi shot,
        if(UtilFunctions.getEntityBooleanMetadata(event.getEntity(), "multiShot")){
            Entity e = event.getHitEntity(); // get the entity it supposedly hit.
            if (e != null) { // if we did actually hit an entity, and not a block
                if (e instanceof LivingEntity) { // make sure it's cool.
                    LivingEntity livingHit = (LivingEntity) e; // here's the cool guy (he has the setNoDamageTicks function)
                    livingHit.setNoDamageTicks(0); // now, get rid of that entity's invulnerability frames.
                    // this makes sure all of the arrows will hit.
                }
            }
        }
    }

}
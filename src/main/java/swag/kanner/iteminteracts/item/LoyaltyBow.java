package swag.kanner.iteminteracts.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;
import swag.kanner.iteminteracts.ItemInteracts;
import swag.kanner.iteminteracts.misc.UtilFunctions;

import java.util.List;

public class LoyaltyBow implements Listener {

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
                // we can check if we have the loyalty enchant.
                if (bow.getItemMeta().hasEnchant(Enchantment.LOYALTY)) {
                    // wait, this fixes a desync. the positions while sitting on the arrow differ (client vs server) when bouncing off of an entity.
                    arrow.setMetadata("destroyOnBounce", new FixedMetadataValue(ItemInteracts.plugin, true));
                    // ok. now,
                    // we sit on the arrow, and
                    arrow.addPassenger(shooter);
                    // then reduce the velocity of that arrow by half
                    Vector vel = arrow.getVelocity();
                    vel.setX(arrow.getVelocity().getX() / 2);
                    vel.setY(arrow.getVelocity().getY() / 2);
                    vel.setZ(arrow.getVelocity().getZ() / 2);
                    arrow.setVelocity(vel);
                }
            }

        }
    }

    @EventHandler // on projectile hit,
    public void projectileHit(ProjectileHitEvent event) {
        if(UtilFunctions.getEntityBooleanMetadata(event.getEntity(), "destroyOnBounce")){
            event.getEntity().remove();
        }
    }
}
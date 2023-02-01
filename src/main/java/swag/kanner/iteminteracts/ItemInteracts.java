package swag.kanner.iteminteracts;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import sun.nio.ch.Util;
import swag.kanner.iteminteracts.entity.RecipeShowerItemFrame;
import swag.kanner.iteminteracts.item.*;
import swag.kanner.iteminteracts.listeners.InventoryInteractListener;
import swag.kanner.iteminteracts.listeners.JoinLeaveListener;
import swag.kanner.iteminteracts.misc.UtilFunctions;

import java.sql.Time;

public final class ItemInteracts extends JavaPlugin {

    // declare the plugin so that we can access it elsewhere, e.g in procMultishot, ShootBowListener.
    public static JavaPlugin plugin;
    // get ya config
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        System.out.println("[ItemInteracts] onEnable fired");
        plugin = this; // set plugin for use elsewhere
        config = getConfig(); // set config for use elsewhere
        // now setup config
        config.addDefault("UsePearlUpdate", true);
        config.addDefault("DropHeadsRare", true);
        config.addDefault("JoinLeaveMessages", false);
        config.addDefault("UseWallhackParticle", true);
        config.options().copyDefaults(true);
        saveConfig();
        // register listeners
        if (config.getBoolean("JoinLeaveMessages")) {
            registerEvent(new JoinLeaveListener(), this);
        }
        registerEvent(new InventoryInteractListener(), this);
        // items
        registerEvent(new DoubleJumpFeather(), this);
        registerEvent(new FrostWalkerSnowball(), this);
        registerEvent(new TimedEnderPearl(), this);
        // combat items
        registerEvent(new LoyaltyBow(), this);
        registerEvent(new MultishotBow(), this);
        registerEvent(new SilkTouchSword(), this);
        // blocks
        //
        // entity
        registerEvent(new RecipeShowerItemFrame(), this);
        // wallhacks
        if(config.getBoolean("UseWallhackParticle")){
            UtilFunctions.startWallhacksRunnable();
        }
    }

    @Override
    public void onDisable() {
        System.out.println("[ItemInteracts] onDisable fired");
    }
    // shortens the lines up above in onEnable for ａｅｓｔｈｅｔｉｃｓ
    // looks nicer basically
    public void registerEvent(Listener event, JavaPlugin plugin){
        getServer().getPluginManager().registerEvents(event, plugin);
    }
}

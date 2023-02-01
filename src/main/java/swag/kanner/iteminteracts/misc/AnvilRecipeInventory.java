package swag.kanner.iteminteracts.misc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.UUID;

public class AnvilRecipeInventory {

    // create inventory :P
    public Inventory createInventory() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, "Anvil Recipe");
        return inv;
    }

    public void setForRecipe(ItemStack itemStack, Inventory inventory){
        // setup some variables
        ChatColor descColor = ChatColor.AQUA;
        ItemMeta meta = itemStack.getItemMeta();
        String recipe = meta.getDisplayName();
        // make a new item with the same material of the item we are trying to "craft"
        ItemStack item = new ItemStack(itemStack.getType());
        // get a head
        ItemStack plus = UtilFunctions.getHead("MHF_ArrowRight");
        UtilFunctions.setItemName(plus, "With ->");
        // get an enchanting book
        ItemStack enchant = new ItemStack(Material.ENCHANTED_BOOK);
        // makes enough sense right
        if(recipe.equals("Beheading Sword")){
            // extra lore for enchantment, e.g "eny enchantment level will work :P"
            ArrayList<String> strings = new ArrayList<>(); strings.add(descColor + "[ drop heads 10% of the time ]");
            ItemStack x = new ItemStack(Material.ANVIL); UtilFunctions.setItemLore(x, strings);
            // add the enchantment to the book, and add some items
            enchant.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1); // add the enchant
            inventory.setItem(4, x); // anvil
            inventory.setItem(11, item); // sword
            inventory.setItem(13, plus); // head
            inventory.setItem(15, enchant); // silk touch
        }

        if(recipe.equals("Travel Bow")){
            // extra lore for enchantment, e.g "eny enchantment level will work :P"
            ArrayList<String> strings = new ArrayList<>(); strings.add(descColor + "[ travel with your arrow! ]");
            strings.add(descColor + "[ any enchantment level will work ]");
            ItemStack x = new ItemStack(Material.ANVIL); UtilFunctions.setItemLore(x, strings);
            // add the enchantment to the book, and add some items
            enchant.addUnsafeEnchantment(Enchantment.LOYALTY, 1);
            inventory.setItem(4, x); // anvil
            inventory.setItem(11, item); // bow
            inventory.setItem(13, plus); // head
            inventory.setItem(15, enchant); // loyalty
        }

        if(recipe.equals("Multishot Bow")){
            // add the enchantment to the book, and add some items
            enchant.addUnsafeEnchantment(Enchantment.MULTISHOT, 1);
            ArrayList<String> strings = new ArrayList<>(); strings.add(descColor + "[ bow that shoots multiple times! ]");
            ItemStack x = new ItemStack(Material.ANVIL); UtilFunctions.setItemLore(x, strings);
            inventory.setItem(4, x); // anvil
            inventory.setItem(11, item); // bow
            inventory.setItem(13, plus); // head
            inventory.setItem(15, enchant); // multishot
        }

        if(recipe.equals("Double Jump")){
            // extra lore for enchantment, e.g "eny enchantment level will work :P"
            ArrayList<String> strings = new ArrayList<>(); strings.add(descColor + "[ drop to use! ]");
            strings.add(descColor + "[ jump height scales with enchantment level ]"); strings.add(descColor + "[ can 'real drop' while sneaking ]");
            ItemStack x = new ItemStack(Material.ANVIL); UtilFunctions.setItemLore(x, strings);
            // add the enchantment to the book, and add some items
            enchant.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);

            // name tag with name
            ItemStack nt = new ItemStack(Material.NAME_TAG);
            // this
            UtilFunctions.setItemName(nt, "Requires Name!");
            // with description
            ArrayList<String> loreList = new ArrayList<>(); loreList.add(ChatColor.RED + "'Double Jump'");
            UtilFunctions.setItemLore(nt, loreList);

            inventory.setItem(2, nt); // requires name

            inventory.setItem(4, x); // anvil
            inventory.setItem(11, item); // feather
            inventory.setItem(13, plus); // head
            inventory.setItem(15, enchant); // feather falling
        }

        if(recipe.equals("Freeze Ball")){
            // extra lore for enchantment, e.g "eny enchantment level will work :P"
            ArrayList<String> strings = new ArrayList<>(); strings.add(descColor + "[ when thrown on water, will freeze the area! ]");
            strings.add(descColor + "[ ice radius goes up with enchantment level ]");
            ItemStack x = new ItemStack(Material.ANVIL); UtilFunctions.setItemLore(x, strings);
            // add the enchantment to the book, and add some items
            enchant.addUnsafeEnchantment(Enchantment.FROST_WALKER, 1);
            inventory.setItem(4, x); // anvil
            inventory.setItem(11, item); // snowball
            inventory.setItem(13, plus); // head
            inventory.setItem(15, enchant); // frost walker
        }

        if(recipe.equals("Timed Ender Pearls")){
            // extra lore for enchantment, e.g "eny enchantment level will work :P"
            ArrayList<String> strings = new ArrayList<>(); strings.add(descColor + "[ delayed ender pearls! ]");
            ItemStack x = new ItemStack(Material.ANVIL); UtilFunctions.setItemLore(x, strings);
            // add the enchantment to the book, and add some items
            enchant.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);

            // name tag with name
            ItemStack nt = new ItemStack(Material.NAME_TAG);
            // this
            UtilFunctions.setItemName(nt, "Requires Name!");
            // with description
            ArrayList<String> loreList = new ArrayList<>(); loreList.add(ChatColor.RED + "name must follow this format:");
            loreList.add(ChatColor.GRAY + "{number} + <seconds / minutes>"); loreList.add(ChatColor.DARK_GRAY + "// e.g '5 seconds', '2 minutes' //");
            // this
            UtilFunctions.setItemLore(nt, loreList);

            inventory.setItem(2, nt); // requires name

            inventory.setItem(4, x); // anvil
            inventory.setItem(11, item); // snowball
            inventory.setItem(13, plus); // head
            inventory.setItem(15, enchant); // frost walker
        }

        if(recipe.equals("Wallhacks")){

            // extra lore for enchantment, e.g "eny enchantment level will work :P"
            ArrayList<String> strings = new ArrayList<>(); strings.add(descColor + "[ indicates where players are, within 10 blocks ]");

            // add the enchantment to the book, and add some items
            enchant.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);

            // extra enchant
            ItemStack enchant2 = new ItemStack(Material.ENCHANTED_BOOK);
            enchant2.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);

            // name tag
            ItemStack nt = new ItemStack(Material.NAME_TAG);
            // with name
            UtilFunctions.setItemName(nt, "Requires Name!");
            // with description
            ArrayList<String> loreList = new ArrayList<>(); loreList.add(ChatColor.RED + "'Wallhacks'");
            UtilFunctions.setItemLore(nt, loreList);
            
            // set enchant lore
            ItemStack x = new ItemStack(Material.ANVIL); UtilFunctions.setItemLore(x, strings);

            // nametag
            inventory.setItem(2, nt);

            inventory.setItem(4, x); // anvil
            inventory.setItem(11, item); // leather hat
            inventory.setItem(13, plus); // head
            inventory.setItem(15, enchant); // vanishing
            inventory.setItem(16, enchant2); // binding
        }

    }

    public void showRecipe(ItemStack itemStack, Player player) { // show an inventory to the given player
        Inventory myInv = createInventory(); // create a new inv, for thi splayer
        setForRecipe(itemStack, myInv); // call your method that change all items
        player.openInventory(myInv); // final inventory
    }

}

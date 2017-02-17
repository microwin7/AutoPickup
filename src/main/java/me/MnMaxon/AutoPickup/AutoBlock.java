package me.MnMaxon.AutoPickup;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by MnMaxon on 5/26/2015.
 */
public class AutoBlock {
    public static HashMap<Material, Material> convertTo = new HashMap<>();
    public static HashMap<Material, Integer> convertNum = new HashMap<>();
    private static HashMap<Material, Short> convertDurability = new HashMap<>();

    public static HashMap<Integer, ItemStack> addItem(Player p, ItemStack is) {
<<<<<<< HEAD
        if (!isSpaceAvailable(p, is)
            || is == null) {
            return new HashMap<>();
        }
        
        Inventory pInv = p.getInventory();
        Inventory inv = Bukkit.createInventory(p, InventoryType.PLAYER);

        inv.setContents(pInv.getContents());

        HashMap<Integer, ItemStack> remaining = AutoPickupPlugin.giveItem(p, inv, is);

        if (!convertTo.containsKey(is.getType()))
        {
            return remaining;
        }

        if (remaining.size() == 1 && remaining.values().toArray()[0].equals(is))
        {
             return remaining;
        }
        
        ItemStack[] newCont = block(p, inv.getContents(), is.getType());
        if (newCont != null) 
        {
            pInv.setContents(newCont);
        } else 
        {
            pInv.setContents(inv.getContents());
        }
=======
        if (is == null) return new HashMap<>();
        Inventory pInv = p.getInventory();
        Inventory inv = Bukkit.createInventory(p, 36);
        inv.setStorageContents(pInv.getStorageContents());
        HashMap<Integer, ItemStack> remaining = AutoPickupPlugin.giveItem(p, inv, is);
        if (!convertTo.containsKey(is.getType())) {
            pInv.setStorageContents(inv.getStorageContents());
            p.updateInventory();
            return remaining;
        }
        if (remaining.size() == 1 && remaining.values().toArray()[0].equals(is)) return remaining;
        ItemStack[] newCont = block(p, inv.getStorageContents(), is.getType());
        if (newCont != null) pInv.setStorageContents(newCont);
        else pInv.setStorageContents(inv.getStorageContents());
>>>>>>> f3e4793ed3b5c38fafe54dfde686df56543361cd
        p.updateInventory();
        return remaining;
    }

    public static void block(Player p) {
        ItemStack[] newConts = block(p, p.getInventory().getStorageContents(), null);
        if (newConts == null) p.sendMessage(Message.ERROR0BLOCKED_INVENTORY + "");
        else {
            p.getInventory().setStorageContents(newConts);
            p.updateInventory();
            p.sendMessage(Message.SUCCESS0BLOCKED_INVENTORY + "");
        }
    }

    private static ItemStack[] block(Player p, ItemStack[] conts, Material forceType) {
        boolean totalChanged = false;
        boolean changed = true;
        while (changed) {
            changed = false;
            //loop through the inventory looking for items we can make blocks from
            for (ItemStack is : conts)
            {
                if (is != null
                    && convertTo.containsKey(is.getType()) 
                    && (forceType == null || forceType == is.getType())
                    && (!is.hasItemMeta() || !is.getItemMeta().hasDisplayName())
                    && (!convertDurability.containsKey(is.getType()) || is.getDurability() == convertDurability.get(is.getType())))
                {
                    //we found one, so look to see how many we have
                    Material type = is.getType();
                    int num = 0;
                    int required = convertNum.get(type);
                    for (ItemStack numIS : conts)
                    {
                        if (numIS != null
                            && numIS.getType() == type
                            && (!numIS.hasItemMeta() || !numIS.getItemMeta().hasDisplayName())
                            && (!convertDurability.containsKey(type) || numIS.getDurability() == convertDurability.get(type)))
                        {
                            //count how many items we have
                            num += numIS.getAmount();
                        }
                    }

                    //for some reason lapis needs +1
                    if (num <= required && !type.equals(Material.INK_SACK)
                        || num <= required + 1 && type.equals(Material.INK_SACK))

                    {
                        continue;
                    }

                    Material convertTo = AutoBlock.convertTo.get(type);
                    changed = true;
                    totalChanged = true;
                    int toMake = num / required;
                    int tobeUsed = toMake * required;
                    for (int i = 0; i < conts.length; i++)
                    {
                        if (conts[i] != null
                            && conts[i].getType() == type
                            && (!conts[i].hasItemMeta() || !conts[i].getItemMeta().hasDisplayName())
                            && (!convertDurability.containsKey(type) || conts[i].getDurability() == convertDurability.get(type)))
                        {
                            if (conts[i].getAmount() > tobeUsed) {
                                conts[i].setAmount(conts[i].getAmount() - tobeUsed);
                                break;
                            } else {
                                tobeUsed -= conts[i].getAmount();
                                conts[i] = null;
                            }
                        }
                    }

<<<<<<< HEAD
                    Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER);
                    ItemStack toAdd = new ItemStack(convertTo);

                    if (isSpaceAvailable(p, toAdd))
                    {
                        inv.setContents(conts);
                        toAdd.setAmount(type.getMaxStackSize());
                        while (toMake > convertTo.getMaxStackSize()) {
                            AutoPickupPlugin.giveItem(p, inv, toAdd);
                            toMake -= type.getMaxStackSize();
                        }
                        toAdd.setAmount(toMake);
=======
                    Inventory inv = Bukkit.createInventory(null, 36);
                    ItemStack toAdd = new ItemStack(convertTo);
                    inv.setStorageContents(conts);
                    toAdd.setAmount(type.getMaxStackSize());
                    while (toMake > convertTo.getMaxStackSize()) {
>>>>>>> f3e4793ed3b5c38fafe54dfde686df56543361cd
                        AutoPickupPlugin.giveItem(p, inv, toAdd);
                        toMake -= type.getMaxStackSize();
                    }
                    toAdd.setAmount(toMake);
                    AutoPickupPlugin.giveItem(p, inv, toAdd);
                    conts = inv.getStorageContents();
                }
            }
        }
        if (totalChanged) return conts;
        return null;
    }

    static {
        convertTo.put(Material.CLAY_BALL, Material.CLAY);
        convertNum.put(Material.CLAY_BALL, 4);
        convertTo.put(Material.IRON_INGOT, Material.IRON_BLOCK);
        convertNum.put(Material.IRON_INGOT, 9);
        convertTo.put(Material.REDSTONE, Material.REDSTONE_BLOCK);
        convertNum.put(Material.REDSTONE, 9);
        convertTo.put(Material.DIAMOND, Material.DIAMOND_BLOCK);
        convertNum.put(Material.DIAMOND, 9);

        convertTo.put(Material.INK_SACK, Material.LAPIS_BLOCK);
        convertNum.put(Material.INK_SACK, 9);
        convertDurability.put(Material.INK_SACK, (short) 4);

        convertTo.put(Material.COAL, Material.COAL_BLOCK);
        convertNum.put(Material.COAL, 9);
        convertDurability.put(Material.COAL, (short) 0);

        convertTo.put(Material.EMERALD, Material.EMERALD_BLOCK);
        convertNum.put(Material.EMERALD, 9);
        convertTo.put(Material.GOLD_INGOT, Material.GOLD_BLOCK);
        convertNum.put(Material.GOLD_INGOT, 9);
    }

    static boolean isSpaceAvailable(Player player, ItemStack item) {
        //Exclude armor slots - ids 100, 101, 102, 103 - Normal Inventory is slots 0-35
        boolean space = false;
        for (int i = 0; i <= 35; i++) {
            ItemStack slotItem = player.getInventory().getItem(i);
            if (slotItem == null || ((slotItem.getType() == item.getType()) && item.getAmount() + slotItem.getAmount() <= slotItem.getMaxStackSize())) {
                space = true;
            }
        }
        return space;
    }
}

package me.darkolythe.shulkercleaner;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShulkerCleaner extends JavaPlugin implements CommandExecutor {

    final String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "ShulkerCleaner" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    @Override
    public void onEnable() {
        System.out.println(prefix + ChatColor.GREEN + "ShulkerCleaner has been enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println(prefix + ChatColor.RED + "ShulkerCleaner has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("shulkercleaner.clean")) {
                if (cmd.getName().equalsIgnoreCase("shulkercleaner")) {
                    if (args.length > 0 && args[0].equalsIgnoreCase("all")) {
                        int count = 0;
                        for (int i = 0; i < player.getInventory().getContents().length; i++) {
                            ItemStack item = player.getInventory().getContents()[i];
                            if (item != null && item.hasItemMeta()) {
                                if (item.getItemMeta() instanceof BlockStateMeta) {
                                    BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                                    if (meta.getBlockState() instanceof ShulkerBox) {
                                        ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                                        if (shulker.getInventory().getContents().length == 0 || isAllAir(shulker)) {
                                            player.getInventory().setItem(i, new ItemStack(item.getType(), item.getAmount()));
                                            count++;
                                        }
                                    }
                                }
                            }
                        }
                        player.sendMessage(prefix + ChatColor.WHITE + getConfig().get("successall").toString().replace("%count%", Integer.toString(count)));
                    } else if (args.length > 0 && args[0].equalsIgnoreCase("override")) {
                        int count = 0;
                        for (int i = 0; i < player.getInventory().getContents().length; i++) {
                            ItemStack item = player.getInventory().getContents()[i];
                            if (item != null && item.hasItemMeta()) {
                                if (item.getItemMeta() instanceof BlockStateMeta) {
                                    BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                                    if (meta.getBlockState() instanceof ShulkerBox) {
                                        player.getInventory().setItem(i, new ItemStack(item.getType(), item.getAmount()));
                                        count++;
                                    }
                                }
                            }
                        }
                        player.sendMessage(prefix + ChatColor.WHITE + getConfig().get("successoverride").toString().replace("%count%", Integer.toString(count)));
                    } else {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (item.hasItemMeta()) {
                            if (item.getItemMeta() instanceof BlockStateMeta) {
                                BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                                if (meta.getBlockState() instanceof ShulkerBox) {
                                    ShulkerBox shulker = (ShulkerBox) meta.getBlockState();
                                    if (shulker.getInventory().getContents().length == 0 || isAllAir(shulker)) {
                                        player.getInventory().setItemInMainHand(new ItemStack(item.getType(), item.getAmount()));
                                        player.sendMessage(prefix + ChatColor.WHITE + getConfig().get("successclean"));
                                        return true;
                                    }
                                }
                            }
                        }
                        player.sendMessage(prefix + ChatColor.WHITE + getConfig().get("failclean"));
                    }
                }
            }
        }
        return true;
    }

    private boolean isAllAir(ShulkerBox shulker) {
        for (ItemStack item : shulker.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }
}
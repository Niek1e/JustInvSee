package me.Niek1e.JustInvSee;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class JustInvSee extends JavaPlugin implements Listener {

	/*
	 * JustInvSee v8.0, a Bukkit plugin to view a player's inventory
	 * Copyright (C) 2020 Niek1e
	 * 
	 * This program is free software; you can redistribute it and/or modify it under
	 * the terms of the GNU General Public License as published by the Free Software
	 * Foundation; either version 2 of the License, or (at your option) any later
	 * version.
	 * 
	 * This program is distributed in the hope that it will be useful, but WITHOUT
	 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
	 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
	 * details.
	 * 
	 * You should have received a copy of the GNU General Public License along with
	 * this program; if not, write to the Free Software Foundation, Inc., 51
	 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
	 * 
	 */

	public void onEnable() {
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, (Plugin) this);
	}

	public void onDisable() {
		saveDefaultConfig();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getView().getTitle().equals("Armor - JustInvSee (C)")) {
			e.setResult(Result.DENY);
			return;
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if (e.getView().getTitle().equals("Armor - JustInvSee (C)")) {
			e.setResult(Result.DENY);
			return;
		}
	}

	private boolean isValidPlayer(CommandSender sender, Command command, String[] args) {
		if (!(sender instanceof Player)) {
			Message exception = new Message(this, "onlyingame");
			exception.doSend(sender);
			return false;
		}
		if (!sender.hasPermission("justinvsee." + command.getName())) {
			Message exception = new Message(this, "needop");
			exception.doSend(sender);
			return false;
		}
		if (args.length != 1) {
			Message exception = new Message(this,
					command.getName().substring(0, command.getName().length() - 3) + "usage");
			exception.doSend(sender);
			return false;
		}
		if (getTargetPlayer(sender, args[0]) == null) {
			Message exception = new Message(this, "playernotonline");
			exception.doSend(sender);
			return false;
		}
		return true;
	}

	private boolean isValidCommand(CommandSender sender, Command command, String[] args) {

		if (!sender.hasPermission("justinvsee.changeconfig")) {
			Message exception = new Message(this, "needop");
			exception.doSend(sender);
			return false;
		}

		if (args.length != 1) {
			Message exception = new Message(this, "justinvseeusage");
			exception.doSend(sender);
			return false;
		}

		return true;

	}

	private Player getTargetPlayer(CommandSender sender, String name) {
		Player targetPlayer = sender.getServer().getPlayer(name);
		return targetPlayer;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("justinvsee")) {
			if (!isValidCommand(sender, command, args)) {
				return true;
			}

			if (args[0].equalsIgnoreCase("togglemsg")) {
				boolean setValue = toggleMessages();
				Message exception = new Message(this, "messagetoggled", setValue);
				exception.doSend(sender);
				return true;
			} else {
				Message exception = new Message(this, "justinvseeusage");
				exception.doSend(sender);
				return true;
			}
		}

		if (!isValidPlayer(sender, command, args))
			return true;
		Player player = (Player) sender;
		Player targetPlayer = getTargetPlayer(sender, args[0]);

		if (targetPlayer.hasPermission("justinvsee." + label) && getMessageStatus()) {
			Message notification = new Message(this, "lookedinyourinv", targetPlayer);
			notification.doSend(targetPlayer);
		}

		if (command.getName().equalsIgnoreCase("inv")) {
			PlayerInventory playerInventory = targetPlayer.getInventory();
			player.openInventory((Inventory) playerInventory);
			player.playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.MOBSPAWNER_FLAMES, null);
			return true;
		}
		if (command.getName().equalsIgnoreCase("enderinv")) {
			Inventory targetInventory = targetPlayer.getEnderChest();
			player.openInventory(targetInventory);
			return true;
		}
		if (command.getName().equalsIgnoreCase("armorinv")) {
			player.openInventory(getArmorInventory(targetPlayer));
			return true;
		}
		return false;
	}

	private boolean toggleMessages() {
		boolean value = getMessageStatus();
		getConfig().set("opmessage", Boolean.valueOf(!value));
		saveConfig();
		return !value;
	}

	private boolean getMessageStatus() {
		return getConfig().getBoolean("opmessage");
	}

	private ItemStack getGlassPane(String name) {
		ItemStack i = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		i.setItemMeta(im);
		return i;
	}

	private Inventory getArmorInventory(Player targetPlayer) {
		Inventory armorInventory = Bukkit.getServer().createInventory(null, 9, "Armor - JustInvSee (C)");
		ItemStack overig = getGlassPane(ChatColor.RED + "[]");
		armorInventory.setItem(0, overig);
		armorInventory.setItem(1, targetPlayer.getEquipment().getHelmet());
		armorInventory.setItem(2, targetPlayer.getEquipment().getChestplate());
		armorInventory.setItem(3, targetPlayer.getEquipment().getLeggings());
		armorInventory.setItem(4, targetPlayer.getEquipment().getBoots());
		armorInventory.setItem(5, overig);
		armorInventory.setItem(6, overig);
		armorInventory.setItem(7, targetPlayer.getEquipment().getItemInOffHand());
		armorInventory.setItem(8, overig);
		return armorInventory;
	}

}

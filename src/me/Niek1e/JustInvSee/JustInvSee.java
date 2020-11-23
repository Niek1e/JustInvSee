package me.Niek1e.JustInvSee;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.Niek1e.JustInvSee.JustInventory.Type;

public class JustInvSee extends JavaPlugin implements Listener {

	/*
	 * JustInvSee v8.0, a Bukkit plugin to view a player's inventory Copyright (C)
	 * 2020 Niek1e
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

	private JustInventoryManager justInventoryManager;
	private EffectsManager effectsManager;

	public void onEnable() {
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, (Plugin) this);
		justInventoryManager = new JustInventoryManager();
		effectsManager = new EffectsManager();
	}

	public void onDisable() {
		justInventoryManager.closeAll();
		saveDefaultConfig();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		JustInventory justInventory = justInventoryManager.getJustInventory(e.getView());

		if (justInventory == null || e.getInventory() == null) {
			return;
		}

		if (justInventory.getJustInventoryType().equals(Type.PLAYER_INVENTORY)) {

			Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {
				public void run() {
					justInventory.getInventoryOwner().updateInventory();
				}
			}, 2L);

		}

		if (justInventory.getInventoryView().getTitle().equals("Armor - JustInvSee (C)")) {
			

			if (e.getCurrentItem() == null || !e.getClick().equals(ClickType.LEFT)) {
				e.setResult(Result.DENY);
				return;
			}

			if (!e.getCurrentItem().isSimilar(justInventory.getRemoveStack())) {
				e.setResult(Result.DENY);
				return;
			}

			if (!e.getClickedInventory().equals(justInventory.getInventory())) {
				e.setResult(Result.DENY);
				return;
			}
			
			removeItem(justInventory, e.getSlot() - 9);
			e.getWhoClicked().closeInventory();	
			JustInventory armorInventory = new JustInventory(Type.ARMOR_INVENTORY, justInventory.getInventoryOwner(),
					justInventoryManager);
			armorInventory.openInventory((Player) e.getWhoClicked(), effectsManager);
			
			e.setResult(Result.DENY);
		}
	}

	private void removeItem(JustInventory justInventory, int actualSlot) {

		Inventory inventory = justInventory.getInventory();
		ItemStack item = inventory.getItem(actualSlot);
		EntityEquipment equipment = justInventory.getInventoryOwner().getEquipment();
		ItemStack air = new ItemStack(Material.AIR);
		boolean relevant = false;

		switch (actualSlot) {
		case 1:
			if (equipment.getHelmet() != null && equipment.getHelmet().isSimilar(item))
				relevant = true;
			if (relevant)
				equipment.setHelmet(air);
			break;
		case 2:
			if (equipment.getChestplate() != null && equipment.getChestplate().isSimilar(item))
				relevant = true;
			if (relevant)
				equipment.setChestplate(air);
			break;
		case 3:
			if (equipment.getLeggings() != null && equipment.getLeggings().isSimilar(item))
				relevant = true;
			if (relevant)
				equipment.setLeggings(air);
			break;
		case 4:
			if (equipment.getBoots() != null && equipment.getBoots().isSimilar(item))
				relevant = true;
			if (relevant)
				equipment.setBoots(air);
			break;
		case 7:
			if (equipment.getItemInOffHand() != null && equipment.getItemInOffHand().isSimilar(item))
				relevant = true;
			if (relevant)
				equipment.setItemInOffHand(air);
			break;
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		JustInventory justInventory = justInventoryManager.getJustInventory(e.getView());

		if (justInventory == null || e.getInventory() == null)
			return;

		if (justInventory.getJustInventoryType().equals(Type.PLAYER_INVENTORY)) {

			Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {
				public void run() {
					justInventory.getInventoryOwner().updateInventory();
				}
			}, 2L);

		}

		if (e.getView().getTitle().equals("Armor - JustInvSee (C)")) {
			e.setResult(Result.DENY);
			return;
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		JustInventory justInventory = justInventoryManager.getJustInventory(e.getView());

		if (justInventory != null)
			justInventoryManager.remove(justInventory);

	}

	private boolean isValidPlayer(CommandSender sender, Command command, String[] args) {
		if (!(sender instanceof Player)) {
			Message exception = new Message(this, MessageType.PLAYERS_ONLY);
			exception.doSend(sender);
			return false;
		}
		if (!sender.hasPermission("justinvsee." + command.getName())) {
			Message exception = new Message(this, MessageType.NO_PERMISSION);
			exception.doSend(sender);
			return false;
		}
		if (args.length != 1) {
			MessageType messageType = MessageType.getMessageTypeByCommand(command.getName());
			Message exception = new Message(this, messageType);
			exception.doSend(sender);
			return false;
		}
		if (getTargetPlayer(sender, args[0]) == null) {
			Message exception = new Message(this, MessageType.PLAYER_NOT_FOUND);
			exception.doSend(sender);
			return false;
		}
		return true;
	}

	private boolean isValidCommand(CommandSender sender, Command command, String[] args) {

		if (!sender.hasPermission("justinvsee.changeconfig")) {
			Message exception = new Message(this, MessageType.NO_PERMISSION);
			exception.doSend(sender);
			return false;
		}

		if (args.length != 1) {
			Message exception = new Message(this, MessageType.USAGE_JUSTINVSEE);
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
				Message exception = new Message(this, MessageType.SETTING_CHANGED, setValue);
				exception.doSend(sender);
				return true;
			} else {
				Message exception = new Message(this, MessageType.USAGE_JUSTINVSEE);
				exception.doSend(sender);
				return true;
			}
		}

		if (!isValidPlayer(sender, command, args))
			return true;
		Player player = (Player) sender;
		Player targetPlayer = getTargetPlayer(sender, args[0]);

		if (targetPlayer.hasPermission("justinvsee." + label) && getMessageStatus()) {
			Message notification = new Message(this, MessageType.LOOKED_IN_INVENTORY, targetPlayer.getName());
			notification.doSend(targetPlayer);
		}

		if (command.getName().equalsIgnoreCase("inv")) {
			JustInventory playerInventory = new JustInventory(Type.PLAYER_INVENTORY, targetPlayer,
					justInventoryManager);
			playerInventory.openInventory(player, effectsManager);
			return true;
		}
		if (command.getName().equalsIgnoreCase("enderinv")) {
			JustInventory enderInventory = new JustInventory(Type.ENDER_INVENTORY, targetPlayer, justInventoryManager);
			enderInventory.openInventory(player, effectsManager);
			return true;
		}
		if (command.getName().equalsIgnoreCase("armorinv")) {
			JustInventory armorInventory = new JustInventory(Type.ARMOR_INVENTORY, targetPlayer, justInventoryManager);
			armorInventory.openInventory(player, effectsManager);
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

}

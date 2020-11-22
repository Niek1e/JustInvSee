package me.Niek1e.JustInvSee;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_16_R3.Containers;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;

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

	private InventoryView[] openViews;

	public void onEnable() {
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, (Plugin) this);
		openViews = new InventoryView[0];
	}

	public void onDisable() {
		closeAllViews();
		saveDefaultConfig();
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		if (isInOpenViews(e.getView())) {

			if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof Player) {
				Player player = (Player) e.getClickedInventory().getHolder();

				Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {
					public void run() {
						player.updateInventory();
					}
				}, 2L);

			}
		}

		if (e.getView().getTitle().equals("Armor - JustInvSee (C)")) {
			e.setResult(Result.DENY);
			return;
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {
		if (isInOpenViews(e.getView())) {

			if (e.getInventory() != null && e.getInventory().getHolder() instanceof Player) {
				Player player = (Player) e.getInventory().getHolder();

				Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {
					public void run() {
						player.updateInventory();
					}
				}, 2L);

			}
		}

		if (e.getView().getTitle().equals("Armor - JustInvSee (C)")) {
			e.setResult(Result.DENY);
			return;
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (isInOpenViews(e.getView())) {
			removeOpenView(e.getView());
		}
	}

	protected InventoryView[] getOpenViews() {
		return openViews;
	}

	private void closeAllViews() {
		int i;

		for (i = 0; i < getOpenViews().length; i++)
			getOpenViews()[i].close();

	}

	protected void addOpenView(InventoryView newView) {
		int n = getOpenViews().length;
		int i;
		InventoryView[] newArray = new InventoryView[n + 1];

		for (i = 0; i < getOpenViews().length; i++)
			newArray[i] = getOpenViews()[i];
		newArray[n] = newView;
		openViews = newArray;
		return;
	}

	protected void removeOpenView(InventoryView removeView) {
		int n = getOpenViews().length;
		int i;
		int rm = 0;

		for (i = 0; i < getOpenViews().length; i++) {
			if (getOpenViews()[i].equals(removeView)) {
				rm = i;
				break;
			}
		}

		InventoryView[] newArray = new InventoryView[n - 1];

		for (i = 0; i < getOpenViews().length; i++) {
			if (i == rm)
				continue;
			newArray[i] = getOpenViews()[i];
		}
		openViews = newArray;
		return;
	}

	protected boolean isInOpenViews(InventoryView inventoryView) {
		int i;
		for (i = 0; i < getOpenViews().length; i++) {
			if (getOpenViews()[i].equals(inventoryView))
				return true;
		}
		return false;
	}

	private void doOpenInventory(Player player, Inventory inventory) {
		InventoryView newView = player.openInventory(inventory);
		addOpenView(newView);
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
			doOpenInventory(player, (Inventory) playerInventory);
			sendInventoryPacket(player, targetPlayer.getName());
			targetPlayer.updateInventory();
			player.playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.MOBSPAWNER_FLAMES, null);
			return true;
		}
		if (command.getName().equalsIgnoreCase("enderinv")) {
			Inventory targetInventory = targetPlayer.getEnderChest();
			player.openInventory(targetInventory);
			playEnderchestEffect(player);
			return true;
		}
		if (command.getName().equalsIgnoreCase("armorinv")) {
			player.openInventory(getArmorInventory(targetPlayer));
			player.playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.MOBSPAWNER_FLAMES, null);
			return true;
		}
		return false;
	}

	private void sendInventoryPacket(Player player, String inventoryTitle) {
		CraftPlayer cPlayer = (CraftPlayer) player;
		PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(cPlayer.getHandle().activeContainer.windowId,
				Containers.GENERIC_9X4, IChatBaseComponent.ChatSerializer
						.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', inventoryTitle + "\"}")));
		cPlayer.getHandle().playerConnection.sendPacket(packet);

	}

	private boolean toggleMessages() {
		boolean value = getMessageStatus();
		getConfig().set("opmessage", Boolean.valueOf(!value));
		saveConfig();
		return !value;
	}

	private ArrayList<Location> getEnderchestLocations(Chunk[] chunks) {
		ArrayList<Location> enderchestLocations = new ArrayList<Location>();

		for (int i = 0; i < chunks.length; i++) {
			BlockState[] tileEntities = chunks[i].getTileEntities();

			for (int j = 0; j < tileEntities.length; j++) {
				if (tileEntities[j].getType().equals(Material.ENDER_CHEST)) {
					enderchestLocations.add(tileEntities[j].getLocation());
				}
			}

		}

		return enderchestLocations;
	}

	private void playEnderchestEffect(Player player) {
		Location playerLocation = player.getLocation();
		Chunk[] chunks = getSurroundingChunks(playerLocation);
		ArrayList<Location> enderChests = getEnderchestLocations(chunks);

		for (int i = 0; i < enderChests.size(); i++) {
			player.playEffect(enderChests.get(i).add(0, 0.5, 0), Effect.MOBSPAWNER_FLAMES, null);
		}
	}

	private Chunk[] getSurroundingChunks(Location location) {
		int chunkX = location.getChunk().getX();
		int chunkZ = location.getChunk().getZ();
		Chunk[] chunks = new Chunk[9];
		chunks[0] = location.getWorld().getChunkAt(chunkX - 1, chunkZ - 1);
		int i = 0;
		int x;
		int z;

		for (x = -1; x < 2; x++) {
			for (z = -1; z < 2; z++) {
				chunks[i] = location.getWorld().getChunkAt(chunkX - x, chunkZ - z);
				i++;
			}
		}

		return chunks;
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

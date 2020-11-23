package me.Niek1e.JustInvSee;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_16_R3.Containers;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;

public class JustInventory {

	public enum Type {
		PLAYER_INVENTORY, ENDER_INVENTORY, ARMOR_INVENTORY;
	}

	private Player inventoryOwner;
	private Inventory inventory;
	private InventoryView inventoryView;
	private Type justInventoryType;
	private JustInventoryManager justInventoryManager;

	public JustInventory(Type justInventoryType, Player targetPlayer, JustInventoryManager justInventoryManager) {
		this.justInventoryType = justInventoryType;
		this.inventoryOwner = targetPlayer;
		this.inventoryView = null;
		this.inventory = createInventory();
		this.justInventoryManager = justInventoryManager;
	}

	public Player getInventoryOwner() {
		return this.inventoryOwner;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public InventoryView getInventoryView() {
		return this.inventoryView;
	}

	public Type getJustInventoryType() {
		return this.justInventoryType;
	}
	
	public ItemStack getRemoveStack() {
		return getInventoryItem(Material.BARRIER, ChatColor.RED + "Clear Item");
	}
	
	public void openInventory(Player player, EffectsManager effectsManager) {
		
		if (this.inventoryView != null)
			return;
		
		this.inventoryView = player.openInventory(this.inventory);
		
		switch(this.justInventoryType) {
		case PLAYER_INVENTORY:
			sendInventoryPacket(player);
			effectsManager.playEffects(player, this.inventoryOwner);
			this.getInventoryOwner().updateInventory();
			break;
		case ENDER_INVENTORY:
			effectsManager.playEffects(player);
			break;
		case ARMOR_INVENTORY:
			effectsManager.playEffects(player, this.inventoryOwner);
			break;
		}
		
		justInventoryManager.add(this);
		
	}
	
	private Inventory createInventory() {
		
		Inventory newInventory = null;
		
		switch (justInventoryType) {
		case ARMOR_INVENTORY:
			newInventory = createArmorInventory();
			break;
		case ENDER_INVENTORY:
			newInventory = createEnderInventory();
			break;
		case PLAYER_INVENTORY:
			newInventory = createPlayerInventory();
			break;
		}
		
		return newInventory;
	}
	
	private Inventory createPlayerInventory() {
		return inventoryOwner.getInventory();
	}
	
	private Inventory createEnderInventory() {
		return inventoryOwner.getEnderChest();
	}
	
	private Inventory createArmorInventory() {
		
		int size = 18;
		
		Inventory armorInventory = Bukkit.getServer().createInventory(null, size, "Armor - JustInvSee (C)");
		
		ItemStack remove = getInventoryItem(Material.BARRIER, ChatColor.RED + "Clear Item");
		ItemStack empty = getInventoryItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.RED + "[]");
		
		EntityEquipment equipment = this.inventoryOwner.getEquipment();

		armorInventory.setItem(1, equipment.getHelmet());
		armorInventory.setItem(2, equipment.getChestplate());
		armorInventory.setItem(3, equipment.getLeggings());
		armorInventory.setItem(4, equipment.getBoots());
		armorInventory.setItem(7, equipment.getItemInOffHand());

		for (int i = 0; i < size; i++) {

			if (i > 8 && !armorInventory.getItem(i - 9).equals(empty))
				armorInventory.setItem(i, remove);

			if (armorInventory.getItem(i) == null || armorInventory.getItem(i).getType().equals(Material.AIR))
				armorInventory.setItem(i, empty);

		}

		return armorInventory;
	}
	
	private ItemStack getInventoryItem(Material material, String name) {
		ItemStack i = new ItemStack(material, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		i.setItemMeta(im);
		return i;
	}
	
	private void sendInventoryPacket(Player player) {
		
		CraftPlayer cPlayer = (CraftPlayer) player;
		PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(cPlayer.getHandle().activeContainer.windowId,
				Containers.GENERIC_9X4, IChatBaseComponent.ChatSerializer
						.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', this.inventoryOwner.getName() + "\"}")));
		cPlayer.getHandle().playerConnection.sendPacket(packet);

	}
}

package me.Niek1e.JustInvSee.JustInventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import me.Niek1e.JustInvSee.EffectsManager;
import me.Niek1e.JustInvSee.JustInventoryManager;

public abstract class JustInventory {

	public enum Type {
		PLAYER_INVENTORY, ENDER_INVENTORY, ARMOR_INVENTORY;
	}

	private Player inventoryOwner;
	private Inventory inventory;
	private InventoryView inventoryView;
	private Type justInventoryType;
	private JustInventoryManager justInventoryManager;

	protected abstract Inventory createInventory();
	protected abstract void openInventory(Player player, EffectsManager effectsManager);
	protected abstract void openInventory(Player player);

	protected JustInventory(Type justInventoryType, Player targetPlayer, JustInventoryManager justInventoryManager) {
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
	
	public void setInventoryView(InventoryView newView) {
		this.inventoryView = newView;
	}

	public Type getJustInventoryType() {
		return this.justInventoryType;
	}

	protected JustInventoryManager getJustInventoryManager() {
		return this.justInventoryManager;
	}

}

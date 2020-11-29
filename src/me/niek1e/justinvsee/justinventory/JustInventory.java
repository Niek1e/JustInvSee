package me.niek1e.justinvsee.justinventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import me.niek1e.justinvsee.EffectsManager;
import me.niek1e.justinvsee.JustInventoryManager;

public abstract class JustInventory {

	private Player inventoryOwner;
	private Player inventoryViewer;
	private Inventory inventory;
	private InventoryView inventoryView;
	private JustInventoryManager justInventoryManager;

	protected abstract Inventory createInventory();
	protected abstract void openInventory(Player player, EffectsManager effectsManager);
	protected abstract void openInventory(Player player);

	protected JustInventory(Player targetPlayer, JustInventoryManager justInventoryManager) {
		this.inventoryOwner = targetPlayer;
		this.inventoryViewer = null;
		this.inventoryView = null;
		this.inventory = createInventory();
		this.justInventoryManager = justInventoryManager;
	}

	public Player getInventoryOwner() {
		return this.inventoryOwner;
	}
	
	public Player getInventoryViewer() {
		return this.inventoryViewer;
	}
	
	public void setInventoryViewer(Player player) {
		this.inventoryViewer = player;
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

	protected JustInventoryManager getJustInventoryManager() {
		return this.justInventoryManager;
	}

}

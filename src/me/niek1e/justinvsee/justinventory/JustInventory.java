package me.niek1e.justinvsee.justinventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.niek1e.justinvsee.EffectsManager;

public abstract class JustInventory {

	private Player inventoryOwner;
	private Inventory inventory;

	protected abstract Inventory createInventory();

	protected abstract void openInventory(Player player, EffectsManager effectsManager);

	protected abstract void openInventory(Player player);

	protected JustInventory(Player targetPlayer) {
		this.inventoryOwner = targetPlayer;
		this.inventory = createInventory();
	}

	public Player getInventoryOwner() {
		return this.inventoryOwner;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

}

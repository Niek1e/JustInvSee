package me.niek1e.justinvsee.justinventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.niek1e.justinvsee.EffectsManager;
import me.niek1e.justinvsee.JustInventoryManager;

public class JustEnderInventory extends JustInventory {

	public JustEnderInventory(Player targetPlayer, JustInventoryManager justInventoryManager) {
		super(targetPlayer, justInventoryManager);
	}

	@Override
	protected Inventory createInventory() {
		return this.getInventoryOwner().getEnderChest();
	}

	@Override
	public void openInventory(Player player, EffectsManager effectsManager) {
		if (this.getInventoryView() != null)
			return;

		this.setInventoryViewer(player);
		this.setInventoryView(player.openInventory(this.getInventory()));
		effectsManager.playEnderchestEffects(player);
		this.getJustInventoryManager().add(this);
		
	}

	@Override
	public void openInventory(Player player) {
		if (this.getInventoryView() != null)
			return;

		this.setInventoryViewer(player);
		this.setInventoryView(player.openInventory(this.getInventory()));
		this.getJustInventoryManager().add(this);
	}

}

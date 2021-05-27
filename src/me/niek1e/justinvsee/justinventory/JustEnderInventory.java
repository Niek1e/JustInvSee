package me.niek1e.justinvsee.justinventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.niek1e.justinvsee.EffectsManager;

public class JustEnderInventory extends JustInventory {

	public JustEnderInventory(Player targetPlayer) {
		super(targetPlayer);
	}

	@Override
	protected Inventory createInventory() {
		return this.getInventoryOwner().getEnderChest();
	}

	@Override
	public void openInventory(Player player, EffectsManager effectsManager) {
		player.openInventory(this.getInventory());
		effectsManager.playEnderchestEffects(player);
	}

	@Override
	public void openInventory(Player player) {
		player.openInventory(this.getInventory());
	}

}

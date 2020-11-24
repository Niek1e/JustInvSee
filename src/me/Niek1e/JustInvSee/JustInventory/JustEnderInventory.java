package me.Niek1e.JustInvSee.JustInventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.Niek1e.JustInvSee.EffectsManager;
import me.Niek1e.JustInvSee.JustInventoryManager;

public class JustEnderInventory extends JustInventory {

	public JustEnderInventory(Player targetPlayer, JustInventoryManager justInventoryManager) {
		super(Type.ENDER_INVENTORY, targetPlayer, justInventoryManager);
	}

	@Override
	protected Inventory createInventory() {
		return this.getInventoryOwner().getEnderChest();
	}

	@Override
	public void openInventory(Player player, EffectsManager effectsManager) {
		if (this.getInventoryView() != null)
			return;

		this.setInventoryView(player.openInventory(this.getInventory()));
		effectsManager.playEffects(player);
		this.getJustInventoryManager().add(this);
		
	}

	@Override
	public void openInventory(Player player) {
		if (this.getInventoryView() != null)
			return;

		this.setInventoryView(player.openInventory(this.getInventory()));
		this.getJustInventoryManager().add(this);
	}

}

package me.niek1e.justinvsee.justinventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.niek1e.justinvsee.EffectsManager;
import me.niek1e.justinvsee.JustInventoryManager;
import me.niek1e.justinvsee.packets.InventoryName;

public class JustPlayerInventory extends JustInventory {

	public JustPlayerInventory(Player targetPlayer, JustInventoryManager justInventoryManager) {
		super(targetPlayer, justInventoryManager);
	}

	@Override
	protected Inventory createInventory() {
		return this.getInventoryOwner().getInventory();
	}

	@Override
	public void openInventory(Player player, EffectsManager effectsManager) {
		if (this.getInventoryView() != null)
			return;

		this.setInventoryViewer(player);
		this.setInventoryView(player.openInventory(this.getInventory()));
		sendInventoryPacket(player);
		effectsManager.playPlayerEffects(player, this.getInventoryOwner());

		this.getInventoryOwner().updateInventory();
		this.getJustInventoryManager().add(this);
	}

	@Override
	public void openInventory(Player player) {
		if (this.getInventoryView() != null)
			return;

		this.setInventoryViewer(player);
		this.setInventoryView(player.openInventory(this.getInventory()));
		sendInventoryPacket(player);

		this.getInventoryOwner().updateInventory();
		this.getJustInventoryManager().add(this);
	}

	private void sendInventoryPacket(Player player) {

		InventoryName inventoryNamePacket = InventoryName.getInventoryName(player, this.getInventoryOwner());
		if(inventoryNamePacket != null)
			inventoryNamePacket.sendPacket();

	}

}

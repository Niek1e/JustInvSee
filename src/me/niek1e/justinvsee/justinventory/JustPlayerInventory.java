package me.niek1e.justinvsee.justinventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.niek1e.justinvsee.EffectsManager;
import me.niek1e.justinvsee.JustInvSee;

public class JustPlayerInventory extends JustInventory {

	private JustInvSee plugin;

	public JustPlayerInventory(Player targetPlayer, JustInvSee plugin) {
		super(targetPlayer);
		this.plugin = plugin;
	}

	@Override
	protected Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null, 36, this.getInventoryOwner().getName() + "'s Inventory");
		inventory.setStorageContents(this.getInventoryOwner().getInventory().getStorageContents());
		return inventory;
	}

	@Override
	public void openInventory(Player player, EffectsManager effectsManager) {
		syncToInventory();
		player.openInventory(this.getInventory());
		effectsManager.playPlayerEffects(player, this.getInventoryOwner());
	}

	@Override
	public void openInventory(Player player) {
		syncToInventory();
		player.openInventory(this.getInventory());
	}

	public void syncToOwner() {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				ItemStack[] justInventoryContents = getInventory().getStorageContents();
				getInventoryOwner().getInventory().setStorageContents(justInventoryContents);
			}
		}, 1L);
	}

	public void syncToInventory() {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				ItemStack[] inventoryContents = getInventoryOwner().getInventory().getStorageContents();
				getInventory().setStorageContents(inventoryContents);
			}
		}, 1L);
	}

}

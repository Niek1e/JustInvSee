package me.niek1e.justinvsee.justinventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.niek1e.justinvsee.EffectsManager;
import me.niek1e.justinvsee.JustInvSee;

public class JustArmorInventory extends JustInventory {

	private JustInvSee plugin;

	public JustArmorInventory(Player targetPlayer, JustInvSee plugin) {
		super(targetPlayer);
		this.plugin = plugin;
	}

	public void update(int actualSlot) {
		processUpdate(actualSlot);
		syncToInventory();
	}

	private void processUpdate(int actualSlot) {
		ItemStack item = this.getInventory().getItem(actualSlot);
		PlayerInventory equipment = this.getInventoryOwner().getInventory();
		ItemStack air = new ItemStack(Material.AIR);

		switch (actualSlot) {
		case 1:
			if (equipment.getHelmet() != null && equipment.getHelmet().isSimilar(item))
				equipment.setHelmet(air);
			break;
		case 2:
			if (equipment.getChestplate() != null && equipment.getChestplate().isSimilar(item))
				equipment.setChestplate(air);
			break;
		case 3:
			if (equipment.getLeggings() != null && equipment.getLeggings().isSimilar(item))
				equipment.setLeggings(air);
			break;
		case 4:
			if (equipment.getBoots() != null && equipment.getBoots().isSimilar(item))
				equipment.setBoots(air);
			break;
		case 7:
			if (equipment.getItemInOffHand().isSimilar(item))
				equipment.setItemInOffHand(air);
			break;
		}
	}

	@Override
	protected Inventory createInventory() {

		String name = getInventoryOwner().getName() + "'s Armor - JustInvSee (C)";
		return Bukkit.getServer().createInventory(null, 18, name);
	}

	private void updateArmorInventory() {

		Inventory armorInventory = getInventory();
		
		armorInventory.clear();

		ItemStack remove = getInventoryItem(Material.BARRIER, ChatColor.RED + "Clear Item");
		ItemStack empty = getInventoryItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.RED + "[]");

		EntityEquipment equipment = this.getInventoryOwner().getEquipment();

		armorInventory.setItem(1, equipment.getHelmet());
		armorInventory.setItem(2, equipment.getChestplate());
		armorInventory.setItem(3, equipment.getLeggings());
		armorInventory.setItem(4, equipment.getBoots());
		armorInventory.setItem(7, equipment.getItemInOffHand());

		for (int i = 0; i < armorInventory.getSize(); i++) {

			if (i > 8 && !armorInventory.getItem(i - 9).equals(empty))
				armorInventory.setItem(i, remove);

			if (armorInventory.getItem(i) == null || armorInventory.getItem(i).getType().equals(Material.AIR))
				armorInventory.setItem(i, empty);

		}
	}

	private ItemStack getInventoryItem(Material material, String name) {
		ItemStack i = new ItemStack(material, 1);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName(name);
		i.setItemMeta(im);
		return i;
	}

	public boolean checkRemoveStack(ItemStack itemStack) {
		return getInventoryItem(Material.BARRIER, ChatColor.RED + "Clear Item").isSimilar(itemStack);
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

	public void syncToInventory() {
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			public void run() {
				updateArmorInventory();
			}
		}, 1L);
	}

}

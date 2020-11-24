package me.Niek1e.JustInvSee.JustInventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Niek1e.JustInvSee.EffectsManager;
import me.Niek1e.JustInvSee.JustInventoryManager;

public class JustArmorInventory extends JustInventory {

	public JustArmorInventory(Player targetPlayer, JustInventoryManager justInventoryManager) {
		super(Type.ARMOR_INVENTORY, targetPlayer, justInventoryManager);
	}

	public JustArmorInventory getUpdatedInventory(int actualSlot) {
		return createUpdatedInventory(actualSlot);
	}

	private JustArmorInventory createUpdatedInventory(int actualSlot) {
		processUpdate(actualSlot);
		JustArmorInventory updatedInventory = new JustArmorInventory(this.getInventoryOwner(),
				this.getJustInventoryManager());
		return updatedInventory;
	}

	private void processUpdate(int actualSlot) {
		ItemStack item = this.getInventory().getItem(actualSlot);
		EntityEquipment equipment = this.getInventoryOwner().getEquipment();
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
			if (equipment.getItemInOffHand() != null && equipment.getItemInOffHand().isSimilar(item))
				equipment.setItemInOffHand(air);
			break;
		}
	}

	@Override
	protected Inventory createInventory() {
		return createArmorInventory();
	}

	private Inventory createArmorInventory() {

		int size = 18;

		Inventory armorInventory = Bukkit.getServer().createInventory(null, size, "Armor - JustInvSee (C)");

		ItemStack remove = getInventoryItem(Material.BARRIER, ChatColor.RED + "Clear Item");
		ItemStack empty = getInventoryItem(Material.WHITE_STAINED_GLASS_PANE, ChatColor.RED + "[]");

		EntityEquipment equipment = this.getInventoryOwner().getEquipment();

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

	public boolean checkRemoveStack(ItemStack itemStack) {
		return getInventoryItem(Material.BARRIER, ChatColor.RED + "Clear Item").isSimilar(itemStack);
	}

	@Override
	public void openInventory(Player player, EffectsManager effectsManager) {
		if (this.getInventoryView() != null)
			return;

		this.setInventoryView(player.openInventory(this.getInventory()));
		effectsManager.playEffects(player, this.getInventoryOwner());
		
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

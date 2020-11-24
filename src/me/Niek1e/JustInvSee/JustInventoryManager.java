package me.Niek1e.JustInvSee;

import org.bukkit.inventory.InventoryView;

import me.Niek1e.JustInvSee.JustInventory.JustArmorInventory;
import me.Niek1e.JustInvSee.JustInventory.JustInventory;
import me.Niek1e.JustInvSee.JustInventory.JustInventory.Type;

public class JustInventoryManager {

	private JustInventory[] allJustInventories;

	public JustInventoryManager() {
		this.allJustInventories = new JustInventory[0];
	}

	public void add(JustInventory justInventory) {
		addToList(justInventory);
	}

	public void remove(JustInventory justInventory) {
		removeFromList(justInventory);
	}

	public void closeAll() {
		closeAllInventoryViews();
	}

	public JustInventory getJustInventory(InventoryView inventoryView) {
		for (int i = 0; i < allJustInventories.length; i++) {
			if (allJustInventories[i].getInventoryView().equals(inventoryView))
				return allJustInventories[i];
		}
		return null;
	}

	public JustArmorInventory getJustArmorInventory(InventoryView inventoryView) {
		for (int i = 0; i < allJustInventories.length; i++) {
			if (allJustInventories[i].getInventoryView().equals(inventoryView)
					&& allJustInventories[i].getJustInventoryType().equals(Type.ARMOR_INVENTORY))
				return (JustArmorInventory) allJustInventories[i];
		}
		return null;
	}

	private void addToList(JustInventory justInventory) {
		int n = allJustInventories.length;
		JustInventory[] newArray = new JustInventory[n + 1];

		for (int i = 0; i < n; i++)
			newArray[i] = allJustInventories[i];

		newArray[n] = justInventory;
		allJustInventories = newArray;
	}

	private void removeFromList(JustInventory justInventory) {
		int n = allJustInventories.length;
		int removeIndex = 0;

		for (int i = 0; i < allJustInventories.length; i++) {
			if (allJustInventories[i].equals(justInventory)) {
				removeIndex = i;
				break;
			}
		}

		JustInventory[] newArray = new JustInventory[n - 1];

		for (int i = 0; i < allJustInventories.length; i++) {
			if (i == removeIndex)
				continue;
			newArray[i] = allJustInventories[i];
		}

		allJustInventories = newArray;
	}

	private void closeAllInventoryViews() {
		for (int i = 0; i < allJustInventories.length; i++) {
			if(allJustInventories[i].getInventoryView() != null)
				allJustInventories[i].getInventoryView().close();
		}
	}

}

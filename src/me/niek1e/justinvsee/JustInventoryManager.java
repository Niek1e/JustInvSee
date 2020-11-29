package me.niek1e.justinvsee;

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import me.niek1e.justinvsee.justinventory.JustArmorInventory;
import me.niek1e.justinvsee.justinventory.JustInventory;
import me.niek1e.justinvsee.message.Message;
import me.niek1e.justinvsee.message.MessageType;

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

	public void closeByPlayer(JustInvSee main, Player player) {
		closeAllInventoryViews(main, player);
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
					&& allJustInventories[i] instanceof JustArmorInventory)
				return (JustArmorInventory) allJustInventories[i];
		}
		return null;
	}

	private void addToList(JustInventory justInventory) {
		int n = allJustInventories.length;
		JustInventory[] newArray = Arrays.copyOf(allJustInventories, n + 1);

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
			if (allJustInventories[i].getInventoryView() != null)
				allJustInventories[i].getInventoryView().close();
		}
	}

	private void closeAllInventoryViews(JustInvSee main, Player player) {
		for (int i = 0; i < allJustInventories.length; i++) {
			if (allJustInventories[i].getInventoryView() != null
					&& allJustInventories[i].getInventoryOwner().equals(player)) {
				Message exception = new Message(main, MessageType.PLAYER_LEFT);
				exception.doSend(allJustInventories[i].getInventoryViewer());
				allJustInventories[i].getInventoryView().close();
			}
		}
	}

}

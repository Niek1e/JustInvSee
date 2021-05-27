package me.niek1e.justinvsee;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.niek1e.justinvsee.justinventory.JustArmorInventory;
import me.niek1e.justinvsee.justinventory.JustInventory;
import me.niek1e.justinvsee.justinventory.JustPlayerInventory;
import me.niek1e.justinvsee.message.Message;
import me.niek1e.justinvsee.message.MessageType;

public class JustInventoryManager {

	private JustInvSee plugin;

	private ArrayList<JustPlayerInventory> listJustPlayerInventory;
	private ArrayList<JustArmorInventory> listJustArmorInventory;

	public JustInventoryManager(JustInvSee plugin) {
		this.plugin = plugin;
		this.listJustPlayerInventory = new ArrayList<>();
		this.listJustArmorInventory = new ArrayList<>();
	}

	public JustInventory getJustInventory(Inventory inventory) {
		return searchByInventory(inventory);
	}

	public JustPlayerInventory getByTarget(Player target) {
		JustPlayerInventory justPlayerInventory = playerByTarget(target);
		if (justPlayerInventory == null) {
			return createJustPlayerInventoryByTarget(target);
		} else {
			return justPlayerInventory;
		}
	}

	public JustArmorInventory getArmorByTarget(Player target) {
		JustArmorInventory justArmorInventory = armorByTarget(target);
		if (justArmorInventory == null) {
			return createJustArmorInventoryByTarget(target);
		} else {
			return justArmorInventory;
		}
	}

	public void closeAll() {
		closeAllJustInventories();
	}

	public void closeByPlayer(JustInvSee plugin, Player player) {
		closeJustInventoryByPlayer(plugin, player);
	}

	public void remove(JustInventory justInventory) {
		if (justInventory instanceof JustPlayerInventory) {
			listJustPlayerInventory.remove(justInventory);
		} else if (justInventory instanceof JustArmorInventory) {
			listJustArmorInventory.remove(justInventory);
		}

	}

	public void syncOwnerToInventory(Player player) {
		JustPlayerInventory justPlayerInventory = playerByTarget(player);
		JustArmorInventory justArmorInventory = armorByTarget(player);

		if (justPlayerInventory != null) {
			justPlayerInventory.syncToInventory();
		}
		if (justArmorInventory != null) {
			justArmorInventory.syncToInventory();
		}
	}

	private JustPlayerInventory createJustPlayerInventoryByTarget(Player target) {
		JustPlayerInventory playerInventory = new JustPlayerInventory(target, plugin);
		addPlayerToList(playerInventory);
		return playerInventory;
	}

	private JustArmorInventory createJustArmorInventoryByTarget(Player target) {
		JustArmorInventory armorInventory = new JustArmorInventory(target, plugin);
		addArmorToList(armorInventory);
		return armorInventory;
	}

	private JustInventory searchByInventory(Inventory inventory) {
		for (int i = 0; i < listJustPlayerInventory.size(); i++) {
			if (listJustPlayerInventory.get(i).getInventory().equals(inventory))
				return listJustPlayerInventory.get(i);
		}

		for (int i = 0; i < listJustArmorInventory.size(); i++) {
			if (listJustArmorInventory.get(i).getInventory().equals(inventory))
				return listJustArmorInventory.get(i);
		}

		return null;
	}

	private JustPlayerInventory playerByTarget(Player player) {
		for (int i = 0; i < listJustPlayerInventory.size(); i++) {
			if (listJustPlayerInventory.get(i).getInventoryOwner().equals(player))
				return listJustPlayerInventory.get(i);
		}

		return null;
	}

	private JustArmorInventory armorByTarget(Player player) {
		for (int i = 0; i < listJustArmorInventory.size(); i++) {
			if (listJustArmorInventory.get(i).getInventoryOwner().equals(player))
				return listJustArmorInventory.get(i);
		}

		return null;
	}

	private void closeAllJustInventories() {
		for (int i = 0; i < listJustPlayerInventory.size(); i++) {
			for (int x = 0; x < listJustPlayerInventory.get(i).getInventory().getViewers().size(); x++) {
				listJustPlayerInventory.get(i).getInventory().getViewers().get(x).closeInventory();
			}
		}

		for (int i = 0; i < listJustArmorInventory.size(); i++) {
			for (int x = 0; x < listJustArmorInventory.get(i).getInventory().getViewers().size(); x++) {
				listJustArmorInventory.get(i).getInventory().getViewers().get(x).closeInventory();
			}
		}
	}

	private void closeJustInventoryByPlayer(JustInvSee plugin, Player player) {
		Message exception = new Message(plugin, MessageType.PLAYER_LEFT);

		JustPlayerInventory justPlayerInventory = playerByTarget(player);
		JustArmorInventory justArmorInventory = armorByTarget(player);

		if (justPlayerInventory != null) {
			for (int i = 0; i < justPlayerInventory.getInventory().getViewers().size(); i++) {
				exception.doSend(justPlayerInventory.getInventory().getViewers().get(i));
				justPlayerInventory.getInventory().getViewers().get(i).closeInventory();
			}

			remove(justPlayerInventory);
		}

		if (justArmorInventory != null) {
			for (int i = 0; i < justArmorInventory.getInventory().getViewers().size(); i++) {
				exception.doSend(justArmorInventory.getInventory().getViewers().get(i));
				justArmorInventory.getInventory().getViewers().get(i).closeInventory();
			}

			remove(justArmorInventory);
		}
	}

	private void addPlayerToList(JustPlayerInventory justPlayerInventory) {
		listJustPlayerInventory.add(justPlayerInventory);
	}

	private void addArmorToList(JustArmorInventory justArmorInventory) {
		listJustArmorInventory.add(justArmorInventory);
	}
}

package me.niek1e.justinvsee;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import me.niek1e.justinvsee.justinventory.JustArmorInventory;
import me.niek1e.justinvsee.justinventory.JustInventory;
import me.niek1e.justinvsee.justinventory.JustPlayerInventory;

public class JustInventoryEvents implements Listener {

	private JustInvSee plugin;

	private void syncInventoryToOwner(JustPlayerInventory justPlayerInventory) {
		// Sync the JustPlayerInventory to the inventory owner
		justPlayerInventory.syncToOwner();
	}

	private void syncOwnerToInventory(Player player) {
		// Sync the Owner's inventory to the JustInventory views
		plugin.getJustInventoryManager().syncOwnerToInventory(player);
	}

	public JustInventoryEvents(JustInvSee plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		plugin.getJustInventoryManager().closeByPlayer(plugin, e.getPlayer());
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		// Sync the inventory if there is an open view
		if (e.getWhoClicked() instanceof Player) {
			Player player = (Player) e.getWhoClicked();
			syncOwnerToInventory(player);
		}

		if (e.getClickedInventory() == null)
			return;

		// Check if this concerns a JustInventory inventory
		JustInventory justInventoryClicked = plugin.getJustInventoryManager().getJustInventory(e.getClickedInventory());
		JustInventory justInventoryOpen = plugin.getJustInventoryManager()
				.getJustInventory(e.getView().getTopInventory());

		// Check if the JustInventory is of type JustPlayerInventory to sync the
		// inventory
		if (justInventoryOpen instanceof JustPlayerInventory) {
			syncInventoryToOwner((JustPlayerInventory) justInventoryOpen);
		}

		// Check if the JustInventory is of type JustArmorInventory to process changes
		if (justInventoryClicked instanceof JustArmorInventory) {
			JustArmorInventory justArmorInventory = (JustArmorInventory) justInventoryClicked;
			e.setResult(Result.DENY);

			if (e.getCurrentItem() == null || !e.getClick().equals(ClickType.LEFT))
				return;

			if (!justArmorInventory.checkRemoveStack(e.getCurrentItem()))
				return;

			justArmorInventory.update(e.getSlot() - 9);
		}

	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent e) {

		// Sync the inventory if there is an open view
		if (e.getWhoClicked() instanceof Player) {
			Player player = (Player) e.getWhoClicked();
			syncOwnerToInventory(player);
		}

		// Check if this concerns a JustInventory inventory
		JustInventory justInventory = plugin.getJustInventoryManager().getJustInventory(e.getInventory());

		// Check if the JustInventory is of type JustPlayerInventory to sync the
		// inventory
		if (justInventory instanceof JustPlayerInventory) {
			syncInventoryToOwner((JustPlayerInventory) justInventory);
		}

		// Check if the JustInventory is of type JustArmorInventory to process changes
		if (justInventory instanceof JustArmorInventory) {
			e.setResult(Result.DENY);
		}

	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		// Check if this is about a JustInventory view
		JustInventory justInventory = plugin.getJustInventoryManager().getJustInventory(e.getInventory());
		if (justInventory == null)
			return;

		// Check if the JustInventory is of type JustPlayerInventory to sync the content
		if (justInventory instanceof JustPlayerInventory) {
			// Sync JustInventory and Player inventory to make sure no items are lost
			syncInventoryToOwner((JustPlayerInventory) justInventory);
		}

		// Check if there are multiple viewers of the inventory
		if (justInventory.getInventory().getViewers().size() > 1)
			return;

		plugin.getJustInventoryManager().remove(justInventory);

	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		syncOwnerToInventory(e.getPlayer());
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if (e.getEntity() instanceof Player)
			syncOwnerToInventory((Player) e.getEntity());
	}

	@EventHandler
	public void onEntityPickupItem(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player)
			syncOwnerToInventory((Player) e.getEntity());
	}

	@EventHandler
	public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent e) {
		syncOwnerToInventory(e.getPlayer());
	}

	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
		syncOwnerToInventory(e.getPlayer());
	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		syncOwnerToInventory(e.getPlayer());
	}

}

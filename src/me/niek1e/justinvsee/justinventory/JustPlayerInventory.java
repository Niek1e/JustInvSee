package me.niek1e.justinvsee.justinventory;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.niek1e.justinvsee.EffectsManager;
import me.niek1e.justinvsee.JustInventoryManager;
import net.minecraft.server.v1_16_R3.Containers;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutOpenWindow;

public class JustPlayerInventory extends JustInventory {

	public JustPlayerInventory(Player targetPlayer, JustInventoryManager justInventoryManager) {
		super(targetPlayer, justInventoryManager);
	}
	
	private void sendInventoryPacket(Player player) {
		
		CraftPlayer cPlayer = (CraftPlayer) player;
		PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(cPlayer.getHandle().activeContainer.windowId,
				Containers.GENERIC_9X4, IChatBaseComponent.ChatSerializer
						.a("{\"text\": \"" + ChatColor.translateAlternateColorCodes('&', this.getInventoryOwner().getName() + "\"}")));
		cPlayer.getHandle().playerConnection.sendPacket(packet);

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

}

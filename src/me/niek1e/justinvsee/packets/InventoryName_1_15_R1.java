package me.niek1e.justinvsee.packets;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow;

public class InventoryName_1_15_R1 extends InventoryName {

	protected InventoryName_1_15_R1(Player player, Player targetPlayer) {
		super(player, targetPlayer);
	}

	@Override
	public void sendPacket() {
		CraftPlayer cPlayer = (CraftPlayer) this.getPlayer();
		PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(cPlayer.getHandle().activeContainer.windowId,
				Containers.GENERIC_9X4, IChatBaseComponent.ChatSerializer.a("{\"text\": \""
						+ ChatColor.translateAlternateColorCodes('&', this.getTargetPlayerName() + "\"}")));
		cPlayer.getHandle().playerConnection.sendPacket(packet);
	}
	

}

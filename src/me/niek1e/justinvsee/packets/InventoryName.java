package me.niek1e.justinvsee.packets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class InventoryName {

	private Player player;
	private String targetPlayerName;

	protected InventoryName(Player player, Player targetPlayer) {
		this.player = player;
		this.targetPlayerName = targetPlayer.getName();
	}

	public abstract void sendPacket();

	protected Player getPlayer() {
		return this.player;
	}
	
	protected String getTargetPlayerName() {
		return this.targetPlayerName;
	}

	public static InventoryName getInventoryName(Player player, Player targetPlayer) {
		String version;

		try {
			version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		} catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
			return null;
		}
		
		if (version.equals("v1_14_R1")) {
			return new InventoryName_1_14_R1(player, targetPlayer);
		} else if (version.equals("v1_15_R1")) {
			return new InventoryName_1_15_R1(player, targetPlayer);
		} else if (version.equals("v1_16_R1")) {
			return new InventoryName_1_16_R1(player, targetPlayer);
		} else if (version.equals("v1_16_R2")) {
			return new InventoryName_1_16_R2(player, targetPlayer);
		} else if (version.equals("v1_16_R3")) {
			return new InventoryName_1_16_R3(player, targetPlayer);
		}
		
		return null;
	}

}

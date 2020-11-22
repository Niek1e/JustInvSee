package me.Niek1e.JustInvSee;

import org.bukkit.plugin.Plugin;

public enum MessageType {

	NO_PERMISSION("needop"), PLAYERS_ONLY("onlyingame"), USAGE_INV("usage"), USAGE_ENDERINV("enderusage"),
	USAGE_ARMORINV("armorusage"), USAGE_JUSTINVSEE("justinvseeusage"), PLAYER_NOT_FOUND("playernotonline"),
	LOOKED_IN_INVENTORY("lookedinyourinv"), SETTING_CHANGED("messagetoggled");

	private MessageType(String configCode) {
		this.configCode = configCode;
	}

	private String configCode;

	public String getTextMessage(Plugin plugin) {
		String language = plugin.getConfig().getString("language");
		String content = plugin.getConfig().getString("lang." + language + "." + this.configCode);
		return content;
	}

	public static MessageType getMessageTypeByCommand(String commandName) {
		switch (commandName) {

		case "inv":
			return MessageType.USAGE_INV;
		case "armorinv":
			return MessageType.USAGE_ARMORINV;
		case "enderinv":
			return MessageType.USAGE_ENDERINV;
		case "justinvsee":
			return MessageType.USAGE_JUSTINVSEE;

		}
		
		return MessageType.USAGE_JUSTINVSEE;
	}

}

package me.niek1e.justinvsee.message;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.niek1e.justinvsee.JustInvSee;

public class Message {

	/*
	 * JustInvSee v8.0, a Bukkit plugin to view a player's inventory Copyright (C)
	 * 2020 Niek1e
	 * 
	 * This program is free software; you can redistribute it and/or modify it under
	 * the terms of the GNU General Public License as published by the Free Software
	 * Foundation; either version 2 of the License, or (at your option) any later
	 * version.
	 * 
	 * This program is distributed in the hope that it will be useful, but WITHOUT
	 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
	 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
	 * details.
	 * 
	 * You should have received a copy of the GNU General Public License along with
	 * this program; if not, write to the Free Software Foundation, Inc., 51
	 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
	 * 
	 */

	protected static final String PREFIX = "[JustInvSee]";

	protected JustInvSee main;
	protected String content;

	public Message(JustInvSee main, MessageType messageType) {
		this.main = main;
		this.content = constructMessage(messageType);
	}
	
	public Message(JustInvSee main, MessageType messageType, String additionalInformation) {
		this.main = main;
		this.content = constructMessage(messageType, additionalInformation);
	}

	private String constructMessage(MessageType messageType) {
		String content = messageType.getTextMessage(this.main);
		return ChatColor.WHITE + PREFIX + " " + ChatColor.RED + content;
	}

	private String constructMessage(MessageType messageType, String additionalInformation) {
		String content = messageType.getTextMessage(this.main);
		String message = null;

		switch (messageType) {

		case SETTING_CHANGED:
			message = ChatColor.WHITE + PREFIX + " " + content + " " + additionalInformation;
			break;
		case LOOKED_IN_INVENTORY:
			message = ChatColor.WHITE + PREFIX + " " + ChatColor.GOLD + additionalInformation + " "
					+ ChatColor.WHITE + content;
			break;
		case PLAYER_LEFT:
			message = ChatColor.WHITE + PREFIX + " " + ChatColor.GOLD + additionalInformation + " "
					+ ChatColor.WHITE + content;
			break;
		default:
			message = ChatColor.WHITE + PREFIX + " " + content;
			break;

		}

		return message;

	}

	public void doSend(Player receiver) {
		receiver.sendMessage(this.content);
	}

	public void doSend(CommandSender receiver) {
		receiver.sendMessage(this.content);
	}
}

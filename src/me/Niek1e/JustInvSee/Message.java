package me.Niek1e.JustInvSee;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {
	
	/*
	 * JustInvSee v8.0, a Bukkit plugin to view a player's inventory
	 * Copyright (C) 2020 Niek1e
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

	protected final String PREFIX = "[JustInvSee]";

	protected JustInvSee main;
	protected String content;

	public Message(JustInvSee main, String content) {
		this.main = main;
		this.content = constructMessage(content);
	}

	public Message(JustInvSee main, String content, Player target) {
		this.main = main;
		this.content = constructMessage(content, target);
	}

	public Message(JustInvSee main, String content, boolean newValue) {
		this.main = main;
		this.content = constructMessage(content, newValue);
	}

	private String constructMessage(String content) {
		String text = this.main.getConfig()
				.getString("lang." + this.main.getConfig().getString("language") + "." + content);
		String message = ChatColor.WHITE + "[JustInvSee]" + " " + ChatColor.RED + text;
		return message;
	}

	private String constructMessage(String content, Player target) {
		String text = this.main.getConfig()
				.getString("lang." + this.main.getConfig().getString("language") + "." + content);
		String message = ChatColor.WHITE + "[JustInvSee]" + " " + ChatColor.GOLD + target.getName() + " "
				+ ChatColor.WHITE + text;
		return message;
	}

	private String constructMessage(String content, boolean newValue) {
		String text = this.main.getConfig()
				.getString("lang." + this.main.getConfig().getString("language") + "." + content);
		String message = ChatColor.WHITE + "[JustInvSee]" + " " + ChatColor.WHITE + text + " "
				+ String.valueOf(newValue);
		return message;
	}

	public void doSend(Player receiver) {
		receiver.sendMessage(this.content);
	}

	public void doSend(CommandSender receiver) {
		receiver.sendMessage(this.content);
	}
}

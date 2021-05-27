package me.niek1e.justinvsee;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.niek1e.justinvsee.commands.ArmorInv;
import me.niek1e.justinvsee.commands.EnderInv;
import me.niek1e.justinvsee.commands.Inv;

public class JustInvSee extends JavaPlugin implements Listener {

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

	private JustInventoryManager justInventoryManager;
	private EffectsManager effectsManager;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new JustInventoryEvents(this), (Plugin) this);
		this.getCommand("inv").setExecutor(new Inv(this));
		this.getCommand("armorinv").setExecutor(new ArmorInv(this));
		this.getCommand("enderinv").setExecutor(new EnderInv(this));
		this.getCommand("justinvsee").setExecutor(new me.niek1e.justinvsee.commands.JustInvSee(this));
		justInventoryManager = new JustInventoryManager(this);
		effectsManager = new EffectsManager();
	}

	@Override
	public void onDisable() {
		justInventoryManager.closeAll();
	}

	public boolean toggleMessages() {
		boolean value = getMessageStatus();
		getConfig().set("opmessage", Boolean.valueOf(!value));
		saveConfig();
		return !value;
	}

	public boolean getMessageStatus() {
		return getConfig().getBoolean("opmessage");
	}

	public JustInventoryManager getJustInventoryManager() {
		return justInventoryManager;
	}

	public EffectsManager getEffectsManager() {
		return effectsManager;
	}

}

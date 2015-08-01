package me.Niek1e.JustInvSee;

import java.io.IOException;

import org.bukkit.Effect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class JustInvSee extends JavaPlugin {

	public String prefix = "�f[�bJustInvSee�f] ";
	
	
//	One line to give the program's name and a brief idea of what it does.
//	Copyright (C) 2015 Niek1e
//
//	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License.
//
//	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//	
//	You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
//	You can contact Niek1e by email: 'prinscou@gmail.com'

	@Override
	public void onEnable() {
		saveDefaultConfig();

		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
			// Failed to submit the stats :-(
			this.getLogger().warning("Couldn't submit stats to Metrics!");
		}

		@SuppressWarnings("unused")
		Updater updater = new Updater(this, 66804, getFile(), UpdateType.DEFAULT, true);
	}

	@Override
	public void onDisable() {
		saveDefaultConfig();
	}


	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (!(sender instanceof Player)) {
			sender.sendMessage(prefix + Message.get("onlyingame", this, player));
		} else {

			if (label.equalsIgnoreCase("inv")) {
				if (args.length == 1) {
					if (player.hasPermission("justinvsee.inv")) {
						if (player.getServer().getPlayer(args[0]) != null) {
							Player tplayer = player.getServer().getPlayer(args[0]);
							Inventory tinv = tplayer.getInventory();
							player.openInventory(tinv);
							player.playEffect(tplayer.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
							if (tplayer.isOp()) {
								tplayer.sendMessage(prefix + Message.get("lookedinyourinv", this, player));
							}
						} else {
							player.sendMessage(prefix + Message.get("playernotonline", this, player));
						}
					} else {
						player.sendMessage(prefix + Message.get("needop", this, player));
					}
				} else {
					player.sendMessage(prefix + Message.get("usage", this, player));
				}
			} else if (label.equalsIgnoreCase("enderinv")) {
				if (args.length == 1) {
					if (player.hasPermission("justinvsee.enderinv")) {
						if (player.getServer().getPlayer(args[0]) != null) {
							Player tplayer = player.getServer().getPlayer(args[0]);
							Inventory tinv = tplayer.getEnderChest();
							player.openInventory(tinv);
							if (tplayer.isOp()) {
								tplayer.sendMessage(prefix + Message.get("lookedinyourinv", this, player));
							} else {
							}
						} else {
							player.sendMessage(prefix + Message.get("playernotonline", this, player));
						}
					} else {
						player.sendMessage(prefix + Message.get("needop", this, player));
					}
				} else {
					player.sendMessage(prefix + Message.get("enderusage", this, player));
				}
			}
		}
		return false;
	}
}
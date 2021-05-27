package me.niek1e.justinvsee.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.niek1e.justinvsee.JustInvSee;
import me.niek1e.justinvsee.message.Message;
import me.niek1e.justinvsee.message.MessageType;

public abstract class JustInventoryCommands implements CommandExecutor {

	protected JustInvSee plugin;

	public JustInventoryCommands(JustInvSee plugin) {
		this.plugin = plugin;
	}

	protected boolean isValidPlayer(CommandSender sender, Command command, String[] args) {
		if (!(sender instanceof Player)) {
			Message exception = new Message(plugin, MessageType.PLAYERS_ONLY);
			exception.doSend(sender);
			return false;
		}
		if (!sender.hasPermission("justinvsee." + command.getName())) {
			Message exception = new Message(plugin, MessageType.NO_PERMISSION);
			exception.doSend(sender);
			return false;
		}
		if (args.length != 1) {
			MessageType messageType = MessageType.getMessageTypeByCommand(command.getName());
			Message exception = new Message(plugin, messageType);
			exception.doSend(sender);
			return false;
		}
		if (getTargetPlayer(sender, args[0]) == null) {
			Message exception = new Message(plugin, MessageType.PLAYER_NOT_FOUND);
			exception.doSend(sender);
			return false;
		}
		return true;
	}

	protected boolean isValidCommand(CommandSender sender, String[] args) {

		if (!sender.hasPermission("justinvsee.changeconfig")) {
			Message exception = new Message(plugin, MessageType.NO_PERMISSION);
			exception.doSend(sender);
			return false;
		}

		if (args.length != 1) {
			Message exception = new Message(plugin, MessageType.USAGE_JUSTINVSEE);
			exception.doSend(sender);
			return false;
		}

		return true;

	}

	protected Player getTargetPlayer(CommandSender sender, String name) {
		return sender.getServer().getPlayer(name);
	}

}

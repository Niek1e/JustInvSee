package me.niek1e.justinvsee.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.niek1e.justinvsee.message.Message;
import me.niek1e.justinvsee.message.MessageType;

public class JustInvSee extends JustInventoryCommands {

	public JustInvSee(me.niek1e.justinvsee.JustInvSee plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!isValidCommand(sender, args))
			return true;

		if (args[0].equalsIgnoreCase("togglemsg")) {
			boolean setValue = plugin.toggleMessages();
			Message exception = new Message(plugin, MessageType.SETTING_CHANGED, String.valueOf(setValue));
			exception.doSend(sender);
			return true;
		} else {
			Message exception = new Message(plugin, MessageType.USAGE_JUSTINVSEE);
			exception.doSend(sender);
			return true;
		}
	}

}

package me.niek1e.justinvsee.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.niek1e.justinvsee.JustInvSee;
import me.niek1e.justinvsee.justinventory.JustPlayerInventory;
import me.niek1e.justinvsee.message.Message;
import me.niek1e.justinvsee.message.MessageType;

public class Inv extends JustInventoryCommands {

	public Inv(JustInvSee plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Process the /inv <player> command.

		// Check if the sender, syntax, permissions and usage are correct.
		if (!isValidPlayer(sender, command, args))
			return true;

		Player player = (Player) sender;
		Player target = getTargetPlayer(sender, args[0]);
		
		// You currently can't open your own inventory to prevent loss of items.
		if (player.equals(target)) {
			Message notification = new Message(plugin, MessageType.INVALID_TARGET);
			notification.doSend(player);
			return true;
		}

		// Send the target a notification if they have the right permission and these
		// notifications are enabled.
		if (target.hasPermission("justinvsee." + label) && plugin.getMessageStatus()) {
			Message notification = new Message(plugin, MessageType.LOOKED_IN_INVENTORY, player.getName());
			notification.doSend(target);
		}

		// Check if there already is a JustPlayerInventory view for the target player's
		// inventory
		JustPlayerInventory targetJustInventory = plugin.getJustInventoryManager().getByTarget(target);
		targetJustInventory.openInventory(player, plugin.getEffectsManager());
		
		return true;
	}

}

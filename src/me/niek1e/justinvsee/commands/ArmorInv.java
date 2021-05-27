package me.niek1e.justinvsee.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.niek1e.justinvsee.JustInvSee;
import me.niek1e.justinvsee.justinventory.JustArmorInventory;
import me.niek1e.justinvsee.message.Message;
import me.niek1e.justinvsee.message.MessageType;

public class ArmorInv extends JustInventoryCommands {

	public ArmorInv(JustInvSee plugin) {
		super(plugin);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Process the /armorinv <player> command.

		// Check if the sender, syntax, permissions and usage are correct.
		if (!isValidPlayer(sender, command, args))
			return true;

		Player player = (Player) sender;
		Player target = getTargetPlayer(sender, args[0]);

		// Send the target a notification if they have the right permission and these
		// notifications are enabled.
		if (target.hasPermission("justinvsee." + label) && plugin.getMessageStatus()) {
			Message notification = new Message(plugin, MessageType.LOOKED_IN_INVENTORY, player.getName());
			notification.doSend(target);
		}

		// Check if there already is a JustPlayerInventory view for the target player's
		// inventory
		JustArmorInventory targetJustInventory = plugin.getJustInventoryManager().getArmorByTarget(target);
		targetJustInventory.openInventory(player, plugin.getEffectsManager());
		
		return true;
	}

}

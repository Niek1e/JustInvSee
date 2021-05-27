package me.niek1e.justinvsee;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public class EffectsManager {

	public void playEnderchestEffects(Player player) {
		createEnderChestEffects(player);
	}

	public void playPlayerEffects(Player player, Player targetPlayer) {
		Location location = targetPlayer.getLocation().add(0, 0.5, 0);
		playPlayerEffects(player, location);
	}

	private void createEnderChestEffects(Player player) {
		Location playerLocation = player.getLocation();
		Chunk[] chunks = getSurroundingChunks(playerLocation);
		ArrayList<Location> enderChests = getEnderchestLocations(chunks);

		for (int i = 0; i < enderChests.size(); i++) {
			player.playEffect(enderChests.get(i), Effect.MOBSPAWNER_FLAMES, null);
		}
	}

	private void playPlayerEffects(Player player, Location location) {
		player.playEffect(location, Effect.MOBSPAWNER_FLAMES, null);
	}

	private Chunk[] getSurroundingChunks(Location location) {
		int chunkX = location.getChunk().getX();
		int chunkZ = location.getChunk().getZ();
		Chunk[] chunks = new Chunk[9];
		chunks[0] = location.getWorld().getChunkAt(chunkX - 1, chunkZ - 1);
		int i = 0;

		for (int x = -1; x < 2; x++) {
			for (int z = -1; z < 2; z++) {
				chunks[i] = location.getWorld().getChunkAt(chunkX - x, chunkZ - z);
				i++;
			}
		}

		return chunks;
	}

	private ArrayList<Location> getEnderchestLocations(Chunk[] chunks) {
		ArrayList<Location> enderchestLocations = new ArrayList<>();

		for (int i = 0; i < chunks.length; i++) {
			BlockState[] tileEntities = chunks[i].getTileEntities();

			for (int j = 0; j < tileEntities.length; j++) {
				if (tileEntities[j].getType().equals(Material.ENDER_CHEST)) {
					Location location = tileEntities[j].getLocation().add(0, 0.5, 0);
					enderchestLocations.add(location);
				}
			}

		}

		return enderchestLocations;
	}

}

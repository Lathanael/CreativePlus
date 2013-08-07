/*************************************************************************
 * Copyright (C) 2012 Philippe Leipold
 *
 * This file is part of CreativePlus.
 *
 * CreativePlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CreativePlus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CreativePlus. If not, see <http://www.gnu.org/licenses/>.
 *
 **************************************************************************/

package de.Lathanael.CP.Listener;

import java.io.EOFException;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import be.Balor.Manager.LocaleManager;
import be.Balor.Manager.Permissions.PermissionManager;
import de.Lathanael.CP.CreativePlus.CPConfigEnum;
import de.Lathanael.CP.CreativePlus.CreativePlus;
import de.Lathanael.CP.Protect.ChunkBlockLocation;
import de.Lathanael.CP.Protect.ChunkFiles;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CPProtectBlockListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!CreativePlus.worlds.contains(player.getWorld().getName()))
			return;
		if (player.getGameMode() != GameMode.CREATIVE)
			return;
		Block b = event.getBlock();
		int chunkX = b.getChunk().getX();
		int chunkZ = b.getChunk().getZ();
		ChunkBlockLocation cbl = new ChunkBlockLocation(b.getX(), b.getY(), b.getZ(), chunkX, chunkZ);
		ChunkFiles.add(chunkX, chunkZ, cbl);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!CreativePlus.worlds.contains(player.getWorld().getName()))
			return;
		if (player.getGameMode() == GameMode.CREATIVE)
			return;
		Block b = event.getBlock();
		int chunkX = b.getChunk().getX();
		int chunkZ = b.getChunk().getZ();
		ChunkBlockLocation cbl = new ChunkBlockLocation(b.getX(), b.getY(), b.getZ(), chunkX, chunkZ);
		try {
			if (ChunkFiles.isProtected(chunkX, chunkZ, cbl) && !PermissionManager.hasPerm(event.getPlayer(), "creativeplus.breakproteced", false)) {
				event.setCancelled(true);
				LocaleManager.sI18n(player, "ProtectedBlock");
			}
		} catch (EOFException e) {
				CreativePlus.log.warning("Database is most probably corrupted.");
				CreativePlus.log.warning(e.getMessage());
				if (CPConfigEnum.VERBOSE.getBoolean())
					e.printStackTrace();
				CreativePlus.log.warning("Protecting all blocks in the affected area to prevent loss of data!");
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "All blocks in this chunk are proteced due to a corrupted datatbase, please inform a server admin!");
		}
	}
}

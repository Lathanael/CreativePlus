/************************************************************************
 * This file is part of CreativePlus.
 *
 * CreativePlus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ExamplePlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CreativePlus.  If not, see <http://www.gnu.org/licenses/>.
 ************************************************************************/

package de.Lathanael.CP.Listeners;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import be.Balor.Manager.Permissions.PermissionManager;
import be.Balor.Tools.Utils;

import de.Lathanael.CP.CreativePlus.CreativePlus;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CPBlockListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		if (!CreativePlus.worlds.contains(player.getWorld().getName()))
			return;
		if (player.getGameMode() != GameMode.CREATIVE)
			return;
		int blockID = event.getBlock().getTypeId();
		if (!CreativePlus.blPlace.contains(blockID))
			return;
		if (PermissionManager.hasPerm(player, "creativeplus.placebl", false))
			return;
		event.setCancelled(true);
		HashMap<String, String> replace = new HashMap<String, String>();
		replace.put("block", Material.getMaterial(blockID).toString());
		Utils.sI18n(player, "blacklisted", replace);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		if (!CreativePlus.worlds.contains(player.getWorld().getName()))
			return;
		if (player.getGameMode() != GameMode.CREATIVE)
			return;
		int blockID = event.getBlock().getTypeId();
		if (!CreativePlus.blBreak.contains(blockID))
			return;
		if (PermissionManager.hasPerm(player, "creativeplus.breakbl", false))
			return;
		event.setCancelled(true);
	}
}

/*************************************************************************
 * Copyright (C) 2012 Philippe Leipold
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

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import be.Balor.Manager.Permissions.PermissionManager;

import de.Lathanael.CP.CreativePlus.CreativePlus;
import de.Lathanael.CP.Inventory.InventoryHandler;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CPInventoryListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		if (!CreativePlus.worlds.contains(player.getWorld().getName()))
			return;
		if (PermissionManager.hasPerm(player, "creativeplus.sharedinv", false))
			return;
		if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
			InventoryHandler.getInstance().saveInventory(player, "survival");
			InventoryHandler.getInstance().clearInventory(player);
			InventoryHandler.getInstance().loadInventory(player, "creative");
		} else {
			InventoryHandler.getInstance().saveInventory(player, "creative");
			InventoryHandler.getInstance().clearInventory(player);
			InventoryHandler.getInstance().loadInventory(player, "survival");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!CreativePlus.worlds.contains(player.getWorld().getName()))
			return;
		if (PermissionManager.hasPerm(player, "creativeplus.sharedinv", false))
			return;
		InventoryHandler.getInstance().createPlayerFiles(player.getName());
		if (player.getGameMode().equals(GameMode.CREATIVE)) {
			InventoryHandler.getInstance().clearInventory(player);
			InventoryHandler.getInstance().loadInventory(player, "creative");
		} else {
			InventoryHandler.getInstance().clearInventory(player);
			InventoryHandler.getInstance().loadInventory(player, "survival");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (!CreativePlus.worlds.contains(player.getWorld().getName()))
			return;
		if (PermissionManager.hasPerm(player, "creativeplus.sharedinv", false))
			return;
		if (player.getGameMode().equals(GameMode.CREATIVE))
			InventoryHandler.getInstance().saveInventory(player, "creative");
		else
			InventoryHandler.getInstance().saveInventory(player, "survival");
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event) {
		Player player = event.getPlayer();
		if (!CreativePlus.worlds.contains(player.getWorld().getName()))
			return;
		if (PermissionManager.hasPerm(player, "creativeplus.sharedinv", false))
			return;
		if (player.getGameMode().equals(GameMode.CREATIVE))
			InventoryHandler.getInstance().saveInventory(player, "creative");
		else
			InventoryHandler.getInstance().saveInventory(player, "survival");
	}
}

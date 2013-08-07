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

package de.Lathanael.CP.Inventory;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class InventoryHandler {

	public HashMap<Player, Inventory> inventories = new HashMap<Player, Inventory>();
	private File directory;
	private static InventoryHandler instance;

	public static InventoryHandler getInstance() {
		if (instance == null)
			instance = new InventoryHandler();
		return instance;
	}

	public void initInvHandler(File file) {
		directory = new File(file, "PlayerInventories");
		if (!directory.exists())
			directory.mkdirs();
	}

	public void createPlayerFiles(String playerName) {
		File playerFolder = new File(directory.getPath() + File.separator + playerName);
		if (!playerFolder.exists())
			playerFolder.mkdirs();
		File creative = new File(directory.getPath() + File.separator + playerName, "creative.inv");
		File survival = new File(directory.getPath() + File.separator + playerName, "survival.inv");
		try {
			if (!creative.exists())
				creative.createNewFile();
			if (!survival.exists())
				survival.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveInventory(Player player, String gameMode) {
		CPInventory inv = Converter.serializeInventory(player.getInventory().getContents());
		savePlayerFile(player.getName(),gameMode, inv);
	}

	public void clearInventory(Player player) {
		player.getInventory().clear();
	}

	public void loadInventory(Player player, String gameMode) {
		CPInventory inv = readPlayerFile(player.getName(), gameMode);
		ItemStack[] stack = Converter.deSerializeInventory(inv);
		int i = 0;
		for (ItemStack item : stack) {
			player.getInventory().setItem(i, item);
			i++;
		}
	}

	public CPInventory readPlayerFile(String playerName, String gameMode) {
		File playerFile = new File(directory.getPath() + File.separator + playerName, gameMode + ".inv");
		FileInputStream fis = null;
		ObjectInputStream oit = null;
		CPInventory inv = null;
		try {
			fis = new FileInputStream(playerFile);
			oit = new ObjectInputStream(fis);
			inv = (CPInventory) oit.readObject();
			oit.close();
		} catch (EOFException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return inv;
	}

	public void savePlayerFile(String playerName, String gameMode, CPInventory inv) {
		File playerFile = new File(directory.getPath() + File.separator + playerName, gameMode + ".inv");
		if (playerFile.exists())
			playerFile.delete();
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(playerFile);
			out = new ObjectOutputStream(fos);
			out.writeObject(inv);
			out.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

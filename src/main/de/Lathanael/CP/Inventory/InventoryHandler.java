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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.Lathanael.CP.CreativePlus.CreativePlus;


/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class InventoryHandler {

	public HashMap<Player, Inventory> inventories = new HashMap<Player, Inventory>();
	private File directory;
	private FilenameFilter ymlFilter = new YamlFileFilter();
	private final Set<String> filePlayers = new HashSet<String>();
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
		loadPlayerFiles();
	}

	public void saveInventory(Player player) {
		checkFile(player.getName());
		CPInventory inv = Converter.serializeInventory(player.getInventory().getContents());
		savePlayerFile(player.getName(), inv);
		player.getInventory().clear();
	}

	public void loadInventory(Player player) {
		CPInventory inv = readPlayerFile(player.getName());
		ItemStack[] stack = Converter.deSerializeInventory(inv);
		int i = 0;
		for (ItemStack item : stack) {
			player.getInventory().setItem(i, item);
			i++;
		}
	}

	public void checkFile(String playerName) {
		if (!filePlayers.contains(playerName))
			createNewPlayerFile(playerName);
	}

	public void createNewPlayerFile(String playerName) {
		File playerFile = new File(directory.getPath(), playerName + ".yml");
		try {
			playerFile.createNewFile();
		} catch (IOException e) {
			CreativePlus.log.info("Error while creating new PlayerFile for: " + playerName);
			e.printStackTrace();
		}
		filePlayers.add(playerName);
	}

	public void loadPlayerFiles() {
		File[] files = directory.listFiles(ymlFilter);
		for (File file : files) {
			String name = file.getName();
			filePlayers.add(name.substring(0, name.lastIndexOf('.')));
		}
	}

	public CPInventory readPlayerFile(String playerName) {
		File playerFile = new File(directory.getPath(), playerName + ".inv");
		FileInputStream fis = null;
		ObjectInputStream oit = null;
		CPInventory inv = null;
		try {
			fis = new FileInputStream(playerFile);
			oit = new ObjectInputStream(fis);
			inv = (CPInventory) oit.readObject();
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return inv;
	}

	public void savePlayerFile(String playerName, CPInventory inv) {
		File playerFile = new File(directory.getPath(), playerName + ".inv");
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
	/*
	 * Gets all files with the extension '.yml'
	 */
	private class YamlFileFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			if (name.endsWith(".yml")) {
				return true;
			}
			else {
				return false;
			}
		}
	}
}

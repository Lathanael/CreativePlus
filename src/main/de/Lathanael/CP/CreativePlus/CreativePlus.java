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

package de.Lathanael.CP.CreativePlus;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import de.Lathanael.CP.Inventory.InventoryHandler;
import de.Lathanael.CP.Listeners.CPBlockListener;
import de.Lathanael.CP.Listeners.CPInventoryListener;
import de.Lathanael.CP.Listeners.CPPlayerListener;

import be.Balor.Manager.Permissions.PermParent;
import be.Balor.Tools.Utils;
import be.Balor.Tools.Configuration.File.ExtendedConfiguration;
import be.Balor.bukkit.AdminCmd.ACPluginManager;
import be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CreativePlus extends AbstractAdminCmdPlugin{

	private ExtendedConfiguration config;
	public static List<String> worlds;
	public static List<Integer> blBreak;
	public static List<Integer> blPlace;
	public static boolean sepInv;
	public static Logger log;

	public CreativePlus() {
		super("CreativePlus");
	}

	@Override
	public void onEnable() {
		super.onEnable();
		PluginDescriptionFile pdfFile = this.getDescription();
		log = ACPluginManager.getPluginInstance("CreativePlus").getLogger();
		config = ExtendedConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
		CPConfigEnum.setPluginInfos(pdfFile);
		CPConfigEnum.setPluginConfig(config);
		config.options().copyDefaults(true).header(CPConfigEnum.getHeader());
		config.addDefaults(CPConfigEnum.getDefaultvalues());
		try {
			config.save();
		} catch (IOException e) {
			log.warning("Error while saving the config.yml!");
		}
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CPPlayerListener(), this);
		pm.registerEvents(new CPBlockListener(), this);
		permissionLinker.registerAllPermParent();
		worlds = CPConfigEnum.WROLDS.getStringList();
		blBreak = CPConfigEnum.BREAK_LIST.getIntList();
		blPlace = CPConfigEnum.PLACE_LIST.getIntList();
		sepInv = CPConfigEnum.SEP_INV.getBoolean();
		if (sepInv) {
			InventoryHandler.getInstance().initInvHandler(getDataFolder());
			pm.registerEvents(new CPInventoryListener(), this);
		}
		log.info("Enabled. (Version " + pdfFile.getVersion() + ")");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("Disabled. (Version " + pdfFile.getVersion() + ")");
	}

	@Override
	protected void registerPermParents() {
		PermParent major = new PermParent("creativeplus.*");
		permissionLinker.setMajorPerm(major);
		major.addChild("creativeplus.dropitems");
		major.addChild("creativeplus.breakbl");
		major.addChild("creativeplus.placebl");
		major.addChild("creativeplus.chest.allowed");
		major.addChild("creativeplus.sharedinv");
	}

	@Override
	public void registerCmds() {
	}

	@Override
	protected void setDefaultLocale() {
		Utils.addLocale("NoItemsDrop", ChatColor.RED + "You are not allowed to drop items/blocks while beeing in creative mode!");
		Utils.addLocale("blacklisted", ChatColor.GOLD + "%block " + ChatColor.RED
				+ "is blacklisted and you are not allowed to place it");
	}
}
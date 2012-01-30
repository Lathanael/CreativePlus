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

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import de.Lathanael.CP.Listeners.CPBlockListener;
import de.Lathanael.CP.Listeners.CPPlayerListener;

import be.Balor.Manager.Permissions.PermParent;
import be.Balor.Tools.Utils;
import be.Balor.Tools.Debug.ACLogger;
import be.Balor.bukkit.AdminCmd.AbstractAdminCmdPlugin;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CreativePlus extends AbstractAdminCmdPlugin{

	private Configuration config;
	public static List<String> worlds;
	public static List<Integer> blBreak;
	public static List<Integer> blPlace;

	public CreativePlus() {
		super("CreativePlus");
	}

	@Override
	public void onEnable() {
		super.onEnable();
		config = Configuration.getInstance();
		config.setInstance(this);
		final PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new CPPlayerListener(), this);
		pm.registerEvents(new CPBlockListener(), this);
		PluginDescriptionFile pdfFile = this.getDescription();
		permissionLinker.registerAllPermParent();
		worlds = config.getConfStringList("CreativeWorlds");
		blBreak = config.getConfIntList("BlockBreakBlacklist");
		blPlace = config.getConfIntList("BlockPlaceBlacklist");
		ACLogger.info("[" + pdfFile.getName() +"] Enabled. (Version " + pdfFile.getVersion() + ")");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		ACLogger.info("[" + pdfFile.getName() +"] Disabled. (Version " + pdfFile.getVersion() + ")");
	}

	@Override
	protected void registerPermParents() {
		permissionLinker.addPermParent(new PermParent("admincmd.creativeplus.*"));
		permissionLinker.setMajorPerm(new PermParent("admincmd.*"));
		permissionLinker.addPermChild("admincmd.creativeplus.dropitems");
		permissionLinker.addPermChild("admincmd.creativeplus.breakbl");
		permissionLinker.addPermChild("admincmd.creativeplus.placebl");
		permissionLinker.addPermChild("admincmd.creativeplus.chest.allowed");
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

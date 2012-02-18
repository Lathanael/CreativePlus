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

import java.io.Serializable;

import org.bukkit.enchantments.Enchantment;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CPEnchantment implements Serializable {

	private static final long serialVersionUID = -2008379441051076337L;

	private int id, level;

	public CPEnchantment(Enchantment e){
		this.id = e.getId();
		this.level = e.getMaxLevel();
	}

	public int getId() {
		return id;
	}

	public int getMaxLevel() {
		return level;
	}
}

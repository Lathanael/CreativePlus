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
import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CPItemStack implements Serializable {

	private static final long serialVersionUID = 6833325765899849608L;
	private int id, amount;
	private short durability;
	private byte data;
	private HashMap<CPEnchantment, Integer> enchantments = new HashMap<CPEnchantment, Integer>();

	public CPItemStack(int id, short durability, byte data, int amount, Map<Enchantment, Integer> enchantments) {
		this.id = id;
		this.durability = durability;
		this.data = data;
		this.amount = amount;
		if (enchantments != null && enchantments.size() > 0 && !enchantments.isEmpty()) {
			for (Map.Entry<Enchantment, Integer> entries : enchantments.entrySet()) {
				if (entries.getKey() != null) {
					this.enchantments.put(new CPEnchantment(entries.getKey()), entries.getValue());
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public short getDurability() {
		return durability;
	}

	public byte getData() {
		return data;
	}

	public int getAmount() {
		return amount;
	}

	public Map<CPEnchantment, Integer> getEnchantments() {
		return enchantments;
	}
}
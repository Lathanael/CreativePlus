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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class Converter {

	public static CPInventory serializeInventory(ItemStack[] stack) {
		return new CPInventory(stack);
	}

	public static ItemStack[] deSerializeInventory(CPInventory stack) {
		ItemStack[] inventory = new ItemStack[36];
		CPItemStack[] itemStack = stack.getInventory();
		for (CPItemStack item : itemStack) {
			ItemStack is = new ItemStack(item.getId(), item.getAmount());
			is.getData().setData(item.getData());
			is.setDurability(item.getDurability());
			Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
			Map<CPEnchantment, Integer> enchants = item.getEnchantments();
			if (enchants != null && enchants.size() > 0 && !enchants.isEmpty()) {
				for (Map.Entry<CPEnchantment, Integer> entries  : item.getEnchantments().entrySet()) {
					if (entries.getKey() != null) {
						Enchantment enchantment = Enchantment.getById(entries.getKey().getId());
						enchantments.put(enchantment, entries.getValue());
					}
				}
				is.addEnchantments(enchantments);
			}
		}
		return inventory;
	}
}

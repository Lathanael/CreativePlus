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

import org.bukkit.inventory.ItemStack;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CPInventory implements Serializable {

	private static final long serialVersionUID = 6465964968815277273L;

	private CPItemStack[] inventory = new CPItemStack[36];

	public CPInventory(ItemStack[] inventory) {
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null && inventory[i].getAmount() != 0 && inventory[i].getTypeId() != 0) {
				ItemStack stack = inventory[i];
				this.inventory[i] = new CPItemStack(stack.getTypeId(), stack.getDurability(), stack.getData().getData(), stack.getAmount(), stack.getEnchantments());
			} else {
				continue;
			}
		}
	}

	public CPItemStack[] getInventory() {
		return inventory;
	}
}

/*************************************************************************
 * Copyright (C) 2012 Philippe Leipold
 *
 * This file is part of CreativePlus.
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

package de.Lathanael.CP.Protect;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CBLSet implements Serializable {

	private HashSet<ChunkBlockLocation> blocks = new HashSet<ChunkBlockLocation>();
	private String key;

	private static final long serialVersionUID = -3877731953197835237L;

	public CBLSet(final HashSet<ChunkBlockLocation> blocks, final String key, boolean n) {
		this.key = key;
		this.blocks = blocks;
	}

	public CBLSet(final HashSet<ChunkBlockLocation> blocks, final String key) {
		this.blocks.addAll(blocks);
		this.key = key;
	}

	public HashSet<ChunkBlockLocation> getBlocks() {
		return blocks;
	}

	public String getKey() {
		return key;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() == getClass()) {
			if(((CBLSet) obj).key.equals(this.key) &&
					((CBLSet) obj).blocks.equals(this.blocks))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hc = 47;
		int mult = 73;
		hc = hc*mult + key.hashCode();
		hc = hc*mult + blocks.hashCode();
		return hc;
	}
}

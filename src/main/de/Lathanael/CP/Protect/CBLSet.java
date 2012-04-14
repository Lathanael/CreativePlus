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
import java.util.TreeSet;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class CBLSet implements Serializable {

	private TreeSet<ChunkBlockLocation> blocks = new TreeSet<ChunkBlockLocation>();
	private String key;

	private static final long serialVersionUID = -3877731953197835237L;

	public CBLSet (TreeSet<ChunkBlockLocation> blocks, String key) {
		this.blocks = blocks;
		this.key = key;
	}

	public TreeSet<ChunkBlockLocation> getBlocks() {
		return blocks;
	}

	public String getKey() {
		return key;
	}

/*	@Override
	public int compareTo(CBLSet o) {
		if (o.getKey() == null && this.getKey() == null) {
			return 0;
		}
		if (this.getKey() == null) {
			return 1;
		}
		if (o.getKey() == null) {
			return -1;
		}
		return this.getKey().compareTo(o.getKey());
	}*/
}

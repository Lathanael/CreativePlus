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

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class ChunkBlockLocation implements Serializable {

	private static final long serialVersionUID = -7011023961320643404L;
	private int x, y, z;
	private int chunkX, chunkZ;

	public ChunkBlockLocation(int x, int y, int z, int chunkX, int chunkZ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkZ() {
		return chunkZ;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (obj.getClass() == getClass()) {
			if(((ChunkBlockLocation) obj).x == this.x
					&& ((ChunkBlockLocation) obj).y == this.y
					&& ((ChunkBlockLocation) obj).z == this.z
					&& ((ChunkBlockLocation) obj).chunkX == this.chunkX
					&& ((ChunkBlockLocation) obj).chunkZ == this.chunkZ)
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hc = 87;
		int mult = 47;
		hc = hc*mult + x;
		hc = hc*mult + y;
		hc = hc*mult + z;
		hc = hc*mult + chunkX;
		hc = hc*mult + chunkZ;
		return hc;
	}
}

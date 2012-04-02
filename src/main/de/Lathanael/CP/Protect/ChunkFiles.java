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

import java.io.File;
import java.util.List;

import de.Lathanael.BinaryFileDB.API.SubDirFileFilter;
import de.Lathanael.BinaryFileDB.API.SubDirFileFilter.Type;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class ChunkFiles {

	private static boolean loaded = false;
	private static SubDirFileFilter filter = new SubDirFileFilter();

	public static void initFiles(String path, String extension) {
		loaded = true;
		List<File> files= filter.getFiles(new File(path), filter.new PatternFilter(Type.ALL, extension), true);
	}

	public static void closeFiles() {
		if (!loaded)
			return;
	}

	public static void add(int chunkX, int chunkZ, ChunkBlockLocation cbl) {

	}

	public static boolean isProtected(int chunkX, int chunkZ, ChunkBlockLocation cbl) {
		if (cbl == null)
			return false;
		return true;
	}
}

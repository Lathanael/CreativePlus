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

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import de.Lathanael.BinaryFileDB.API.DBAccess;
import de.Lathanael.BinaryFileDB.API.RecordReader;
import de.Lathanael.BinaryFileDB.API.RecordWriter;
import de.Lathanael.BinaryFileDB.API.SubDirFileFilter;
import de.Lathanael.BinaryFileDB.API.SubDirFileFilter.Type;
import de.Lathanael.BinaryFileDB.Exception.CacheSizeException;
import de.Lathanael.BinaryFileDB.Exception.QueueException;
import de.Lathanael.BinaryFileDB.Exception.RecordsFileException;
import de.Lathanael.CP.CreativePlus.CPConfigEnum;
import de.Lathanael.CP.CreativePlus.CreativePlus;

/**
 * @author Lathanael (aka Philippe Leipold)
 *
 */
public class ChunkFiles {

	private static final String filePrefix = "chunks.";
	private static SubDirFileFilter filter = new SubDirFileFilter();
	private static String path;
	private static String extension;
	private static HashMap<String, File> existingFiles;
	private static final TreeMap<String, DBAccess> loadedDataBases = new TreeMap<String, DBAccess>();

	public static void initFiles(String path, String extension) {
		ChunkFiles.path = path;
		ChunkFiles.extension = extension;
		List<File> files= filter.getFiles(new File(path), filter.new PatternFilter(Type.FILE, extension), true);
		existingFiles = new HashMap<String, File>((int) (files.size()/0.75) + 1);
		String name;
		for (File file : files) {
			if (CPConfigEnum.VERBOSE.getBoolean()) {
				CreativePlus.log.info("Found database file: " + file.getName());
			}
			int lastDot = file.getName().lastIndexOf('.');
			if (0 < lastDot && lastDot <= file.length()) {
				name = file.getName().substring(0, lastDot);
				existingFiles.put(name, file);
				if (CPConfigEnum.VERBOSE.getBoolean()) {
					CreativePlus.log.info("Added file to list: " + name);
				}
			}
		}
	}

	public static void closeFiles() {
		for (Map.Entry<String, DBAccess> entry : loadedDataBases.entrySet())
			try {
				entry.getValue().closeDB(false);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RecordsFileException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				if (e.getMessage() != null )
					CreativePlus.log.severe(e.getMessage());
				else
					e.printStackTrace();
			}
	}

	public static void add(final int chunkX, final int chunkZ, final ChunkBlockLocation cbl) {
		if (cbl == null)
			return;
		final int fileExX = chunkX >> 3;
			final int fileExZ = chunkZ >> 3;
		final String newFileName = filePrefix + String.valueOf(fileExX) + "." + String.valueOf(fileExZ);
		final String newChunkName = "chunk." + String.valueOf(chunkX) + "." + String.valueOf(chunkZ);
		File file = new File(path, newChunkName + extension +"s");
		DBAccess database = null;
		try {
			if (loadedDataBases.containsKey(newFileName)) {
				if (CPConfigEnum.VERBOSE.getBoolean())
					CreativePlus.log.info("Writing to already loaded Database...");
				database = loadedDataBases.get(newFileName);
				if (database != null) {
					final RecordReader rr = database.getRecord(newChunkName);
					if (rr != null) {
						final Object o = rr.readObject();
						final CBLSet blocks = (CBLSet) o;
						blocks.getBlocks().add(cbl);
						final RecordWriter rw = new RecordWriter(newChunkName);
						rw.writeObject(blocks);
						database.writeRecord(rw);
						FileOutputStream fos = null;
						ObjectOutputStream out = null;
						try {
							fos = new FileOutputStream(file);
							out = new ObjectOutputStream(fos);
							out.writeObject(blocks);
							out.close();
						} catch(IOException e) {
							e.printStackTrace();
						}
						return;
					} else {
						final HashSet<ChunkBlockLocation> blocks = new HashSet<ChunkBlockLocation>();
						blocks.add(cbl);
						final CBLSet blockSet = new CBLSet(blocks, newChunkName);
						final RecordWriter rw = new RecordWriter(newChunkName);
						rw.writeObject(blockSet);
						database.writeRecord(rw);
						FileOutputStream fos = null;
						ObjectOutputStream out = null;
						try {
							fos = new FileOutputStream(file);
							out = new ObjectOutputStream(fos);
							out.writeObject(blocks);
							out.close();
						} catch(IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
			}
			if (CPConfigEnum.VERBOSE.getBoolean())
				CreativePlus.log.info("Database not loaded, check for existance..");
			if (existingFiles.keySet().contains(newFileName)) {
				if (CPConfigEnum.VERBOSE.getBoolean())
					CreativePlus.log.info("Database found, loading it...");
				database = new DBAccess(existingFiles.get(newFileName).getPath(), "rw", true, 5);
			} else {
				if (CPConfigEnum.VERBOSE.getBoolean())
					CreativePlus.log.info("No Database found, creating a new one...");
				database = new DBAccess(path + File.separator + newFileName + extension, 10, true, 5);
			}
			if (database != null) {
				final RecordReader rr = database.getRecord(newChunkName);
				if (rr != null) {
					final Object o = rr.readObject();
					final CBLSet blocks = (CBLSet) o;
					blocks.getBlocks().add(cbl);
					final RecordWriter rw = new RecordWriter(newChunkName);
					rw.writeObject(blocks);
					database.writeRecord(rw);
					FileOutputStream fos = null;
					ObjectOutputStream out = null;
					try {
						fos = new FileOutputStream(file);
						out = new ObjectOutputStream(fos);
						out.writeObject(blocks);
						out.close();
					} catch(IOException e) {
						e.printStackTrace();
					}
				} else {
					final HashSet<ChunkBlockLocation> blocks = new HashSet<ChunkBlockLocation>();
					blocks.add(cbl);
					final CBLSet blockSet = new CBLSet(blocks, newChunkName);
					final RecordWriter rw = new RecordWriter(newChunkName);
					rw.writeObject(blockSet);
					database.writeRecord(rw);
					FileOutputStream fos = null;
					ObjectOutputStream out = null;
					try {
						fos = new FileOutputStream(file);
						out = new ObjectOutputStream(fos);
						out.writeObject(blocks);
						out.close();
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
				loadedDataBases.put(newFileName, database);
				return;
			}
		} catch (IOException e) {
			if (e.getMessage() != null && e.getMessage().contains("large"))
				CreativePlus.log.severe(e.getMessage());
			else
				e.printStackTrace();
			return;
		} catch (RecordsFileException e) {
			CreativePlus.log.severe(e.getMessage());
			return;
		} catch (CacheSizeException e) {
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (QueueException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			if (e.getMessage() != null)
				CreativePlus.log.severe(e.getMessage());
			else
				e.printStackTrace();
		}
	}

	public static void remove(final int chunkX, final int chunkZ,  final ChunkBlockLocation cbl) {
		if (cbl == null)
			return;
		final int fileExX = chunkX >> 3;
		final int fileExZ = chunkZ >> 3;
		final String newFileName = filePrefix + String.valueOf(fileExX) + "." + String.valueOf(fileExZ);
		final String newChunkName = "chunk." + String.valueOf(chunkX) + "." + String.valueOf(chunkZ);
		DBAccess database = null;
		try {
			if (loadedDataBases.containsKey(newFileName)) {
				database = loadedDataBases.get(newFileName);
				if (database != null) {
					final RecordReader rr = database.getRecord(newChunkName);
					if (rr != null) {
						final Object o = rr.readObject();
						final CBLSet blocks = (CBLSet) o;
						// TODO: remove code
						final RecordWriter rw = new RecordWriter(newChunkName);
						rw.writeObject(blocks);
						database.writeRecord(rw);
						return;
					}
				}
			}
			if (existingFiles.keySet().contains(newFileName)) {
				database = new DBAccess(existingFiles.get(newFileName).getPath(), "rw", true, 5);
			}
			else
				database = new DBAccess(path + File.separator + newFileName + extension, 10, true, 5);
			if (database != null) {
				final RecordReader rr = database.getRecord(newChunkName);
				if (rr != null) {
					final CBLSet blocks = (CBLSet) rr.readObject();
					// TODO: remove code
					final RecordWriter rw = new RecordWriter(newChunkName);
					rw.writeObject(blocks);
					database.writeRecord(rw);
				}
				loadedDataBases.put(newFileName, database);
				return;
			}
		} catch (IOException e) {
			if (e.getMessage() != null && e.getMessage().contains("large"))
				CreativePlus.log.severe(e.getMessage());
			else
				e.printStackTrace();
			return;
		} catch (RecordsFileException e) {
			CreativePlus.log.severe(e.getMessage());
			return;
		} catch (CacheSizeException e) {
			e.printStackTrace();
			return;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (QueueException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			if (e.getMessage() != null)
				CreativePlus.log.severe(e.getMessage());
			else
				e.printStackTrace();
		}
	}

	public static boolean isProtected(final int chunkX, final int chunkZ, final ChunkBlockLocation cbl) throws EOFException {
		if (cbl == null)
			return false;
		final int fileExX = chunkX >> 3;
		final int fileExZ = chunkZ >> 3;
		final String newFileName = filePrefix + String.valueOf(fileExX) + "." + String.valueOf(fileExZ);
		final String newChunkName = "chunk." + String.valueOf(chunkX) + "." + String.valueOf(chunkZ);
		DBAccess database = null;
		try {
			if (loadedDataBases.containsKey(newFileName)) {
				if (CPConfigEnum.VERBOSE.getBoolean())
					CreativePlus.log.info("Reading from already loaded Database...");
				database = loadedDataBases.get(newFileName);
				if (database != null) {
					final RecordReader rr = database.getRecord(newChunkName);
					if (rr != null) {
						final Object o = rr.readObject();
						final CBLSet blocks = (CBLSet) o;
						if (blocks == null)
							return false;
						return blocks.getBlocks().contains(cbl);
					} else
						return false;
				}
			} else if (existingFiles.keySet().contains(newFileName)) {
				if (CPConfigEnum.VERBOSE.getBoolean())
					CreativePlus.log.info("Database not loaded but exists, loading and reading it now....");
				database = new DBAccess(existingFiles.get(newFileName).getPath(), "rw", true, 5);
				if (database != null) {
					if (!loadedDataBases.containsKey(newFileName))
						loadedDataBases.put(newFileName, database);
					final RecordReader rr = database.getRecord(newChunkName);
					if (rr != null) {
						final Object o = rr.readObject();
						final CBLSet blocks = (CBLSet) o;
						if (blocks == null)
							return false;
						return blocks.getBlocks().contains(cbl);
					} else
						return false;
				}
			} else
				return false;
		} catch (RecordsFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if (e instanceof EOFException) {
				EOFException ex = new EOFException("Affected file: " + newFileName
						+ ", affected entry: " + newChunkName);
				ex.setStackTrace(e.getStackTrace());
				throw ex;
			} if (e.getMessage() != null && e.getMessage().contains("large"))
				CreativePlus.log.severe(e.getMessage());
			else
				e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (CacheSizeException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			if (e.getMessage() != null)
				CreativePlus.log.severe(e.getMessage());
			else
				e.printStackTrace();
		}
		return false;
	}
}

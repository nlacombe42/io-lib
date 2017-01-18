package net.nlacombe.io.domain.fixedsizedatafile;

import net.nlacombe.io.domain.ArrayAllocationTable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public abstract class FixedSizeDataFile implements AutoCloseable
{
	private RandomAccessFile randomAccessFile;
	private DataUnitStore dataUnitStore;

	public FixedSizeDataFile(Path filePath, ArrayAllocationTable allocationTable, Long dataStartOffsetInBytes) throws FileNotFoundException
	{
		randomAccessFile = new RandomAccessFile(filePath.toFile(), "rw");
		dataUnitStore = new DataUnitStore(allocationTable, randomAccessFile, dataStartOffsetInBytes);
	}

	protected abstract void writeAllocationTableToFile() throws IOException;

	public DataUnitAddress createDataUnit(byte[] data) throws IOException
	{
		DataUnitAddress address = dataUnitStore.createDataUnit(data);
		writeAllocationTableToFile();

		return address;
	}

	public byte[] readDataUnit(DataUnitAddress address) throws IOException
	{
		return dataUnitStore.readDataUnit(address);
	}

	public void updateDataUnit(DataUnitAddress address, byte[] data) throws IOException
	{
		dataUnitStore.updateDataUnit(address, data);
	}

	public void deleteDataUnit(DataUnitAddress address) throws IOException
	{
		dataUnitStore.deleteDataUnit(address);
		writeAllocationTableToFile();
	}

	@Override
	public void close()
	{
		try
		{
			randomAccessFile.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	protected RandomAccessFile getRandomAccessFile()
	{
		return randomAccessFile;
	}
}

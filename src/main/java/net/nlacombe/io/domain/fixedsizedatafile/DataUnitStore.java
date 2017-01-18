package net.nlacombe.io.domain.fixedsizedatafile;

import net.nlacombe.io.domain.ArrayAllocationTable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DataUnitStore
{
	private ArrayAllocationTable allocationTable;
	private Long dataStartOffsetInBytes;
	private RandomAccessFile randomAccessFile;

	public DataUnitStore(ArrayAllocationTable allocationTable, RandomAccessFile randomAccessFile, Long dataStartOffsetInBytes) throws FileNotFoundException
	{
		this.allocationTable = allocationTable;
		this.dataStartOffsetInBytes = dataStartOffsetInBytes;
		this.randomAccessFile = randomAccessFile;
	}

	public DataUnitAddress createDataUnit(byte[] data) throws IOException
	{
		Long dataUnitSize = (long) data.length;
		Long dataUnitOffset = allocationTable.allocate(dataUnitSize);

		writeDataToFile(dataUnitOffset, data);

		return new DataUnitAddress(dataUnitOffset, dataUnitSize);
	}

	public byte[] readDataUnit(DataUnitAddress address) throws IOException
	{
		return readDataFromFile(address);
	}

	public void updateDataUnit(DataUnitAddress address, byte[] data) throws IOException
	{
		validateSameSize(address, (long) data.length);

		writeDataToFile(address.getOffsetInBytes(), data);
	}

	public void deleteDataUnit(DataUnitAddress address)
	{
		allocationTable.free(address.getOffsetInBytes(), address.getSizeInBytes());
	}

	private void writeDataToFile(Long dataUnitOffset, byte[] data) throws IOException
	{
		randomAccessFile.seek(dataStartOffsetInBytes + dataUnitOffset);
		randomAccessFile.write(data);
	}

	private byte[] readDataFromFile(DataUnitAddress address) throws IOException
	{
		byte[] dataBuffer = new byte[address.getSizeInBytes().intValue()];

		randomAccessFile.seek(dataStartOffsetInBytes + address.getOffsetInBytes());
		randomAccessFile.readFully(dataBuffer);

		return dataBuffer;
	}

	private void validateSameSize(DataUnitAddress address, Long dataSize)
	{
		if (!address.getSizeInBytes().equals(dataSize))
			throw new IllegalArgumentException("The data unit address size is not equal to the size of the data provided.");
	}
}

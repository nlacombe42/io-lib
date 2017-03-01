package net.nlacombe.io.domain.fixedsizedatafile;

import net.nlacombe.io.domain.ArrayAllocationTable;
import net.nlacombe.io.domain.DeserializerStream;
import net.nlacombe.io.domain.SerializerStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FixedSizeDataFileHeader
{
	private ArrayAllocationTable allocationTable;
	private DataUnitAddress customHeaderAddress;

	public FixedSizeDataFileHeader(Long fileSizeInBytes, Long allocationUnitSizeInBytes)
	{
		this.allocationTable = new ArrayAllocationTable(fileSizeInBytes, allocationUnitSizeInBytes);
	}

	public FixedSizeDataFileHeader(InputStream inputStream)
	{
		readAndDeserialize(inputStream);
	}

	public static Long getSerializedSizeInBytes(Long fileSizeInBytes, Long allocationUnitSizeInBytes)
	{
		return new FixedSizeDataFileHeader(fileSizeInBytes, allocationUnitSizeInBytes).getSerializedSizeInBytes();
	}

	public Long getSerializedSizeInBytes()
	{
		return (long) serialize().length;
	}

	public byte[] serialize()
	{
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 SerializerStream ss = new SerializerStream(baos))
		{
			ss.writeLong(allocationTable.getAllocationUnitSizeInBytes());
			ss.writeFrame(allocationTable.toByteArray());

			Long offsetInBytes = -1L;
			Long sizeInBytes = -1L;

			if (customHeaderAddress != null)
			{
				offsetInBytes = customHeaderAddress.getOffsetInBytes();
				sizeInBytes = customHeaderAddress.getSizeInBytes();
			}

			ss.writeLong(offsetInBytes);
			ss.writeLong(sizeInBytes);

			return baos.toByteArray();
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	private void readAndDeserialize(InputStream inputStream)
	{
		try
		{
			DeserializerStream ds = new DeserializerStream(inputStream);
			Long allocationUnitSize = ds.readLong();
			byte[] allocationTableData = ds.readFrame();
			allocationTable = new ArrayAllocationTable(allocationTableData, allocationUnitSize);

			Long offsetInBytes = ds.readLong();
			Long sizeInBytes = ds.readLong();

			if (offsetInBytes == -1)
				customHeaderAddress = null;
			else
				customHeaderAddress = new DataUnitAddress(offsetInBytes, sizeInBytes);
		}
		catch (IOException exception)
		{
			throw new RuntimeException(exception);
		}
	}

	public ArrayAllocationTable getAllocationTable()
	{
		return allocationTable;
	}

	public DataUnitAddress getCustomHeaderAddress()
	{
		return customHeaderAddress;
	}

	public void setCustomHeaderAddress(DataUnitAddress customHeaderAddress)
	{
		this.customHeaderAddress = customHeaderAddress;
	}
}

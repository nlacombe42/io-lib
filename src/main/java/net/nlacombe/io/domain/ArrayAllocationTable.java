package net.nlacombe.io.domain;

import net.nlacombe.io.util.IoUtil;

public class ArrayAllocationTable
{
	private boolean[] allocationTable;
	private Long allocationUnitSizeInBytes;

	public ArrayAllocationTable(Long totalBlockSizeInBytes, Long allocationUnitSizeInBytes)
	{
		this.allocationUnitSizeInBytes = allocationUnitSizeInBytes;

		Integer numberOfAllocationUnits = (int) Math.ceil((double) totalBlockSizeInBytes / (double) allocationUnitSizeInBytes);

		this.allocationTable = new boolean[numberOfAllocationUnits];
	}

	public ArrayAllocationTable(byte[] byteArray, Long allocationUnitSizeInBytes)
	{
		this.allocationUnitSizeInBytes = allocationUnitSizeInBytes;

		allocationTable = new boolean[byteArray.length];

		for (int i = 0; i < allocationTable.length; i++)
			allocationTable[i] = IoUtil.byteToBoolean(byteArray[i]);
	}

	/**
	 * Allocates *size* bytes and returns the byte offset of the allocated space.
	 */
	public Long allocate(Long size)
	{
		Integer freeIndex = findFreeBlock(size);
		Integer numberOfAllocationUnits = (int) Math.ceil((double) size / (double) allocationUnitSizeInBytes);

		for (int i = freeIndex; i < freeIndex + numberOfAllocationUnits; i++)
			allocationTable[i] = true;

		return freeIndex * allocationUnitSizeInBytes;
	}

	public void free(Long offset, Long size)
	{
		Integer startIndex = (int) (offset / allocationUnitSizeInBytes);
		Integer numberOfAllocationUnits = (int) Math.ceil((double) size / (double) allocationUnitSizeInBytes);

		for (int i = startIndex; i < startIndex + numberOfAllocationUnits; i++)
			allocationTable[i] = false;
	}

	private Integer findFreeBlock(Long size)
	{
		for (int index = 0; index < allocationTable.length; index++)
			if (isFree(index * allocationUnitSizeInBytes, size))
				return index;

		throw new IllegalStateException("Not enough free memory");
	}

	private boolean isFree(Long offset, Long size)
	{
		Integer startIndex = (int) (offset / allocationUnitSizeInBytes);
		Integer endIndex = (int) Math.ceil((double) (offset + size) / (double) allocationUnitSizeInBytes);

		if (endIndex > allocationTable.length)
			throw new IndexOutOfBoundsException("block is outside bounds");

		for (int i = startIndex; i < endIndex; i++)
			if (allocationTable[i])
				return false;

		return true;
	}

	public Long getAllocationUnitSizeInBytes()
	{
		return allocationUnitSizeInBytes;
	}

	public boolean[] toBooleanArray()
	{
		return allocationTable;
	}

	public byte[] toByteArray()
	{
		byte[] byteArray = new byte[allocationTable.length];

		for (int i = 0; i < byteArray.length; i++)
			byteArray[i] = IoUtil.booleanToByte(allocationTable[i]);

		return byteArray;
	}
}

package net.nlacombe.io.domain;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class ArrayAllocationTableTest
{
	@Test
	public void testHalfAllocation()
	{
		ArrayAllocationTable allocationTable = get5MbAllocationTable();
		Long offset = allocationTable.allocate(FileUtils.ONE_MB / 2);

		assertThat(offset, is(equalTo(0L)));
		assertThat(allocationTable.toBooleanArray(), is(equalTo(new boolean[]{true, false, false, false, false})));
	}

	@Test
	public void testOneAllocation()
	{
		ArrayAllocationTable allocationTable = get5MbAllocationTable();
		Long offset = allocationTable.allocate(FileUtils.ONE_MB);

		assertThat(offset, is(equalTo(0L)));
		assertThat(allocationTable.toBooleanArray(), is(equalTo(new boolean[]{true, false, false, false, false})));
	}

	@Test
	public void testOneHalfAllocation()
	{
		ArrayAllocationTable allocationTable = get5MbAllocationTable();
		Long offset = allocationTable.allocate(FileUtils.ONE_MB + (FileUtils.ONE_MB / 2));

		assertThat(offset, is(equalTo(0L)));
		assertThat(allocationTable.toBooleanArray(), is(equalTo(new boolean[]{true, true, false, false, false})));
	}

	@Test
	public void testTwoAllocation()
	{
		ArrayAllocationTable allocationTable = get5MbAllocationTable();
		Long offset1 = allocationTable.allocate(FileUtils.ONE_MB);
		Long offset2 = allocationTable.allocate(FileUtils.ONE_MB);

		assertThat(offset1, is(equalTo(0L)));
		assertThat(offset2, is(equalTo(FileUtils.ONE_MB)));
		assertThat(allocationTable.toBooleanArray(), is(equalTo(new boolean[]{true, true, false, false, false})));
	}

	@Test
	public void testTwoAllocationAndAFree()
	{
		ArrayAllocationTable allocationTable = get5MbAllocationTable();
		Long offset1 = allocationTable.allocate(FileUtils.ONE_MB);
		Long offset2 = allocationTable.allocate(FileUtils.ONE_MB);
		allocationTable.free(0L, FileUtils.ONE_MB);

		assertThat(offset1, is(equalTo(0L)));
		assertThat(offset2, is(equalTo(FileUtils.ONE_MB)));
		assertThat(allocationTable.toBooleanArray(), is(equalTo(new boolean[]{false, true, false, false, false})));
	}

	@Test
	public void testSerializationAndDeserialization()
	{
		boolean[] expectedAllocationTable = {true, false, true, false, true};
		ArrayAllocationTable allocationTable = get5MbAllocationTable();
		allocationTable.allocate(FileUtils.ONE_MB);
		allocationTable.allocate(FileUtils.ONE_MB);
		allocationTable.allocate(FileUtils.ONE_MB);
		allocationTable.allocate(FileUtils.ONE_MB);
		allocationTable.allocate(FileUtils.ONE_MB);
		allocationTable.free(FileUtils.ONE_MB, FileUtils.ONE_MB);
		allocationTable.free(3 * FileUtils.ONE_MB, FileUtils.ONE_MB);

		assertThat(allocationTable.toBooleanArray(), is(equalTo(expectedAllocationTable)));

		byte[] serializedAllocationTable = allocationTable.toByteArray();
		ArrayAllocationTable deserializedAllocationTable = new ArrayAllocationTable(serializedAllocationTable, allocationTable.getAllocationUnitSizeInBytes());

		assertThat(deserializedAllocationTable.toBooleanArray(), is(equalTo(expectedAllocationTable)));
	}

	private ArrayAllocationTable get5MbAllocationTable()
	{
		return new ArrayAllocationTable(5 * FileUtils.ONE_MB, FileUtils.ONE_MB);
	}
}
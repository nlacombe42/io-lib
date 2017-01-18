package net.nlacombe.io.service;

import net.nlacombe.io.domain.fixedsizedatafile.DataUnitAddress;
import net.nlacombe.io.domain.fixedsizedatafile.FixedSizeDataFile;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class FixedSizeDataFileServiceFunctionalTest
{
	private FixedSizeDataFileService fixedSizeDataFileService = FixedSizeDataFileService.getInstance();
	private Path filePath;

	@Before
	public void setup() throws IOException
	{
		filePath = Files.createTempFile("test-", "");
	}

	@Test
	public void writeAndReadOneDataUnit() throws IOException
	{
		byte[] givenDataUnit = getRandomBytes(100);
		Long fileSize = 10 * FileUtils.ONE_KB;
		Long allocationSize = FileUtils.ONE_KB;

		DataUnitAddress dataUnitAddress = createFileWithDataUnit(givenDataUnit, filePath, fileSize, allocationSize);
		byte[] readDataUnit = readDataUnitFromFile(dataUnitAddress, filePath);

		assertThat(readDataUnit, is(equalTo(givenDataUnit)));
	}

	@After
	public void tearDown() throws IOException
	{
		Files.deleteIfExists(filePath);
	}

	private DataUnitAddress createFileWithDataUnit(byte[] dataUnit, Path filePath, Long fileSize, Long allocationSize) throws IOException
	{
		try (FixedSizeDataFile fixedSizeDataFile = fixedSizeDataFileService.createFixedSizeDataFile(filePath, fileSize, allocationSize))
		{
			return fixedSizeDataFile.createDataUnit(dataUnit);
		}
	}

	private byte[] readDataUnitFromFile(DataUnitAddress dataUnitAddress, Path filePath) throws IOException
	{
		try (FixedSizeDataFile fixedSizeDataFile = fixedSizeDataFileService.getFixedSizeDataFile(filePath))
		{
			return fixedSizeDataFile.readDataUnit(dataUnitAddress);
		}
	}

	private byte[] getRandomBytes(int numberOfBytes)
	{
		byte[] randomBytes = new byte[numberOfBytes];

		new Random().nextBytes(randomBytes);

		return randomBytes;
	}
}
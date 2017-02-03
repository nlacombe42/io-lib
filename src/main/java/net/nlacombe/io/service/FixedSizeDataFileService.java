package net.nlacombe.io.service;

import net.nlacombe.io.domain.fixedsizedatafile.FixedSizeDataFile;
import net.nlacombe.io.domain.fixedsizedatafile.FixedSizeDataFileHeader;
import net.nlacombe.io.domain.fixedsizedatafile.DefaultFixedSizeDataFile;
import net.nlacombe.io.util.IoUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class FixedSizeDataFileService
{
	private static FixedSizeDataFileService instance;

	private FixedSizeDataFileService()
	{
	}

	public static FixedSizeDataFileService getInstance()
	{
		if (instance == null)
			instance = new FixedSizeDataFileService();

		return instance;
	}

	public FixedSizeDataFile createFixedSizeDataFile(Path filePath, Long fileSizeInBytes, Long allocationUnitSizeInBytes) throws IOException
	{
		validateFileSize(fileSizeInBytes, allocationUnitSizeInBytes);

		return new DefaultFixedSizeDataFile(filePath, fileSizeInBytes, allocationUnitSizeInBytes);
	}

	public FixedSizeDataFile getFixedSizeDataFile(Path filePath) throws IOException
	{
		return new DefaultFixedSizeDataFile(filePath);
	}

	private void validateFileSize(Long fileSizeInBytes, Long allocationUnitSizeInBytes)
	{
		Long fileHeaderSize = FixedSizeDataFileHeader.getSerializedSizeInBytes(fileSizeInBytes, allocationUnitSizeInBytes);
		Long minimumFileSize = fileHeaderSize + allocationUnitSizeInBytes;

		if (fileSizeInBytes < minimumFileSize)
			throw new IllegalArgumentException("File size must be at least " + minimumFileSize + " bytes given allocation unit size and total file size.");
	}
}

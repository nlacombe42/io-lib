package net.nlacombe.io.domain.fixedsizedatafile;

import java.io.IOException;
import java.nio.file.Path;

public class DefaultFixedSizeDataFile extends FixedSizeDataFile
{
	/**
	 * Creates a new data file with fixed size.
	 */
	public DefaultFixedSizeDataFile(Path filePath, Long fileSizeInBytes, Long allocationUnitSizeInBytes) throws IOException
	{
		super(filePath, fileSizeInBytes, allocationUnitSizeInBytes, new DefaultFileHeaderService());

		validateFileSize(fileSizeInBytes, allocationUnitSizeInBytes);
	}

	/**
	 * Creates a FixedSizeDataFile from an existing file.
	 */
	public DefaultFixedSizeDataFile(Path filePath) throws IOException
	{
		super(filePath, new DefaultFileHeaderService());
	}

	private static void validateFileSize(Long fileSizeInBytes, Long allocationUnitSizeInBytes)
	{
		Long fileHeaderSize = FixedSizeDataFileHeader.getSerializedSizeInBytes(fileSizeInBytes, allocationUnitSizeInBytes);
		Long minimumFileSize = fileHeaderSize + allocationUnitSizeInBytes;

		if (fileSizeInBytes < minimumFileSize)
			throw new IllegalArgumentException("File size must be at least " + minimumFileSize + " bytes given allocation unit size and total file size.");
	}
}

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
	}

	/**
	 * Creates a FixedSizeDataFile from an existing file.
	 */
	public DefaultFixedSizeDataFile(Path filePath) throws IOException
	{
		super(filePath, new DefaultFileHeaderService());
	}
}

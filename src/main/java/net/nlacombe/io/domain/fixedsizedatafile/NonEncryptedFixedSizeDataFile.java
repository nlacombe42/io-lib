package net.nlacombe.io.domain.fixedsizedatafile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public class NonEncryptedFixedSizeDataFile extends FixedSizeDataFile
{
	private FixedSizeDataFileHeader fileHeader;

	public NonEncryptedFixedSizeDataFile(Path filePath, FixedSizeDataFileHeader fileHeader) throws FileNotFoundException
	{
		super(filePath, fileHeader.getAllocationTable(), fileHeader.getSerializedSizeInBytes());

		this.fileHeader = fileHeader;
	}

	@Override
	protected void writeAllocationTableToFile() throws IOException
	{
		RandomAccessFile randomAccessFile = getRandomAccessFile();

		randomAccessFile.seek(0);
		randomAccessFile.write(fileHeader.serialize());
	}
}

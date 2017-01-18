package net.nlacombe.io.service;

import net.nlacombe.io.domain.fixedsizedatafile.FixedSizeDataFile;
import net.nlacombe.io.domain.fixedsizedatafile.FixedSizeDataFileHeader;
import net.nlacombe.io.domain.fixedsizedatafile.NonEncryptedFixedSizeDataFile;
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

		FixedSizeDataFileHeader fileHeader = new FixedSizeDataFileHeader(fileSizeInBytes, allocationUnitSizeInBytes);

		IoUtil.writeRandomBytes(filePath, fileSizeInBytes);
		writeFileHeader(fileHeader);

		return new NonEncryptedFixedSizeDataFile(filePath, fileHeader);
	}

	public FixedSizeDataFile getFixedSizeDataFile(Path filePath) throws IOException
	{
		FixedSizeDataFileHeader fileHeader = readFixedSizeDataFileHeader(filePath);

		return new NonEncryptedFixedSizeDataFile(filePath, fileHeader);
	}

	private FixedSizeDataFileHeader readFixedSizeDataFileHeader(Path filePath) throws IOException
	{
		try (FileInputStream fis = new FileInputStream(filePath.toFile()))
		{
			return new FixedSizeDataFileHeader(fis);
		}
	}

	private void writeFileHeader(FixedSizeDataFileHeader fileHeader) throws IOException
	{
		RandomAccessFile raf = new RandomAccessFile(fileHeader.toString(), "rw");
		raf.write(fileHeader.serialize());
	}

	private void validateFileSize(Long fileSizeInBytes, Long allocationUnitSizeInBytes)
	{
		Long fileHeaderSize = FixedSizeDataFileHeader.getSerializedSizeInBytes(fileSizeInBytes, allocationUnitSizeInBytes);
		Long minimumFileSize = fileHeaderSize + allocationUnitSizeInBytes;

		if (fileSizeInBytes < minimumFileSize)
			throw new IllegalArgumentException("File size must be at least " + minimumFileSize + " bytes given allocation unit size and total file size.");
	}
}

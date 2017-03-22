package net.nlacombe.io.domain.fixedsizedatafile;

import net.nlacombe.io.util.IoUtil;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

public abstract class FixedSizeDataFile implements AutoCloseable
{
	private RandomAccessFile randomAccessFile;
	private DataUnitStore dataUnitStore;
	private FileHeaderService fileHeaderService;
	private FixedSizeDataFileHeader fileHeader;

	/**
	 * Creates a new data file with fixed size.
	 */
	public FixedSizeDataFile(Path filePath, Long fileSizeInBytes, Long allocationUnitSizeInBytes, FileHeaderService fileHeaderService) throws IOException
	{
		this.fileHeaderService = fileHeaderService;
		randomAccessFile = new RandomAccessFile(filePath.toFile(), "rw");

		IoUtil.writeRandomBytes(filePath, fileSizeInBytes);

		this.fileHeader = new FixedSizeDataFileHeader(fileSizeInBytes, allocationUnitSizeInBytes);
		fileHeaderService.writeHeader(randomAccessFile, fileHeader);

		dataUnitStore = new DataUnitStore(fileHeader.getAllocationTable(), randomAccessFile, fileHeaderService.getTotalFileHeaderSize(fileHeader));
	}

	/**
	 * Creates a FixedSizeDataFile from an existing file.
	 */
	public FixedSizeDataFile(Path filePath, FileHeaderService fileHeaderService) throws IOException
	{
		this.fileHeaderService = fileHeaderService;
		randomAccessFile = new RandomAccessFile(filePath.toFile(), "rw");

		this.fileHeader = fileHeaderService.readHeader(randomAccessFile);

		dataUnitStore = new DataUnitStore(fileHeader.getAllocationTable(), randomAccessFile, fileHeaderService.getTotalFileHeaderSize(fileHeader));
	}

	public DataUnitAddress createDataUnit(byte[] data) throws IOException
	{
		DataUnitAddress address = dataUnitStore.createDataUnit(data);
		fileHeaderService.writeHeader(randomAccessFile, fileHeader);

		return address;
	}

	public byte[] readDataUnit(DataUnitAddress address) throws IOException
	{
		return dataUnitStore.readDataUnit(address);
	}

	public void updateDataUnit(DataUnitAddress address, byte[] data) throws IOException
	{
		dataUnitStore.updateDataUnit(address, data);
	}

	public void deleteDataUnit(DataUnitAddress address) throws IOException
	{
		dataUnitStore.deleteDataUnit(address);
		fileHeaderService.writeHeader(randomAccessFile, fileHeader);
	}

	public DataUnitAddress updateVariableSizeData(DataUnitAddress oldAddress, byte[] data) throws IOException
	{
		dataUnitStore.deleteDataUnit(oldAddress);
		DataUnitAddress newAddress = dataUnitStore.createDataUnit(data);

		fileHeaderService.writeHeader(randomAccessFile, fileHeader);

		return newAddress;
	}

	public void createCustomHeader(byte[] data) throws IOException
	{
		validateCustomHeaderDoesNotExist();

		DataUnitAddress customHeaderAddress = createDataUnit(data);

		fileHeader.setCustomHeaderAddress(customHeaderAddress);
		fileHeaderService.writeHeader(randomAccessFile, fileHeader);
	}

	public byte[] readCustomHeader() throws IOException
	{
		DataUnitAddress customHeaderAddress = fileHeader.getCustomHeaderAddress();

		if (customHeaderAddress == null)
			throw new IllegalStateException("Custom header does not exists");

		return readDataUnit(customHeaderAddress);
	}

	public void updateCustomHeader(byte[] data) throws IOException
	{
		DataUnitAddress customHeaderAddress = fileHeader.getCustomHeaderAddress();

		if (customHeaderAddress == null)
			throw new IllegalStateException("Custom header does not exists");

		customHeaderAddress = updateVariableSizeData(customHeaderAddress, data);

		fileHeader.setCustomHeaderAddress(customHeaderAddress);
		fileHeaderService.writeHeader(randomAccessFile, fileHeader);
	}

	public void deleteCustomHeader() throws IOException
	{
		DataUnitAddress customHeaderAddress = fileHeader.getCustomHeaderAddress();

		if (customHeaderAddress == null)
			throw new IllegalStateException("Custom header does not exists");

		deleteDataUnit(customHeaderAddress);

		fileHeader.setCustomHeaderAddress(null);
		fileHeaderService.writeHeader(randomAccessFile, fileHeader);
	}

	private void validateCustomHeaderDoesNotExist()
	{
		if (fileHeader.getCustomHeaderAddress() != null)
			throw new IllegalStateException("Custom header already present");
	}

	@Override
	public void close()
	{
		try
		{
			randomAccessFile.close();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}

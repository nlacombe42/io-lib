package net.nlacombe.io.domain.fixedsizedatafile;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;

public class DefaultFileHeaderService implements FileHeaderService
{
	@Override
	public void writeHeader(RandomAccessFile randomAccessFile, FixedSizeDataFileHeader fileHeader) throws IOException
	{
		randomAccessFile.seek(0);
		randomAccessFile.write(fileHeader.serialize());
	}

	@Override
	public FixedSizeDataFileHeader readHeader(RandomAccessFile randomAccessFile) throws IOException
	{
		randomAccessFile.seek(0);

		return new FixedSizeDataFileHeader(Channels.newInputStream(randomAccessFile.getChannel()));
	}

	@Override
	public Long getTotalFileHeaderSize(FixedSizeDataFileHeader fixedSizeDataFileHeader)
	{
		return fixedSizeDataFileHeader.getSerializedSizeInBytes();
	}
}

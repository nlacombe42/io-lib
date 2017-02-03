package net.nlacombe.io.domain.fixedsizedatafile;

import java.io.IOException;
import java.io.RandomAccessFile;

public interface FileHeaderService
{
	void writeHeader(RandomAccessFile randomAccessFile, FixedSizeDataFileHeader fixedSizeDataFileHeader) throws IOException;

	FixedSizeDataFileHeader readHeader(RandomAccessFile randomAccessFile) throws IOException;

	Long getTotalFileHeaderSize(FixedSizeDataFileHeader fixedSizeDataFileHeader);
}

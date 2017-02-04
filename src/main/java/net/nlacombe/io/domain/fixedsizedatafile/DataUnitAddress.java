package net.nlacombe.io.domain.fixedsizedatafile;

public class DataUnitAddress
{
	private Long offsetInBytes;
	private Long sizeInBytes;

	public DataUnitAddress(Long offsetInBytes, Long sizeInBytes)
	{
		this.offsetInBytes = offsetInBytes;
		this.sizeInBytes = sizeInBytes;
	}

	public Long getOffsetInBytes()
	{
		return offsetInBytes;
	}

	public Long getSizeInBytes()
	{
		return sizeInBytes;
	}
}

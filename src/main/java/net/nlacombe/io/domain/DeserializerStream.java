package net.nlacombe.io.domain;

import net.nlacombe.io.util.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Stream to aid deserialization of complex data.
 * Uses FrameInputStream to read frames and converts them to specific data elements.
 */
public class DeserializerStream extends FrameInputStream
{
	public DeserializerStream(InputStream is)
	{
		super(is);
	}

	public String readString() throws IOException
	{
		return new String(readFrame(), StandardCharsets.UTF_8);
	}

	public int readInteger() throws IOException
	{
		return IoUtil.deserializeInteger(readFrame());
	}

	public long readLong() throws IOException
	{
		return IoUtil.deserializeLong(readFrame());
	}

	public boolean readBoolean() throws IOException
	{
		byte[] oneByte = IoUtil.read(this, 1);

		return IoUtil.byteToBoolean(oneByte[0]);
	}

	public UUID readUuid() throws IOException
	{
		return IoUtil.deserializeUuid(readFrame());
	}

	public byte[] readBytes(int numberOfBytes) throws IOException
	{
		return IoUtil.read(this, numberOfBytes);
	}
}

package net.nlacombe.io.domain;

import net.nlacombe.io.util.IoUtil;
import org.apache.commons.io.Charsets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Stream to aid serialization of complex data.
 * Uses FrameOutputStream to writes each data element in a frame so it can be read as one element again with DeserializerStream.
 */
public class SerializerStream extends FrameOutputStream
{
	public SerializerStream(OutputStream os)
	{
		super(os);
	}

	public void writeString(String string) throws IOException
	{
		writeFrame(string.getBytes(Charsets.UTF_8));
	}

	public void writeInteger(int integer) throws IOException
	{
		writeFrame(IoUtil.serializeInteger(integer));
	}

	public void writeLong(long longValue) throws IOException
	{
		writeFrame(IoUtil.serializeLong(longValue));
	}

	public void writeBoolean(boolean b) throws IOException
	{
		write(new byte[]{IoUtil.booleanToByte(b)});
	}

	public void writeUuid(UUID uuid) throws IOException
	{
		writeFrame(IoUtil.serializeUuid(uuid));
	}
}

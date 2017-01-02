package net.nlacombe.io.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

public class IoUtil
{
	private static final int BOOLEAN_TRUE_INTEGER = 1;
	private static final int BOOLEAN_FALSE_INTEGER = 0;

	public static int ubyte(byte b)
	{
		if (b >= 0)
			return b;
		else
			return b + 256;
	}

	public static int b4toint(byte[] buffer)
	{
		return ByteBuffer.wrap(buffer).getInt();
	}

	public static byte[] inttob4(int i)
	{
		return ByteBuffer.allocate(4).putInt(i).array();
	}

	public static void writeInteger(OutputStream os, int number) throws IOException
	{
		os.write(inttob4(number));
	}

	public static int readInteger(InputStream is) throws IOException
	{
		return b4toint(read(is, 4));
	}

	public static void writeBoolean(OutputStream outputStream, boolean bool) throws IOException
	{
		int integer = bool ? BOOLEAN_TRUE_INTEGER : BOOLEAN_FALSE_INTEGER;
		outputStream.write(inttob4(integer));
	}

	public static boolean readBoolean(InputStream inputStream) throws IOException
	{
		int integer = readInteger(inputStream);

		return integer == BOOLEAN_TRUE_INTEGER;
	}

	/**
	 * Read <code>size</code> bytes from input stream <code>is</code>.
	 * Keeps reading until all bytes are read or there is an error reading or reaches end of stream.
	 * If not all the bytes are read this methods throws an exception (even if end of stream is reached).
	 */
	public static byte[] read(InputStream is, int size) throws IOException
	{
		byte[] buffer = new byte[size];

		IOUtils.readFully(is, buffer);

		return buffer;
	}

	public static byte[] serialize(UUID uuid) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		dos.writeLong(uuid.getMostSignificantBits());
		dos.writeLong(uuid.getLeastSignificantBits());

		return baos.toByteArray();
	}
}

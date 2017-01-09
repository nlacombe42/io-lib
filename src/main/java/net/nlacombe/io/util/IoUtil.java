package net.nlacombe.io.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.UUID;

public class IoUtil
{
	private static final byte BOOLEAN_TRUE_BYTE = (byte) 0xff;
	private static final byte BOOLEAN_FALSE_BYTE = 0x00;

	public static int getUnsignedValueOfByte(byte b)
	{
		if (b >= 0)
			return b;
		else
			return b + 256;
	}

	public static int deserializeInteger(byte[] buffer)
	{
		return ByteBuffer.wrap(buffer).getInt();
	}

	public static byte[] serializeInteger(int integer)
	{
		return ByteBuffer.allocate(4).putInt(integer).array();
	}

	public static byte[] serializeLong(long longValue)
	{
		return ByteBuffer.allocate(8).putLong(longValue).array();
	}

	public static long deserializeLong(byte[] buffer)
	{
		return ByteBuffer.wrap(buffer).getLong();
	}

	public static byte booleanToByte(boolean bool)
	{
		return bool ? BOOLEAN_TRUE_BYTE : BOOLEAN_FALSE_BYTE;
	}

	public static boolean byteToBoolean(byte b)
	{
		return b == BOOLEAN_TRUE_BYTE;
	}


	public static byte[] serializeUuid(UUID uuid)
	{
		ByteBuffer byteBuffer = ByteBuffer.allocate(16);

		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());

		return byteBuffer.array();
	}

	public static UUID deserializeUuid(byte[] uuidBytes)
	{
		ByteBuffer byteBuffer = ByteBuffer.wrap(uuidBytes);

		long mostSignificantBits = byteBuffer.getLong();
		long leastSignificantBits = byteBuffer.getLong();

		return new UUID(mostSignificantBits, leastSignificantBits);
	}

	/**
	 * Read <code>size</code> bytes from input stream <code>is</code>.
	 * Keeps reading until all bytes are read, there is an error reading or reaches end of stream.
	 * If not all the bytes are read this methods throws an exception (even if end of stream is reached).
	 */
	public static byte[] read(InputStream is, int size) throws IOException
	{
		byte[] buffer = new byte[size];

		IOUtils.readFully(is, buffer);

		return buffer;
	}

	public static void writeRandomBytes(Path file, Long numberOfBytes) throws IOException
	{
		byte[] randomBytes = new byte[numberOfBytes.intValue()];

		new SecureRandom().nextBytes(randomBytes);

		Files.write(file, randomBytes);
	}
}

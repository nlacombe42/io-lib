package net.nlacombe.io.util;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class IoUtilTest
{
	private Path testFile;

	@Before
	public void createTestFile() throws IOException
	{
		testFile = Files.createTempFile("tmp-test", "");
	}

	@Test
	@Ignore
	public void writeRandomBytes_works_with_big_files() throws IOException
	{
		long fileSize = 2 * FileUtils.ONE_GB;

		IoUtil.writeRandomBytes(testFile, fileSize);

		assertThat(Files.size(testFile), is(equalTo(fileSize)));
	}

	@After
	public void deleteTestFile() throws IOException
	{
		Files.deleteIfExists(testFile);
	}
}
package tgc.framework;

import java.io.Closeable;
import java.io.IOException;

public final class StreamHelper
{
	public static void safeClose(final Closeable stream)
	{
		try
		{
			if (stream != null)
			{
				stream.close();
			}
		}
		catch(final IOException exc)
		{
			// ignore IOExceptions
		}
	}
}
package tgc.framework;

import java.io.IOException;
import java.io.InputStream;

public final class StreamHelper
{
	public static void safeClose(final InputStream inStream)
	{
		try
		{
			if (inStream != null)
			{
				inStream.close();
			}
		}
		catch(final IOException exc)
		{
			// ignore IOExceptions
		}
	}
}
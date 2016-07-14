package tgc.epubgenerator;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * This class represents JPG file, which can be as a book cover
 */
public class JpegFile
{
	private static final int COVER_WIDTH = 400;
	private static final int COVER_HEIGHT = 600;
	
	private String title;
	private String author;
	
	public JpegFile(final String title, final String author)
	{
		this.title = title;
		this.author = author;
	}
	
	public File generate(final File destPath)
	{
		BufferedImage coverImage = paintCover();
		File storedFile = new java.io.File(destPath.getPath() + "/cover.jpg");
		storedFile.getParentFile().mkdirs();
		
		// save buffered image to file
		try
		{
			ImageIO.write(coverImage,  "jpg", storedFile);
		}
		catch (final IOException exc)
		{
			System.err.println("Error: could not write Jpeg file to disk. " + exc.getMessage());
			storedFile = null;
		}
		return storedFile;
	}
	
	// Helper method for painting the image file; uses a JpegCover object internally (see below)
	private BufferedImage paintCover()
	{
		BufferedImage bufImg = new JpegCover(title, author, COVER_WIDTH, COVER_HEIGHT);
		return bufImg;
	}
}

// Non-public helper class for painting a cover. It writes text into some java.awt components.
// Many properties like font, font size, pixel dimensions and colours are randomized. 
class JpegCover extends BufferedImage
{	
	JpegCover(String title, String author, int width, int height)
	{
		super(width, height, TYPE_INT_RGB);
		
		Graphics2D gra = this.createGraphics();
		
		// set random, but bright background colour
		Random random = new Random();
		int r1 = (int) (random.nextFloat() * 127.0 + 128.0);
		int g1 = (int) (random.nextFloat() * 127.0 + 128.0);
		int b1 = (int) (random.nextFloat() * 127.0 + 128.0);
		Color bgcolor = new Color(r1, g1, b1);
		gra.setBackground(bgcolor);
		gra.clearRect(0, 0, width, height);
		
		// set random, but dark font colour
		int r2 = (int) (random.nextFloat() * 127.0);
		int g2 = (int) (random.nextFloat() * 127.0);
		int b2 = (int) (random.nextFloat() * 127.0);
		Color fcolor = new Color(r2, g2, b2);
		gra.setColor(fcolor);
		
		// select a font randomly
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = env.getAllFonts();
		int fontNumber = random.nextInt(fonts.length - 1);
		Font baseFont = fonts[fontNumber];
		// select a random font that can display title and author of this product. 
		// Font.canDisplayUpTo return -1, if the selected font can display all characters in a given string
		while(baseFont.canDisplayUpTo(author + title) > -1)
		{
			baseFont = fonts[random.nextInt(fonts.length - 1)];
		}
		
		Font largeFont = new Font(baseFont.getName(), Font.BOLD, (int) (height * 0.75 * 0.08)); // 1 px is approximately 0.75 pt, and large font should be about 5% of the canvas height
		Font mediumFont = new Font(baseFont.getName(), Font.BOLD, (int) (height * 0.75 * 0.05));
		Font smallFont = new Font(baseFont.getName(), Font.PLAIN, (int) (height * 0.75 * 0.035));
		FontMetrics largeMetrics = gra.getFontMetrics(largeFont);
		FontMetrics mediumMetrics = gra.getFontMetrics(mediumFont);
		FontMetrics smallMetrics = gra.getFontMetrics(smallFont);
		
		// print title
		gra.setFont(largeFont);
		int titleWidth = largeMetrics.stringWidth(title);
		if(titleWidth > getWidth())
		{
			// title too long, break it up into two lines
			int breakpoint = title.length() / 3;
			int spaceAfterBreakpoint = title.indexOf(' ', breakpoint);
			if(spaceAfterBreakpoint > 0)
			{
				breakpoint = spaceAfterBreakpoint;
			}
			String titleComponent1 = title.substring(0, breakpoint);
			String titleComponent2 = title.substring(breakpoint);
			
			gra.drawString(titleComponent1, 
					(getWidth() - largeMetrics.stringWidth(titleComponent1)) / 2,  // centered title in x dimension 
					getHeight() / 10); // y location of title
			gra.drawString(titleComponent2, 
					(getWidth() - largeMetrics.stringWidth(titleComponent2)) / 2,  // centered title in x dimension 
					(getHeight() / 10) + largeMetrics.getHeight()); // y location of title
		}
		else
		{
			// title fits into one line
			gra.drawString(title, 
					(getWidth() - titleWidth) / 2,  // centered title in x dimension 
					getHeight() / 10); // y location of title
		}
		
		// print author
		gra.setFont(mediumFont);
		gra.drawString("von " + author,
				(getWidth() - mediumMetrics.stringWidth("von " + author)) / 2, // centered author in x dimension
				(int) ((getHeight() / 10.0) * 2.5)); // y location of title
		
		// print publisher string
		gra.setFont(smallFont);
		gra.drawString("erschienen im",
				(getWidth() - smallMetrics.stringWidth("erschienen im")) / 2,
				(int) ((getHeight() / 2.0)));
		gra.drawString("Komponentenverlag", 
				(getWidth() - smallMetrics.stringWidth("Komponentenverlag")) / 2,
				(int) ((getHeight() / 1.5)));
		gra.drawString("Trier",
				(getWidth() - smallMetrics.stringWidth("Trier")) / 2,
				(int) ((getHeight() / 1.5)) + smallMetrics.getHeight());
		int year = new GregorianCalendar().getWeekYear();
		String yearString = Integer.toString(year);
		gra.drawString(yearString,
				(getWidth() - smallMetrics.stringWidth(yearString)) / 2,
				(int) ((getHeight() / 1.5)) + 2 * smallMetrics.getHeight());
	}
}

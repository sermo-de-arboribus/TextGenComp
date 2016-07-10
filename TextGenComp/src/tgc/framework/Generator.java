package tgc.framework;

import java.util.Locale;

public interface Generator extends LocaleSupporter
{
	// This method generates the text, based on the Spring configuration, and returns it as a string
	public String generateText(Locale locale);
	// This method determines, if the generated text should be only returned as a string (OutputType.STRING) 
	// or also be output in a different form (e.g. saved as a file or filled into an OutputStream). 
	// If the Generator supports alternative OutputTypes, these require separate configuration, which
	// are to be handled individually per Generator.
	public void setOutputType(OutputType outputType) throws OutputTypeNotSupportedException;
	// Determines if a certain output type is supported by this Generator object
	public boolean isSupportedOutputType(OutputType outputType);
}
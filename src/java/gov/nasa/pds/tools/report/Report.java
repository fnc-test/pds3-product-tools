package gov.nasa.pds.tools.report;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Class to generate a human-readable report from an XML log.
 * 
 * @author mcayanan
 *
 */
public class Report {
	private InputStream xml;
	private String stylesheet;

	
	/**
	 * Constructor
	 * 
	 * @param xml A stream representation of the XML log to transform.
	 * @param stylesheet An xsl stylesheet file name.
	 */
	public Report(InputStream xml, String stylesheet) {
		this.xml = xml;
		this.stylesheet = stylesheet;
	}
	
	/**
	 * Generates the human-readable report.
	 * 
	 * @param output The stream where the human-readable report will be
	 *  produced.
	 * @param level Specify the severity level and above to include in the 
	 *  report. Valid values are 'info', 'warning', and 'error'. 
	 * @throws SeverityException For an invalid severity level.
	 * @throws TransformerException If there was an error producing the report.
	 */
	public void generateReport(OutputStream output, String level)
	                           throws TransformerException {
		Source xmlSource = new StreamSource(this.xml);
        Source xsltSource = new StreamSource(getClass().getResourceAsStream(this.stylesheet));
        Result result = new StreamResult(output);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(xsltSource);      
        transformer.setParameter("level", level.toUpperCase());
        transformer.transform(xmlSource, result);
	}
}

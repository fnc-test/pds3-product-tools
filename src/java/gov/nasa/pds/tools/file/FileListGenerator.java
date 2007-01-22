//	Copyright (c) 2005, California Institute of Technology.
//	ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
//	 $Id: FileListGenerator.java 1429 2006-07-14 22:25:54Z mcayanan $ 
//

package gov.nasa.pds.tools.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import gov.nasa.pds.tools.file.filefilter.WildcardOSFilter;

/**
 * Class that can generate a list of files from a supplied directory and optionally, a specified
 * filter. The resulting list is a set of URLs.
 * 
 * @author mcayanan
 * @version $Revision $
 */
public class FileListGenerator {

	private NotFileFilter noFileFilter;
	private IOFileFilter noDirFilter;
	private IOFileFilter fileFilter;
	private IOFileFilter effFileFilter;
	private FileFilter effDirFilter;
	private List files;
	private List subDirs;
	private final int fileExt = 3;
	
	/**
	 * Constructor that takes in a list of supplied targets (files, URLs, directories)
	 * @param list a list of files, URLs, and directories
	 *
	 */
	public FileListGenerator() {
		fileFilter = new WildcardOSFilter("*");
		noFileFilter = null;
		noDirFilter = null;
		files = new ArrayList();
		subDirs = new ArrayList();
	}
	
	/**
	 * Sets the filter to be used when searching for files in a directory
	 * @param wildcards a list of files and/or file patterns to match
	 */
	private void setFileFilter(List wildcards) {
		fileFilter = new WildcardOSFilter(wildcards);
	}
	
	/**
	 * Sets the filter to be used when searching for files to ignore in a directory
	 * @param wildcards a list of files and/or file patterns to ignore
	 */
	private void setNoFileFilter(List wildcards) {
		noFileFilter = new NotFileFilter(new WildcardOSFilter(wildcards));
	}
	
	/**
	 * Sets the filter to be used when searching for directories to ignore
	 * @param patterns a list of directory/directory patterns to ignore
	 */
	private void setNoDirFilter(List patterns) {
		noDirFilter = new NotFileFilter(new WildcardOSFilter(patterns));
	}
	
	/**
	 * Combines filters to include and exclude files using the AND file filter
	 *
	 */
	private void setEffFileFilter() {
		if(noFileFilter != null)
			effFileFilter = FileFilterUtils.andFileFilter(fileFilter, noFileFilter);
		else
			effFileFilter = fileFilter;
	}
	
	/**
	 * Combines filters to seek out directories to exclude using the AND file filter
	 *
	 */
	private void setEffDirFilter() {
		if(noDirFilter != null)
			effDirFilter = FileFilterUtils.andFileFilter(noDirFilter, FileFilterUtils.directoryFileFilter());
		else
			effDirFilter = FileFilterUtils.directoryFileFilter();
	}
	
	/**
	 * Sets all possible filters when looking in a directory
	 * 
	 * @param regexp File patterns to include when finding files in a directory
	 * @param noFiles File patterns to ignore when finding files in a directory
	 * @param noDirs Directory patterns to ignore when finding sub-directories
	 */
	public void setFilters(List regexp, List noFiles, List noDirs) {
		if(regexp != null)
			setFileFilter(regexp);
		if(noFiles != null)
			setNoFileFilter(noFiles);
		if(noDirs != null)
			setNoDirFilter(noDirs);
		
		setEffFileFilter();
		setEffDirFilter();
	}
	
	/**
	 * Gets the list of files. These will be a set of URLs.
	 * @return
	 */
	public List getFiles() {
		return files;
	}
	
	/**
	 * Gets the list of sub-directories. These will be a set of URLs.
	 * @return
	 */
	public List getSubDirs() {
		return subDirs;
	}
	
	/**
	 * Allows one to pass in a file or URL. If it is a file being passed in, then it will be 
	 * converted into a URL. Directories will be visited if the target is a directory. The
	 * resulting list will then need to be retrieved from the getFiles and getSubDirs methods.
	 * 
	 * @param getSubDirs 'true' to recursively search down subdirectories for files to add onto the list.
	 *  'false' to just search at the level of the supplied directory.
	 * @return A list of files based on the supplied list of targets
	 * @throws BadLocationException 
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public void visitTarget(String target, boolean getSubDirs) throws HttpException, IOException, BadLocationException {
		File file = null;
		try {
			URL url = new URL(target.toString());
			if((file = FileUtils.toFile(url)) != null) {
				visitFileTarget(file, getSubDirs);
				return;
			}
			if(!isLinkFile(target.toString()))
				crawl(new URL(target.toString()), getSubDirs);
			else
				files.add(url);
		}
		catch(MalformedURLException uEx) {
			visitFileTarget(new File(target), getSubDirs);
		}
	}
	
	/**
	 * Visits the file being supplied. If a file is being passed in, then it will be
	 * converted to URL. If a directory is being passed in, then it will look for
	 * files and sub-directories (if it is turned ON). In either case, the getFiles
	 * and getSubDirs methods can be called to retrieve the list of files and sub-directories
	 * found. The lists will be a set of URLs. 
	 * 
	 * @param file A file or a directory. If it is a directory, then the visitDir method
	 *      will be called and the list of files can be retrieved via the getFiles and
	 *      getSubDirs methods.
	 * @param getSubDirs Tells the method whether to look for sub-directories
	 * @throws IOException
	 */
	private void visitFileTarget(File file, boolean getSubDirs) throws IOException {
		if(file.isDirectory())
			visitDir(file, getSubDirs);
		else
			files.add(file.toURI().toURL());
	}
	
	/**
	 * Gets a list of files under a given directory. The files and directories can be retrieved
	 * by calling the getFiles and getSubDirs methods, respectively. The resulting list will be
	 * in URLs.
	 * 
	 * Filters must be set via setFileFilters prior to calling this method in order to filter out
	 * un-wanted files and directories.
	 * 
	 * @param dir the name of the directory
	 * @param getSubDirs 'true' to get a list of sub-directories
	 * @return true if files were found and were successfully converted to URLs. Otherwise, an IOException will be thrown.
	 * @throws IOException 
	 */
	public boolean visitDir(File dir, boolean getSubDirs) throws IOException {
		
		if( !dir.isDirectory() )
			throw new IllegalArgumentException("parameter 'dir' is not a directory: " + dir);
		
		//Find files only first. Convert resulting list into URLs
		List list = new ArrayList(FileUtils.listFiles(dir, effFileFilter, null));
		files.addAll( Arrays.asList(FileUtils.toURLs(FileUtils.convertFileCollectionToFileArray(list))) );
		
		//Visit sub-directories if the recurse flag is set
		if(getSubDirs)
			subDirs.addAll(Arrays.asList(FileUtils.toURLs(dir.listFiles(effDirFilter))) );
		
		return true;
	}
		
	/**
	 * Crawls a directory URL, looking for files and subdirectories. The file and directory URLS
	 * can be retrieved by calling the getFiles and getSubDirs methods, respectively.
	 * 
	 * Filters must be set via the setFileFilters method prior to crawling in order to filter out
	 * un-wanted files and directories.
	 * 
	 * @param url The URL to crawl
	 * @param getSubDirURLs Set to 'true' to retrieve sub-directory URLs, 'false' otherwise
	 * @return true if crawling operation was successful. Otherwise an exception will be thrown.
	 * @throws HttpException
	 * @throws IOException
	 * @throws BadLocationException
	 */
	public boolean crawl(URL url, boolean getSubDirURLs) throws HttpException, IOException, BadLocationException {
		Set links = new LinkedHashSet();
		
		links.addAll(getHyperLinks(url));
		files.addAll(getFileURLNames(url, links));
		
		if(getSubDirURLs) {
			subDirs.addAll(getSubDirURLNames(url, links));
		}
		
		return true;
	}
	
	/**
	 * Gets hyperlinks to files and directories found in an HTML document of a URL. No duplicate links will be returned.
	 * 
	 * @param url
	 * @return A Set of hyperlinks that point to files and directories
	 * 
	 * @throws IOException
	 * @throws HttpException 
	 * @throws BadLocationException 
	 */
	private Set getHyperLinks(URL url) throws HttpException, IOException, BadLocationException {
		HttpClient client = new HttpClient();
		GetMethod httpGet = new GetMethod(url.toString());
		HTMLDocument doc = null;
		EditorKit kit = null;
		Set links = new LinkedHashSet();
		
		try {
			client.executeMethod(httpGet);
			kit = new HTMLEditorKit();
			doc = (HTMLDocument) kit.createDefaultDocument();
			kit.read(httpGet.getResponseBodyAsStream(), doc, 0);
		}
		finally {
			httpGet.releaseConnection();
		}

		for(HTMLDocument.Iterator i = doc.getIterator(HTML.Tag.A); i.isValid(); i.next()) {
			SimpleAttributeSet s = (SimpleAttributeSet) i.getAttributes();
			links.add((String) s.getAttribute(HTML.Attribute.HREF));
		}
		return links;
	}
	
	/**
	 * Finds links to files. This assumes that a file must end in a ".xxx", otherwise
	 * it will not be retrieved.
	 * 
	 * @param url The location
	 * @param links The Set of files and directories found inside the URL
	 * @return a list of file URLs
	 * @throws MalformedURLException
	 */
	public List getFileURLNames(URL url, Set links) throws MalformedURLException {
		List fileURLs = new ArrayList();
		String parent = url.toString();
		
		if(parent.endsWith("/") == false)
			parent = parent.concat("/");
		
		for(Iterator i = links.iterator(); i.hasNext();) {
			String link = i.next().toString();
			if(isLinkFile(link)) {
				if(effFileFilter.accept(new File(link)) == true)
					fileURLs.add(new URL(parent.concat(link)));
			}
		}
		return fileURLs;
	}
	
	/**
	 * Finds links to sub-directory URLs
	 * 
	 * @param url
	 * @param links The Set of files and directories found inside the URL
	 * @return a list of sub directory URLs
	 * @throws MalformedURLException
	 */
	public List getSubDirURLNames(URL url, Set links) throws MalformedURLException {
		List dirURLs = new ArrayList();
		String parent = url.toString();
		
		if(parent.endsWith("/") == false)
			parent = parent.concat("/");
		
		for(Iterator i = links.iterator(); i.hasNext();) {
			String link = i.next().toString();
			if(isLinkSubDir(url, link)) {
				if(noDirFilter == null)
					dirURLs.add(new URL(parent.concat(link)));
				else if( (noDirFilter != null) && (noDirFilter.accept(new File(link)) == true) )
					dirURLs.add(new URL(parent.concat(link)));
			}
		}
		return dirURLs;
	}
	
	/**
	 * Determines if a hyperlink is a file. The rule is that if the name ends with a ".xxx",
	 * then it is a file. Otherwise, false is returned.
	 * 
	 * @param link The hyperlink name to examine
	 * @return
	 */
	public boolean isLinkFile(String link) {
		String ext = FilenameUtils.getExtension(link);
		if(ext.length() == fileExt) {
			return true;
		}
		else
			return false;
			
	}
	
	/**
	 * Determines if a hyperlink is a sub-directory.
	 * 
	 * @param url The location
	 * @param link The hyperlink name to examine
	 * @return
	 */
	public boolean isLinkSubDir(URL url, String link) {
		if( !isLinkFile(link) && link.indexOf('#') == -1 && link.indexOf('?') == -1) {
			//Check to see if the directory link is a hyperlink to the parent
			String parent = new File(url.getFile()).getParent();
			if( parent.equalsIgnoreCase(new File(link).toString()) )
				return false;
			else
				return true;
		}
		else
			return false;
			
	}
	
}

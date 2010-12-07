package gov.nasa.pds.tools.util;

public class Utility {
    /**
     * Strips only newline characters and extra whitespaces.
     * 
     * @param value
     *            A string value.
     * @return The filtered value.
     */
    public static String stripOnlyWhitespaceAndNewLine(String value) {
        String filteredValue = value;

        // Next replace all line.separator with " "
        filteredValue = filteredValue.replaceAll(
                System.getProperty("line.separator"), " ");
        // Replace multiple spaces with a single space
        filteredValue = filteredValue.replaceAll("\\s+", " ");
        // Trim whitespace
        filteredValue = filteredValue.trim();

        return filteredValue;
    }
}

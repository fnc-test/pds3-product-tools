package gov.nasa.pds.tools.dict.validate;

import gov.nasa.pds.tools.dict.Dictionary;

// This is a stub, brainstorm of checks performed below: 
// * identity and namespace length
// * duplicate definitions - values that refer to an internally defined list not matching up
// * required and optional elements actually defined
// * line length, line ending, disallowed chars, the usual label checking
public class DictionaryValidator {

    public void validate(final Dictionary dictionary) {
        if (dictionary == null) {
            return;
        }
    }

    // validate a local dd in te context of extending/overriding a master
    // TODO: do this way or just use file/url context to prevent surfacing
    // errors from parent in merged dictionary...
    public void validate(final Dictionary dictionary, final Dictionary master) {
        if (dictionary == null) {
            return;
        }
    }

}

// Copyright 2006-2010, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$
//

package gov.nasa.pds.tools.dict;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class ContainerDefinition extends Definition {

    private static final long serialVersionUID = 5276472073799175326L;

    private final List<DictIdentifier> required = new ArrayList<DictIdentifier>();

    private final List<DictIdentifier> optional = new ArrayList<DictIdentifier>();

    public ContainerDefinition(Dictionary sourceDictionary, int lineNumber,
            DictIdentifier identifier) {
        super(sourceDictionary, lineNumber, identifier);
    }

    public boolean isRequired(final DictIdentifier identifier) {
        return this.required.contains(identifier);
    }

    public boolean isOptional(final DictIdentifier identifier) {
        return this.optional.contains(identifier);
    }

    public boolean isAllowed(final DictIdentifier identifier) {
        if (identifier.getType().equals(ElementDefinition.class)
                && this.optional.contains(WILDCARD_ELEMENT)) {
            return true;
        }
        return this.required.contains(identifier)
                | this.optional.contains(identifier);
    }

    public List<DictIdentifier> getRequired() {
        return this.required;
    }

    public List<DictIdentifier> getOptional() {
        return this.optional;
    }

    public void addOptional(final DictIdentifier identifier) {
        if (!this.optional.contains(identifier)) {
            this.optional.add(identifier);
        }
    }

    public void addOptional(final List<DictIdentifier> optionalList) {
        for (final DictIdentifier identifier : optionalList) {
            addOptional(identifier);
        }
    }

    public void addRequired(final DictIdentifier identifier) {
        if (!this.required.contains(identifier)) {
            this.required.add(identifier);
        }

    }

    public void addRequired(final List<DictIdentifier> requiredList) {
        for (final DictIdentifier identifier : requiredList) {
            addRequired(identifier);
        }
    }

    public List<DictIdentifier> getRequiredElements() {
        List<DictIdentifier> requiredElements = new ArrayList<DictIdentifier>();
        for (DictIdentifier identifier : this.required) {
            if (identifier.getType().equals(ElementDefinition.class)) {
                requiredElements.add(identifier);
            }
        }
        return requiredElements;
    }

    public boolean hasRequiredElements() {
        return !this.getRequiredElements().isEmpty();
    }

    public List<DictIdentifier> getOptionalElements() {
        List<DictIdentifier> optionalElements = new ArrayList<DictIdentifier>();
        for (DictIdentifier identifier : this.optional) {
            if (identifier.getType().equals(ElementDefinition.class)) {
                optionalElements.add(identifier);
            }
        }
        return optionalElements;
    }

    public boolean hasOptionalElements() {
        return !this.getOptionalElements().isEmpty();
    }

    public boolean allowsAnyElement() {
        return this.optional.contains(WILDCARD_ELEMENT);
    }
}

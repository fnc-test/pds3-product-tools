// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// • Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// • Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// • Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.tools.dict.type;

import gov.nasa.pds.tools.dict.ElementDefinition;
import gov.nasa.pds.tools.label.AttributeStatement;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public interface TypeChecker {
    /**
     * Tries to cast the value to the appropriate type and return the value
     * 
     * @param value
     *            which must be cast
     * @return value after it has been cast
     * @throws InvalidTypeException
     *             thrown when value can not be cast
     */
    public Object cast(String value, AttributeStatement attribute)
            throws InvalidTypeException;

    /**
     * Checks to make sure that the value does fall below the minimum length
     * length
     * 
     * @param value
     *            to be checked
     * @param def
     *            contains the bound
     * @throws InvalidLengthException
     *             thrown when value falls below minimum
     */
    public void checkMinLength(String value, ElementDefinition def,
            AttributeStatement attribute) throws InvalidLengthException;

    /**
     * Checks to make sure that the value does not exceed the maximum length
     * 
     * @param value
     *            to be checked
     * @param def
     *            contains the bound
     * @throws InvalidLengthException
     *             thrown when value exceeds maximum
     */
    public void checkMaxLength(String value, ElementDefinition def,
            AttributeStatement attribute) throws InvalidLengthException;
}

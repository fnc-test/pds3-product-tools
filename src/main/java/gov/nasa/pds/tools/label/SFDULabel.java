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

package gov.nasa.pds.tools.label;

import java.nio.ByteBuffer;

/**
 * @author pramirez
 * @author jagander
 * @version $Revision$
 * 
 */
public class SFDULabel {
    private String sfdu;
    private String controlAuthorityId;
    private String versionId;
    private String classId;
    private String delimiterType;
    private String spare;
    private String descriptionDataUnitId;
    private String length;

    public SFDULabel(byte[] label) throws MalformedSFDULabel {
        if (label.length != 20)
            throw new MalformedSFDULabel(
                    "SFDU Label is not the correct length. Must be 20 bytes long");
        ByteBuffer sfduBuffer = ByteBuffer.wrap(label);
        this.sfdu = new String(label);
        byte[] controlAuthorityIdByte = new byte[4];
        byte[] versionIdByte = new byte[1];
        byte[] classIdByte = new byte[1];
        byte[] delimiterTypeByte = new byte[1];
        byte[] spareByte = new byte[1];
        byte[] descriptionDataUnitIdByte = new byte[4];
        byte[] lengthByte = new byte[8];

        sfduBuffer.get(controlAuthorityIdByte, 0, 4);
        this.controlAuthorityId = new String(controlAuthorityIdByte);
        sfduBuffer.get(versionIdByte, 0, 1);
        this.versionId = new String(versionIdByte);
        sfduBuffer.get(classIdByte, 0, 1);
        this.classId = new String(classIdByte);
        sfduBuffer.get(delimiterTypeByte, 0, 1);
        this.delimiterType = new String(delimiterTypeByte);
        sfduBuffer.get(spareByte, 0, 1);
        this.spare = new String(spareByte);
        sfduBuffer.get(descriptionDataUnitIdByte, 0, 4);
        this.descriptionDataUnitId = new String(descriptionDataUnitIdByte);
        sfduBuffer.get(lengthByte, 0, 8);
        this.length = new String(lengthByte);
    }

    public String getControlAuthorityId() {
        return this.controlAuthorityId;
    }

    public String getVersionId() {
        return this.versionId;
    }

    public String getClassId() {
        return this.classId;
    }

    public String getDelimiterType() {
        return this.delimiterType;
    }

    public String getSpare() {
        return this.spare;
    }

    public String getDescriptionDataUnitId() {
        return this.descriptionDataUnitId;
    }

    public String getLength() {
        return this.length;
    }

    @Override
    public String toString() {
        return this.sfdu;
    }
}

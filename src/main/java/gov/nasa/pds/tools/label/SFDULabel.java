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

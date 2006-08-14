//Copyright (c) 2005, California Institute of Technology.
//ALL RIGHTS RESERVED. U.S. Government sponsorship acknowledged.
//
// $Id$ 
//

package gov.nasa.pds.tools.label;

import java.nio.ByteBuffer;

/**
 * @author pramirez
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
            throw new MalformedSFDULabel("SFDU Label is not the correct length. Must be 20 bytes long");
        ByteBuffer sfdu = ByteBuffer.wrap(label);
        this.sfdu = new String(label);
        byte [] controlAuthorityId = new byte[4];
        byte [] versionId = new byte[1];
        byte [] classId = new byte[1];
        byte [] delimiterType = new byte[1];
        byte [] spare = new byte[1];
        byte [] descriptionDataUnitId = new byte[4];
        byte [] length = new byte[8];

        sfdu.get(controlAuthorityId, 0, 4);
        this.controlAuthorityId = new String(controlAuthorityId);
        sfdu.get(versionId, 0, 1);
        this.versionId = new String(versionId);
        sfdu.get(classId, 0, 1);
        this.classId = new String(classId);
        sfdu.get(delimiterType, 0, 1);
        this.delimiterType = new String(delimiterType);
        sfdu.get(spare, 0, 1);
        this.spare = new String(spare);
        sfdu.get(descriptionDataUnitId, 0, 4);
        this.descriptionDataUnitId = new String(descriptionDataUnitId);
        sfdu.get(length, 0, 8);
        this.length = new String(length);
    }

    public String getControlAuthorityId() {
        return controlAuthorityId;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getClassId() {
        return classId;
    }

    public String getDelimiterType() {
        return delimiterType;
    }

    public String getSpare() {
        return spare;
    }

    public String getDescriptionDataUnitId() {
        return descriptionDataUnitId;
    }
    
    public String getLength() {
        return length;
    }
    
    public String toString() {
        return sfdu;
    }
}

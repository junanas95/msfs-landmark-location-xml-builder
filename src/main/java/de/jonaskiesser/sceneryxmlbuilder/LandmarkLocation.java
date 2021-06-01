
package de.jonaskiesser.sceneryxmlbuilder;

import java.util.UUID;

/**
 * @author jonas
 */
public class LandmarkLocation {

    private UUID instanceID;
    private String type;
    private String name;
    private String LatString;
    private String LonString;
    private String alt;

    public LandmarkLocation(String type, String name, String LatString, String LonString, String alt) {
        this.instanceID = UUID.randomUUID();
        this.type = type;
        this.name = name;
        this.LatString = LatString;
        this.LonString = LonString;
        this.alt = alt;
    }

    public String toXML() {
        return "<LandmarkLocation instanceId=\"{" + instanceID + "}\" type=\"" + type + "\" name=\"" + name + "\" lat=\"" + LatString + "\" lon=\"" + LonString + "\" alt=\"" + alt + "\"/>";
    }

    @Override
    public String toString() {
        return "LandmarkLocation{" + "instanceId=" + instanceID + ", type=" + type + ", name=" + name + ", LatString=" + LatString + ", LonString=" + LonString + ", alt=" + alt + '}';
    }

}

package plugin.sqlInXml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Created by BOGONm on 16/8/8.
 */
@XmlRootElement
public class SqlItem {
    @XmlAttribute
    String id;

    @XmlValue
    String value;
}

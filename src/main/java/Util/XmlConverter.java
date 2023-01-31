package Util;

import lombok.experimental.UtilityClass;

import javax.xml.bind.*;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;

@UtilityClass
public class XmlConverter {

    /* мапим java объект в xml строку */
    public static String fromObjectToXml(JAXBElement<?> xmlString) throws IOException, JAXBException {
        String xmlMessage;

        try (StringWriter sw = new StringWriter()) {
            JAXBContext jc = JAXBContext.newInstance(xmlString.getDeclaredType());
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
            marshaller.marshal(xmlString, sw);
            xmlMessage = sw.toString();
        }

        return xmlMessage;
    }

    /* мапим xml документ в java объект */
    public static <T> T fromXmlToObject(String xmlObject, Class<T> clazz) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(xmlObject));
            return unmarshaller.unmarshal(xsr, clazz).getValue();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка преобразования входящего XML документа в объект", e);
        }
    }

    public static LocalDate gregorianToLocalDate(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar != null
                ? xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDate()
                : null;
    }

    public static <T> T jaxbToObjNotNull(JAXBElement<T> jaxbElement) {
        return jaxbElement != null ? jaxbElement.getValue() : null;
    }
}
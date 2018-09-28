package dropwlab.delaval.com.core.vc.listener;

import dropwlab.delaval.com.api.Event;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

public interface EventService {
    /**
     * Start listen to VC
     */
    void startListenToVC();

    /**
     * Find events from VC
     *
     * @return List of Event
     * @param numberOfCalls
     */
    List<Event> findEvents(int numberOfCalls) throws InterruptedException, ParserConfigurationException, IOException, SAXException, TransformerException;

    /**
     * Find events from VC
     *
     * @return events as String
     */
    default String findEvent() {
        return "";
    }

    /**
     * Transform vc-event in xml to xml-file
     *
     * @param xmlDocument of type Document
     * @param fileName    of type String
     */
    default void writeXmlDocumentToXmlFile(Document xmlDocument, String fileName) {
    }

    /**
     * Build an XmlDocument from a String
     *
     * @param text of type String
     */
    default Document convertStringToXMLDocument(String text) throws TransformerException, ParserConfigurationException, IOException, SAXException {
        return null;
    }
}

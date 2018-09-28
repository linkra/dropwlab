package dropwlab.delaval.com.core.vc.listener;

import dropwlab.delaval.com.api.Event;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

public class EventServiceImpl implements EventService {
    private static final Logger logger = Logger.getLogger(EventServiceImpl.class.getName());
    private static final String outputXml = "eventsFromVC.xml";
    private static final String outputXmlTmp = "eventFromVCTmp.xml";
    private VCListener vcListener;

    public EventServiceImpl() {
    }

    @Override
    public void startListenToVC() {
        vcListener = new VCListener();
    }

    @Override
    public List<Event> findEvents(int numberOfCalls) throws ParserConfigurationException, IOException, SAXException, InterruptedException {
        List<Event> foundEvents = new ArrayList<>();
        for (int i = 0; i < numberOfCalls; i++) {
            Thread.sleep(10000);
            String vcEvent = vcListener.getEvent();
            Document xmlDocument = createXMLFile(vcEvent); // For now - always just 1 event here
            ArrayList<Event> eventList = new ArrayList<>(VCDocumentParser.parseEventDocument(xmlDocument));
            addingToEventList(foundEvents, eventList);
        }
        return foundEvents;
    }

    private void addingToEventList(List<Event> foundEvents, ArrayList<Event> eventList) {
        if (eventList.size() > 0 && eventList.size() < 2) {
            foundEvents.add(eventList.get(0));
        } else if (eventList.size() > 1){
            logger.info("NOTE: more than 1 events in arrayList!!  There are: " + eventList.size() + " events found");
        } else {
            logger.info("NOTE: No events found!");
        }
    }

    private Document createXMLFile(String vcEvent) throws ParserConfigurationException, IOException, SAXException {
        if (vcEvent != null) {
            logger.info("Found event: " + vcEvent);
            vcEvent = vcEvent.replaceAll("\t", "").replaceAll("\n", "").replaceAll("\r", "").replaceAll("\\s", "").replaceAll("--", "").trim();
            Document document = convertStringToXMLDocument(vcEvent);
            writeXmlDocumentToXmlFile(document, outputXml);
            logger.info("Did write xml to:  " + vcEvent);
            return document;
        }
        return null;
    }

    @Override
    public void writeXmlDocumentToXmlFile(Document xmlDocument, String fileName) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();

            // Not require XML declaration
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            // Write XML to file
            FileOutputStream outStream = new FileOutputStream(new File(outputXmlTmp));
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(outStream));
            Appender.appendWellFormedXml(outputXmlTmp, outputXml);
        } catch (TransformerException e) {
            logger.info("ERROR: TransformerException occurred! " + e.getMessageAndLocation());
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("ERROR: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Document convertStringToXMLDocument(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
    }
}

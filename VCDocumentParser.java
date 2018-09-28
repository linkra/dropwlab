package dropwlab.delaval.com.core.vc.listener;

import dropwlab.delaval.com.api.Event;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class VCDocumentParser {

    static Set<Event> parseEventDocument(Document xmlDocument) {
        Set<Event> events = new HashSet<>();
        if (xmlDocument != null) {
            Element documentElement = xmlDocument.getDocumentElement();

            if (documentElement != null) {
                String methodTag = documentElement.getTagName();
                // Create new Event
                Event event = new Event();
                event.setMethod(methodTag);
                event.setId(UUID.randomUUID().toString());
                NodeList childNodes = documentElement.getChildNodes();

                if (childNodes != null) {
                    int childNodesLength = childNodes.getLength();
                    for (int i = 0; i < childNodesLength; i++) {
                        if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) childNodes.item(i);
                            if (element != null) {
                                String tagName = element.getTagName();
                                if (tagName.equalsIgnoreCase("category")) {
                                    event.setCategory(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("device")) {
                                    event.setDevice(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("deviceeventid")) {
                                    event.setDeviceEventId(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("supportingdata1")) {
                                    event.setSupportingData1(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("supportingdata2")) {
                                    event.setSupportingData2(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("supportingdata3")) {
                                    event.setSupportingData3(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("supportingdata4")) {
                                    event.setSupportingData4(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("supportingdata5")) {
                                    event.setSupportingData5(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("supportingdata6")) {
                                    event.setSupportingData6(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("creatordevice")) {
                                    event.setCreatorDevice(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("sourcedevice")) {
                                    event.setSourceDevice(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("majorcode")) {
                                    event.setMajorCode(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("minorcode")) {
                                    event.setMinorCode(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("severity")) {
                                    event.setSeverity(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("animal")) {
                                    event.setAnimal(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("direction")) {
                                    event.setDirection(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("gatepassingok")) {
                                    event.setGatePassingOk(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("objectguid")) {
                                    event.setObjectGuid(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("_message")) {
                                    event.setMessage(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("_method")) {
                                    event.setMethod(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("_result")) {
                                    event.setResult(element.getTextContent());
                                }
                                if (tagName.equalsIgnoreCase("eventdatetime")) {
                                    event.setEventDateTime(LocalDateTime.parse(element.getTextContent()));
                                }
                            }
                        }
                        events.add(event);
                    }
                }
            }
        }
        return events;
    }
}

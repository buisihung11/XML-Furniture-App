/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import utils.XMLUtils;

/**
 *
 * @author Admin
 */
public class ParseXMLNotWellFormed {

    private LinkedList<XMLEvent> lEvents = new LinkedList<XMLEvent>();

    private String filePath;

    public LinkedList<XMLEvent> getlEvents() {
        return lEvents;
    }

    public void setlEvents(LinkedList<XMLEvent> lEvents) {
        this.lEvents = lEvents;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    
    
    public void autoParseFileEventNotWellForm() throws FileNotFoundException, XMLStreamException {
        XMLEventReader reader = XMLUtils.parseFileToXMLEvent(filePath);

        LinkedList<Integer> lStartTagPos = new LinkedList<Integer>();
        boolean flagRemoved = false;
        while (reader.hasNext()) {
            XMLEvent event = null;
            try {
                event = reader.nextEvent();
                if (flagRemoved) {
                    lEvents = XMLUtils.removeListFrom(lEvents, lStartTagPos.getLast());
                    lStartTagPos.removeLast();
                    flagRemoved = false;
                } // xu li truong hop thieu the dong
            } catch (XMLStreamException e) {
                if (lStartTagPos.size() != 0) {
                    if (flagRemoved) {
                        String msgErr = e.getMessage();
                        msgErr = msgErr.substring(msgErr.indexOf(":[") + 2,
                                msgErr.indexOf("]\n"));
                        int row = Integer.parseInt(msgErr.substring(0, msgErr.indexOf(",")));
                        int col = Integer.parseInt(msgErr.substring(msgErr.indexOf(",") + 1));

                        StartElement element = (StartElement) lEvents.get(lStartTagPos.getLast());
                        reader = XMLUtils.remakeFile(filePath,
                                element.getLocation().getLineNumber(),
                                element.getLocation().getColumnNumber() - 1,
                                row,
                                col - 3
                        );
                        // col -1 = postion of tag '>'

                        row = 0;
                        col = 0;
                        lEvents.removeAll(lStartTagPos);
                        lStartTagPos.removeAll(lStartTagPos);
                        flagRemoved = false;

                    }// end if flagRemoved
                    // xu ly truong hop 2
                    else {
                        flagRemoved = true;
                    }
                } // endif lStartTagPos.size() != 0
                else {
                    break;
                }
            } catch (Exception e) {
                break;
            }
            if (event != null) {
                if (event.isStartElement()) {
                    lStartTagPos.add(lEvents.size());
                }
                if (event.isEndElement()) {
                    lStartTagPos.removeLast();
                }
                lEvents.add(event);
            }
        } // end while reader.hasnext()
    }

}

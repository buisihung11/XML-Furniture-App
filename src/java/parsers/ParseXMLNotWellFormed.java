/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.xml.sax.SAXException;
import utils.GetHTML;
import utils.TextUtils;
import utils.XMLUtils;

/**
 *
 * @author Admin
 */
public class ParseXMLNotWellFormed {

    private String SELF_CLOSING_TAG = "<\\s*([^\\s>]+)([^>]*)/\\s*>";
    private LinkedList<XMLEvent> lEvents = new LinkedList<XMLEvent>();

    private String filePath;
    private String src;
    private boolean isFromFile = false;

    public LinkedList<XMLEvent> getlEvents() {
        return lEvents;
    }

    public static void main(String[] args) throws Exception {
//        GetHTML.getHTMLToFile("test.txt", "https://www.onekingslane.com/c/furniture.do");
        System.out.println("BAT DAU PARSE");
        ParseXMLNotWellFormed parseXMl = new ParseXMLNotWellFormed();
        parseXMl.setSrc(GetHTML.getHTMLToString("https://www.onekingslane.com/c/furniture.do"));
//        parseXMl.autoParseFileEventNotWellForm();
        System.out.println("PARSEDDDDD");
    }

    public void autoParseFileEventNotWellForm() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
        XMLEventReader reader;
        if (isFromFile) {
            System.out.println("PARSE WITH FILE");
            reader = XMLUtils.parseFileToXMLEvent(filePath);
        } else {
            System.out.println("PARSE WITH SRC");
//            src = TextUtils.removeSelfClosing(src);
            // replace self-closing tag
            reader = XMLUtils.parseStringToXMLEvent(src);
        }

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
                if (!lStartTagPos.isEmpty()) {
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
                System.out.println("PARSER LOI");
                e.printStackTrace();
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

    public void setlEvents(LinkedList<XMLEvent> lEvents) {
        this.lEvents = lEvents;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isIsFromFile() {
        return isFromFile;
    }

    public void setIsFromFile(boolean isFromFile) {
        this.isFromFile = isFromFile;
    }

    public String getParsedSrc() {
        Iterator<XMLEvent> iterator = this.lEvents.iterator();
        String result = "";
        while (iterator.hasNext()) {
            XMLEvent event = iterator.next();
            if (event.isStartElement()) {
                StartElement se = (StartElement) event;
                result += "<" + se.getName().toString();
                Iterator childIter = se.getAttributes();
                while (childIter.hasNext()) {
                    Attribute attr = (Attribute) childIter.next();
                    String value = attr.getValue().replace("&", "&#38");
                    result += " " + attr.getName().toString() + "=" + "\"" + value + "\"";
                }
                result += ">";
            } // endif event.isStartElement
            if (event.isCharacters()) {
                Characters chars = (Characters) event;
                if (!chars.isWhiteSpace()) {
                    result += chars.getData().replace("&", "&#38").trim();
                }
            } // endif event.isCharacter
            if (event.isEndElement()) {
                EndElement end = (EndElement) event;
                result += end.toString() + "\n";
            } // endif event.isEndEleemnt
        }
        return result;
    }

}

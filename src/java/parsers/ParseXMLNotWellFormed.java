/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
        String filePath = "StudentAccount.xml";
        System.out.println("BAT DAU PARSE");

        String src = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "\n"
                + "<students>\n"
                + "    <student id=\"SE01\" class=\"se1274\">\n"
                + "	<img data-listing-card-listing-image=\"\" src=\"https://i.etsystatic.com/25510629/d/il/b3a5b9/2652782619/il_340x270.2652782619_om0y.jpg?version=0\" class=\"width-full wt-height-full display-block position-absolute \" alt=\"\" >\n"
                + "        <lastname>\n"
                + "            Dang\n"
                + "        </lastname>\n"
                + "        <middlename>\n"
                + "            Quoc\n"
                + "        </middlename>\n"
                + "        <firstname>\n"
                + "            Thai\n"
                + "        <!--</firstname>-->\n"
                + "        <sex>\n"
                + "            1\n"
                + "        </sex>\n"
                + "        <password>\n"
                + "            123456\n"
                + "        </password>\n"
                + "        <address>\n"
                + "            tpHCM\n"
                + "        </address>\n"
                + "		<br />\n"
                + "        <status>\n"
                + "            studying\n"
                + "        </status>\n"
                + "    </student>\n"
                + "    <student id= \"SE02\" class=\"se1274\" >\n"
                + "        <lastname>\n"
                + "            Nguyen\n"
                + "        </lastname>\n"
                + "        <middlename>\n"
                + "            Hieu\n"
                + "        </middlename>\n"
                + "        <firstname>\n"
                + "            Liem\n"
                + "        </firstname>\n"
                + "        <sex>\n"
                + "            2\n"
                + "        </sex>\n"
                + "        <password>\n"
                + "            1234956\n"
                + "        </password>\n"
                + "        <address>\n"
                + "            tpHCM\n"
                + "        </address>\n"
                + "        <status>\n"
                + "            dropout\n"
                + "        </status>\n"
                + "    </student>\n"
                + "        <student id=\"SE03\" class=\"se1274\">\n"
                + "        <lastname>\n"
                + "            Nguyen\n"
                + "        </lastname>\n"
                + "        <middlename>\n"
                + "            Hieu\n"
                + "        </middlename>\n"
                + "        <firstname>\n"
                + "            Hoa\n"
                + "        </firstname>\n"
                + "        <sex>\n"
                + "            3\n"
                + "        </sex>\n"
                + "        <password>\n"
                + "            1230456\n"
                + "        </password>\n"
                + "        <address>\n"
                + "            tpHCM\n"
                + "        </address>\n"
                + "        <status>\n"
                + "            break\n"
                + "        </status>\n"
                + "    </student>\n"
                + "</students>";

        try {
            DOMParser.parseStringToDom(src);
        } catch (ParserConfigurationException parseEx) {
            parseEx.printStackTrace();
        } catch (SAXException sax) {
            sax.printStackTrace();
        }

//        ParseXMLNotWellFormed parseXMl = new ParseXMLNotWellFormed();
//        parseXMl.setSrc(src);
//        parseXMl.autoParseFileEventNotWellForm();
//        System.out.println("PARSEDDDDD");
////        XMLUtils.printAllData(parseXMl.getlEvents().iterator());
//        System.out.println(parseXMl.getParsedSrc());

//        Spider esty = new EstySpider("ESTY-SPIDER", "https://www.etsy.com/c/home-and-living/furniture");
//        esty.startExecution();
    }

    public void autoParseFileEventNotWellForm() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
        XMLEventReader reader;
        if (isFromFile) {
            System.out.println("PARSE WITH FILE");
            reader = XMLUtils.parseFileToXMLEvent(filePath);
        } else {
            System.out.println("PARSE WITH SRC");
            String temp = TextUtils.replaceSelfClosing(src);
            // replace self-closing tag
            reader = XMLUtils.parseStringToXMLEvent(temp);
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

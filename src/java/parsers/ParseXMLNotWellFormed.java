/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public static final List<String> INLINE_TAGS = Arrays.asList(
            "area", "base", "br", "col", "command", "embed", "hr", "img", "input",
            "keygen", "link", "meta", "param", "source", "track", "wbr"
    );
    private String SELF_CLOSING_TAG = "<\\s*([^\\s>]+)([^>]*)/\\s*>";
    private LinkedList<XMLEvent> lEvents = new LinkedList<XMLEvent>();

    private String filePath;
    private String src;
    private boolean isFromFile = false;

    public LinkedList<XMLEvent> getlEvents() {
        return lEvents;
    }

    public void autoParseFileEventNotWellForm() throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
        XMLEventReader reader;
        if (isFromFile) {
            System.out.println("PARSE WITH FILE");
            reader = XMLUtils.parseFileToXMLEvent(filePath);
        } else {
            System.out.println("PARSE WITH SRC");
            String temp = replaceSelfClosing(src);
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

    private String replaceSelfClosing(String src) {
        System.out.println("Before");
        System.out.println(src);
        for (String inlineTag : INLINE_TAGS) {
            String reg = "<\\s*(" + inlineTag + ")([^>]*)\\/?\\s*>";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(src);
            while (m.find()) {
                String group1 = m.group(2);
                System.out.println("Group 1 " + group1);
                if (group1.trim().equals("/")) {
                    src = m.replaceFirst("<$1></$1>");
                } else {
                    src = m.replaceFirst("<$1$2></$1>");
                }
            }
        }
        // for unknown tag
//        Pattern p = Pattern.compile(SELF_CLOSING_TAG);
//        Matcher m = p.matcher(src);
//        src = m.replaceAll("<$1$2></$1>");
        System.out.println("After");
        System.out.println(src);
        return src;
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

}

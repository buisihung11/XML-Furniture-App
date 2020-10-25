/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import parsers.ParseXMLNotWellFormed;
import spider.Spider;

/**
 *
 * @author Admin
 */
public class XMLUtils {

    public static void main(String[] args) throws Exception {
        String url = "https://www.onekingslane.com/home.do";
        Spider spider = new Spider("OneKingLane", url);
        spider.startExecution();
//        XMLEventReader reader = spider.splitSrcToXML(new String[]{
//            "<div class=\"tab-pane nav-active Nav-100002\"",
//            "</div>"});
        FileUtil.saveSrcToFile("categories.html", splitSection(spider.src, new String[]{
            "<div class=\"tab-pane nav-active Nav-100002\"",
            "</div>"}));
    }

    public static String splitSection(String src, String[] containerTag) throws MalformedURLException, IOException {
        String content = "";
        String startContainerTag = containerTag[0];
        String endContainerTag = containerTag[1];
        try {
            Reader inputString = new StringReader(src);
            BufferedReader bf = new BufferedReader(inputString);
            String inputLine;
            boolean isFound = false;
            while ((inputLine = bf.readLine()) != null) {
                if (inputLine.contains(startContainerTag)) {
                    isFound = true;
                }
                if (isFound) {
                    String temp = inputLine.trim();
                    temp = temp.replaceAll("</", "\n</")
                            .replaceAll(">", ">\n");
                    content += temp;
                }
                if (isFound && inputLine.equals(endContainerTag)) {
                    break;
                }
            }
            bf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseXMLNotWellFormed.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
        return content;
    }

    public static String splitSection(String src, String[] containerTag, String[] beforeEndTag) throws UnsupportedEncodingException, IOException {
        String content = "";
        String startContainerTag = containerTag[0];
        String endContainerTag = containerTag[1];
        String startBeforeTag = beforeEndTag[0];
        String endBeforeTag = beforeEndTag[1];
        try {
            Reader inputString = new StringReader(src);
            BufferedReader bf = new BufferedReader(inputString);
            String inputLine;
            boolean isFound = false;
            boolean isStartEnd = false;
            boolean isDivEnd = false;
            while ((inputLine = bf.readLine()) != null) {
                if (inputLine.contains(startContainerTag)) {
                    isFound = true;
                }
                if (inputLine.contains(startBeforeTag)) {
                    isStartEnd = true;
                }
                if (isFound) {
                    String temp = inputLine.trim();
                    temp = temp.replaceAll("</", "\n</")
                            .replaceAll(">", ">\n");
                    content += temp;
                }
                if (isDivEnd && inputLine.contains(endContainerTag)) {
                    break;
                }
                if (isStartEnd && inputLine.contains(endBeforeTag)) {
                    isDivEnd = true;
                }
            }
            bf.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseXMLNotWellFormed.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

        }
        return content;
    }

    public static XMLEventReader parseFileToXMLEvent(String filePath) throws FileNotFoundException, XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(new FileInputStream(filePath));
        return reader;
    }

    public static XMLEventReader parseStringToXMLEvent(String xml) throws FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
        byte[] byteArray = xml.getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        XMLEventReader reader = inputFactory.createXMLEventReader(inputStream);
        return reader;
    }

    public static XMLEventReader remakeFile(String filePath, int startRow,
            int posClose, int endRow, int posOpen) {
        List<String> lines = new ArrayList<String>();
        String line = "";
        File file = new File(filePath);
        boolean flagRemoved = false;
        try {
            int curRow = 0;
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            // Readfile, remvoe content error 
            while ((line = br.readLine()) != null) {
                curRow++;
                if (curRow == startRow) {
                    line = removeErrorTagWithPosClose(line, posClose);
                    lines.add(line);
                    flagRemoved = true;
                } // end curRow == startRow
                if (!flagRemoved) {
                    lines.add(line);
                }
                if (curRow == endRow) {
                    line = remmoveErrorTagWithPosOpen(line, posOpen);
                    flagRemoved = false;
                    lines.add(line);
                }// end curRow == endRow
            }
            fr.close();
            br.close();

            // Write new file
            FileWriter fw = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines) {
                if (!s.trim().equals("")) {
                    out.write(s + "\n");
                }
            }
            out.flush();
            fw.close();
            out.close();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            return factory.createXMLEventReader(new FileInputStream(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String removeErrorTagWithPosClose(String line, int posClose) {
        int posOpen = 0;
        --posClose;

        // Position of close tag count by java location from 1,
        // but in arr from 0
        // so posClose - 1
        for (int i = posClose; i > 0; i--) {
            if (line.charAt(i) == '<') {
                posOpen = i;
                break;
            }
        }
        line = line.substring(0, posOpen);
        return line;
    }

    private static String remmoveErrorTagWithPosOpen(String line, int posOpen) {
        int posClose = 0;

        // Position of close tag count by java location from 1,
        // but in arr from 0
        // so posClose - 1
        for (int i = posOpen; i < line.length(); i++) {
            if (line.charAt(i) == '>') {
                posClose = i;
                break;
            }
        }
        line = line.substring(posClose + 1);
        return line;
    }

    public static void printAllData(Iterator<XMLEvent> iterator) {
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
                result += end.toString();
            } // endif event.isEndEleemnt
            System.out.println(result);
            result = "";
        }
    }

    public static LinkedList<XMLEvent> removeListFrom(LinkedList<XMLEvent> lEvents, Integer from) {
        int to = lEvents.size();
        for (int i = from; i < to; i++) {
            lEvents.removeLast();
        }
        return lEvents;

    }
}

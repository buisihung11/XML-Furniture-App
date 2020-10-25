/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import parsers.STAXParser;
import utils.FileUtil;
import utils.GetHTML;
import utils.Internet;
import utils.Pipeline;
import utils.XMLUtils;

/**
 *
 * @author Admin
 */
public class Spider {

    public String name;
    public String startURL;
    public String src;

    public Spider(String name, String startURL) {
        this.name = name;
        this.startURL = startURL;
    }

    public void startExecution() throws IOException, Exception {
        System.out.println("START SPIDER " + name);
        // 1. get raw HTML
        System.out.println("1. DOWNLOAD FILE AND REFINED");
        String rawHTML = GetHTML.getHTMLToString(startURL);
        this.src = rawHTML;
    }

    public void saveToFile(String filePath) {
        FileUtil.saveSrcToFile(filePath, src);
    }

    public XMLEventReader splitSrcToXML(String[] containerTag) throws IOException,
            FileNotFoundException, UnsupportedEncodingException, XMLStreamException {
        String splitedSrc = XMLUtils.splitSection(src, containerTag);
        System.out.println("SPLITED: ");
        System.out.println(splitedSrc);
        return XMLUtils.parseStringToXMLEvent(splitedSrc);
    }

    public XMLEventReader splitSrcToXML(String[] containerTag, String[] beforeEndTag) throws IOException,
            FileNotFoundException, XMLStreamException, UnsupportedEncodingException {
        String splitedSrc = XMLUtils.splitSection(src, containerTag, beforeEndTag);
        System.out.println("SPLITED: ");
        System.out.println(splitedSrc);
        return XMLUtils.parseStringToXMLEvent(splitedSrc);
    }

}

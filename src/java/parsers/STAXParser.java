/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Admin
 */
public class STAXParser {

    public static XMLStreamReader createSTAXCursorReaderFromFile(String filePath) throws Exception {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        File f = new File(filePath);
        InputStream is = new FileInputStream(f);
        XMLStreamReader reader = xif.createXMLStreamReader(is);
        return reader;
    }

    public static XMLEventReader createSTAXCursorReaderFromString(String xml) throws Exception {
        byte[] byteArray = xml.getBytes("UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        inputFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        XMLEventReader reader = inputFactory.createXMLEventReader(inputStream);
        return reader;
    }
}

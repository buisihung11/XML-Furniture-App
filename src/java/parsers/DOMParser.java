/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import java.io.File;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 *
 * @author Admin
 */
public class DOMParser {

    public static Document parseFileToDom(String filePath) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        File f = new File(filePath);
        Document doc = db.parse(f);
        return doc;
    }

    ;
    
     public static Document parseStringToDom(String src) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(src));
        Document doc = db.parse(is);
        return doc;
    }

    ;
    
    public static XPath createPath() throws Exception {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPath = xpf.newXPath();
        return xPath;
    }

    public static boolean transformDOMSourceToStreamResult(Node node, String filePath) throws Exception {
        Source src = new DOMSource(node);
        File file = new File(filePath);

        Result result = new StreamResult(file);

        TransformerFactory tff = TransformerFactory.newInstance();
        try {
            Transformer trans = tff.newTransformer();
            trans.transform(src, result);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
;
}

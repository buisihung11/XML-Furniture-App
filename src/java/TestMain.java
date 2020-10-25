
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import parsers.DOMParser;
import parsers.ParseXMLNotWellFormed;
import spider.Spider;
import utils.GetHTML;
import utils.Handler;
import utils.Pipeline;
import utils.TextUtils;
import utils.XMLUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Admin
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
        String url = "https://www.onekingslane.com/home.do";
        Spider spider = new Spider("OneKingLane", url);
        spider.startExecution();
        XMLEventReader reader = spider.splitSrcToXML(new String[]{
            "<ul class=\"nav navbar-nav ml-navbar-nav ml-navbar-menu cc_cursor\"",
            "</ul>"});
    }

}

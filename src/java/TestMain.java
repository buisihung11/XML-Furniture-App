
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
        String filePath = "StudentAccount.xml";
//        String[] urls = {"https://www.onekingslane.com/c/furniture.do",};
//        for (int i = 0; i < urls.length; i++) {
//            String url = urls[i];
//            GetHTML.getHTMLToFile("onekingslane-" + (i + 1) + ".html", url);
////            String refinedSrc = GetHTML.getHTMLToString(url);
////            System.out.println(url + ": \n :" + refinedSrc);
//        }
        System.out.println("BAT DAU PARSE");
        ParseXMLNotWellFormed parseXMl = new ParseXMLNotWellFormed();
        parseXMl.setFilePath(filePath);
        parseXMl.autoParseFileEventNotWellForm();
        XMLUtils.printAllData(parseXMl.getlEvents().iterator());

//        Spider esty = new EstySpider("ESTY-SPIDER", "https://www.etsy.com/c/home-and-living/furniture");
//        esty.startExecution();
    }

}

class EstySpider extends Spider<Object> {

    public EstySpider(String name, String startURL) {
        super(name, startURL);
    }

    @Override
    public void parse(String src) {
        try {
            System.out.println("SRC: " + src);
            Document doc = DOMParser.parseStringToDom(src);
//            if(doc != null)
//            {
//                XPath xPath = DOMParser.createPath();
//                String exp = "//student[contains(address, '" + address + "')]";
//                NodeList studentList  = (NodeList) xPath.evaluate(exp, doc, XPathConstants.NODESET);
//                if(studentList != null) {
//                    List<StudentDTO> list = new ArrayList<StudentDTO>();
//                    for (int i = 0; i < studentList.getLength(); i++) {
//                        Node node = studentList.item(i);
//                        StudentDTO dto = new StudentDTO();
//                        dto.setId(node.getAttributes().getNamedItem("id").getNodeValue());
//                        NodeList childrent = node.getChildNodes();
//                        for (int j = 0; j < childrent.getLength(); j++) {
//                            Node tmp = childrent.item(j);
//                            if(tmp.getNodeName().equals("firstname")) {
//                                dto.setFirstname(tmp.getTextContent().trim());
//                            } else if (tmp.getNodeName().equals("middlename")) {
//                                dto.setMiddlename(tmp.getTextContent().trim());
//                            } else if (tmp.getNodeName().equals("lastname")) {
//                                dto.setLastname(tmp.getTextContent().trim());
//                            } else if (tmp.getNodeName().equals("address")) {
//                                dto.setAddress(tmp.getTextContent().trim());
//                            } else if (tmp.getNodeName().equals("status")) {
//                                dto.setStatus(tmp.getTextContent().trim());
//                            }
//                        }
//                        list.add(dto);
//                    }
//                    request.setAttribute("INFO", list);
//                }
//            }
        } catch (Exception ex) {
            Logger.getLogger(EstySpider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Pipeline<String, String> getPipeLine() {
        Pipeline filters = new Pipeline<>(new RefineHTMLHandler());
        return filters;
    }

    class RefineHTMLHandler implements Handler<String, String> {

        @Override
        public String process(String input) {

            return TextUtils.refineHtml(input);
        }

    }
}

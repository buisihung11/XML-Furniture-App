
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
        String[] urls = {"https://www.onekingslane.com/c/furniture.do",};
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            GetHTML.getHTMLToFile("onekingslane-" + (i + 1) + ".html", url);
            String refinedSrc = GetHTML.getHTMLToString(url);
            System.out.println(url + ": \n :" + refinedSrc);
        }
//        System.out.println("BAT DAU PARSE");
//
//        String src = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//                + "\n"
//                + "<students>\n"
//                + "    <student id=\"SE01\" class=\"se1274\">\n"
//                + "	<img data-listing-card-listing-image=\"\" src=\"https://i.etsystatic.com/25510629/d/il/b3a5b9/2652782619/il_340x270.2652782619_om0y.jpg?version=0\" class=\"width-full wt-height-full display-block position-absolute \" alt=\"\" >\n"
//                + "        <lastname>\n"
//                + "            Dang\n"
//                + "        </lastname>\n"
//                + "        <middlename>\n"
//                + "            Quoc\n"
//                + "        </middlename>\n"
//                + "        <firstname>\n"
//                + "            Thai\n"
//                + "        <!--</firstname>-->\n"
//                + "        <sex>\n"
//                + "            1\n"
//                + "        </sex>\n"
//                + "        <password>\n"
//                + "            123456\n"
//                + "        </password>\n"
//                + "        <address>\n"
//                + "            tpHCM\n"
//                + "        </address>\n"
//                + "		<br />\n"
//                + "        <status>\n"
//                + "            studying\n"
//                + "        </status>\n"
//                + "    </student>\n"
//                + "    <student id= \"SE02\" class=\"se1274\" >\n"
//                + "        <lastname>\n"
//                + "            Nguyen\n"
//                + "        </lastname>\n"
//                + "        <middlename>\n"
//                + "            Hieu\n"
//                + "        </middlename>\n"
//                + "        <firstname>\n"
//                + "            Liem\n"
//                + "        </firstname>\n"
//                + "        <sex>\n"
//                + "            2\n"
//                + "        </sex>\n"
//                + "        <password>\n"
//                + "            1234956\n"
//                + "        </password>\n"
//                + "        <address>\n"
//                + "            tpHCM\n"
//                + "        </address>\n"
//                + "        <status>\n"
//                + "            dropout\n"
//                + "        </status>\n"
//                + "    </student>\n"
//                + "        <student id=\"SE03\" class=\"se1274\">\n"
//                + "        <lastname>\n"
//                + "            Nguyen\n"
//                + "        </lastname>\n"
//                + "        <middlename>\n"
//                + "            Hieu\n"
//                + "        </middlename>\n"
//                + "        <firstname>\n"
//                + "            Hoa\n"
//                + "        </firstname>\n"
//                + "        <sex>\n"
//                + "            3\n"
//                + "        </sex>\n"
//                + "        <password>\n"
//                + "            1230456\n"
//                + "        </password>\n"
//                + "        <address>\n"
//                + "            tpHCM\n"
//                + "        </address>\n"
//                + "        <status>\n"
//                + "            break\n"
//                + "        </status>\n"
//                + "    </student>\n"
//                + "</students>";
//
//        ParseXMLNotWellFormed parseXMl = new ParseXMLNotWellFormed();
//        parseXMl.setSrc(src);
//        parseXMl.autoParseFileEventNotWellForm();
//        System.out.println("PARSEDDDDD");
////        XMLUtils.printAllData(parseXMl.getlEvents().iterator());
//        System.out.println(parseXMl.getParsedSrc());

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

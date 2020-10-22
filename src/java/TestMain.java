
import utils.GetHTML;

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
        String[] urls = {"https://www.made.com/avalon-3-seater-sofa-steel-boucle",};
        for (int i = 0; i < urls.length; i++) {
            String url = urls[i];
            GetHTML.getHTMLToFile("output" + (i + 1) + ".html", url);
//            String refinedSrc = GetHTML.getHTMLToString(url);
//            System.out.println(url + ": \n :" + refinedSrc);
        }
    }

}

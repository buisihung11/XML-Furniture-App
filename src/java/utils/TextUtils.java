/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import xmlchecker.XmlSyntaxChecker;

/**
 *
 * @author DK
 */
public class TextUtils {

    public static String refineHtml(String src) {
        src = getBody(src);
        src = removeMiscellaneousTags(src);

//        XmlSyntaxChecker xmlSyntaxChecker = new XmlSyntaxChecker();
//        src=xmlSyntaxChecker.check(src);
//        
//        src= getBody(src);
//        System.out.println(src);
//        src = (new XmlSyntaxChecker()).check(src);
        return src;
    }

    public static String getBody(String src) {

        Matcher matcher = Pattern.compile("<head.*?</head>").matcher(src);
        String result = src, tmp = "";
        if (matcher.find()) {
            System.out.println("FOUNDED HEAD");
            tmp = matcher.group(0);
            result = result.replace(tmp, "");
        }
        // end remove head
        Matcher matcherSvg = Pattern.compile("<svg.*?</svg>").matcher(src);
        while (matcherSvg.find()) {
//            System.out.println("FOUNDED SVG");
            tmp = matcherSvg.group(0);
            result = result.replace(tmp, "");
        }
        // end remove svg
        String expression = "<body.*?</body>";
        matcher = Pattern.compile(expression).matcher(result);
        if (matcher.find()) {
            result = matcher.group(0);
        }
        return result;
    }

    public static String removeMiscellaneousTags(String src) {
        String result = src;

        //remove all <script> tags
        String expression = "<script.*?</script>";
        result = result.replaceAll(expression, "");

        //remove all commands
        expression = "<!--.*?-->";
        result = result.replaceAll(expression, "");

        //remove all whitespace
        expression = "&nbsp;?";
        result = result.replaceAll(expression, "");

        return result;
    }
}

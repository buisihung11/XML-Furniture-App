/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import xmlchecker.XmlSyntaxChecker;

/**
 *
 * @author DK
 */
public class TextUtils {

    public static final List<String> INLINE_TAGS = Arrays.asList(
            "area", "base", "br", "col", "command", "embed", "hr", "img", "input",
            "keygen", "link", "meta", "param", "source", "track", "wbr"
    );

    public static String refineHtml(String src) {
        src = getBody(src);
        src = removeMiscellaneousTags(src);
        return src;
    }

    public static String getBody(String src) {

        Matcher matcher = Pattern.compile("(?s)<head.*?</head>(?s)").matcher(src);
        String result = src, tmp = "";
        if (matcher.find()) {
            System.out.println("FOUNDED HEAD");
            tmp = matcher.group(0);
            result = result.replace(tmp, "");
        }
        // end remove head
        Matcher matcherSvg = Pattern.compile("(?s)<svg.*?</svg>(?s)").matcher(src);
        while (matcherSvg.find()) {
//            System.out.println("FOUNDED SVG");
            tmp = matcherSvg.group(0);
            result = result.replace(tmp, "");
        }
        // end remove svg
        String expression = "(?s)<body.*?</body>(?s)";
        matcher = Pattern.compile(expression).matcher(result);
        if (matcher.find()) {
            result = matcher.group(0);
        }
        return result;
    }

    public static String removeMiscellaneousTags(String src) {
        String result = src;

        //remove all <script> tags
        String expression = "(?s)<script.*?</script>(?s)";
        result = result.replaceAll(expression, "");

        //remove all <noscript> tags
        String noScript = "(?s)<noscript.*?</noscript>(?s)";
        result = result.replaceAll(noScript, "");

        //remove all commands
        expression = "(?s)<!--.*?-->(?s)";
        result = result.replaceAll(expression, "");

        //remove all whitespace
        expression = "(?s)&nbsp;?(?s)";
        result = result.replaceAll(expression, "");

        return result;
    }

    public static String removeSelfClosing(String src) {
        for (String inlineTag : INLINE_TAGS) {
            String reg = "<\\s*(" + inlineTag + ")([^>]*)\\/?\\s*>";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(src);
            src = m.replaceAll("");
//            while (m.find()) {
//                String group1 = m.group(2);
//                System.out.println("Group 1 " + group1);
//                if (group1.trim().equals("/")) {
//                    src = m.replaceFirst("<$1></$1>");
//                } else {
//                    src = m.replaceFirst("<$1$2></$1>");
//                }
//            }
        }
        return src;
    }

    public static String replaceSelfClosing(String src) {
        for (String inlineTag : INLINE_TAGS) {
            String reg = "<\\s*(" + inlineTag + ")([^>]*)\\/?\\s*>";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(src);
            src = m.replaceAll("<$1$2></$1>");
//            while (m.find()) {
//                String group1 = m.group(2);
//                System.out.println("Group 1 " + group1);
//                if (group1.trim().equals("/")) {
//                    src = m.replaceFirst("<$1></$1>");
//                } else {
//                    src = m.replaceFirst("<$1$2></$1>");
//                }
//            }
        }
        return src;
    }
}

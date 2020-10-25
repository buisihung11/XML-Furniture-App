/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Admin
 */
public class GetHTML {

    public static void getHTMLToFile(String filePath, String uri) {
        try {
            String src = Internet.getHTML(uri);
            FileUtil.saveSrcToFile(filePath, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getHTMLToString(String uri) {
        Writer writer = null;
        try {
            String src = Internet.getHTML(uri);
            src = TextUtils.refineHtml(src);
            return src;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

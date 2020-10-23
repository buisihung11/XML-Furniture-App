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
        Writer writer = null;
        try {
            String src = Internet.getHTML(uri);
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), "UTF-8"));
            src = TextUtils.refineHtml(src);
            writer.write(src);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

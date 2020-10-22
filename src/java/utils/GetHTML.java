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
            String src = getHTMLToString(uri);
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), "UTF-8"));
            writer.write(src);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getHTMLToString(String uri) {
        try {
            URL url = new URL(uri);
            URLConnection yc = url.openConnection();
            yc.setReadTimeout(20 * 1000);
            yc.setConnectTimeout(20 * 1000);
            yc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36");

            InputStream is = yc.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((line = bufferReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String src = TextUtils.refineHtml(stringBuilder.toString());
            return src;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

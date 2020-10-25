/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class Internet {

    public static String getHTML(String uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(uri);
            URLConnection yc = url.openConnection();
            yc.setReadTimeout(20 * 1000);
            yc.setConnectTimeout(20 * 1000);
            yc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36");

            InputStream is = yc.getInputStream();

            String line;
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((line = bufferReader.readLine()) != null) {
                if (!line.trim().equals("")) {
                    stringBuilder.append(line).append("\n");
                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Internet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringBuilder.toString();
    }
}

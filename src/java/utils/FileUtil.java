/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author Admin
 */
public class FileUtil {

    public static void saveSrcToFile(String filePath, String src) {
        Writer writer = null;
        try {
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

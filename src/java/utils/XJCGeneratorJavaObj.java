/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.ErrorListener;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;
import java.io.File;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Admin
 */
public class XJCGeneratorJavaObj {

    public static String JAVA_CLASS_OUTPUT = "src/java/";
    public static String PACKAGE_NAME = "com.dto";

    public static void convertToJavaClass(String fileInputPath) {
        try {
            // tu xsd build thanh Java obj
            String output = JAVA_CLASS_OUTPUT;
            SchemaCompiler sc = XJC.createSchemaCompiler();
            sc.setErrorListener(new ErrorListener() {
                @Override
                public void error(SAXParseException saxpe) {
                    System.out.println("ERROR" + saxpe.getMessage());
                }

                @Override
                public void fatalError(SAXParseException saxpe) {
                    System.out.println("ERROR" + saxpe.getMessage());
                }

                @Override
                public void warning(SAXParseException saxpe) {
                    System.out.println("ERROR" + saxpe.getMessage());
                }

                @Override
                public void info(SAXParseException saxpe) {
                    System.out.println("ERROR" + saxpe.getMessage());
                }
            });
            sc.forcePackageName(PACKAGE_NAME);
//            File schema = new File("customer.xsd");
            File schema = new File(fileInputPath);
            InputSource is = new InputSource(schema.toURI().toString());
            sc.parseSchema(is);
            // schema to java Obj
            S2JJAXBModel model = sc.bind();
            JCodeModel code = model.generateCode(null, null);
            code.build(new File(output));
            System.out.println("Finished");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spider;

import java.io.IOException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import parsers.STAXParser;
import utils.Internet;
import utils.Pipeline;

/**
 *
 * @author Admin
 */
public abstract class Spider<T> {

    public String name;
    public String startURL;
    private T item;

    public Spider(String name, String startURL) {
        this.name = name;
        this.startURL = startURL;
    }

    public void startExecution() throws IOException, Exception {
        System.out.println("START SPIDER " + name);
        // get raw HTML
        String rawHTML = Internet.getHTML(startURL);
        // excution pipeline
        String finalHTML = rawHTML;
        Pipeline<String, String> transformSource = getPipeLine();
        if (transformSource != null) {
            finalHTML = transformSource.execute(rawHTML); // da well-form
        }
        System.out.println("FINAL HTML: \n" + finalHTML + " \n");
        // process final src to Items
//        XMLEventReader eventReader = STAXParser.createSTAXCursorReaderFromString(finalHTML);
//        item = parse(eventReader);
        parse(finalHTML);
    }

    public T getItem() {
        return item;
    }

    public abstract Pipeline<String, String> getPipeLine();

    public abstract void parse(String finalText);
}

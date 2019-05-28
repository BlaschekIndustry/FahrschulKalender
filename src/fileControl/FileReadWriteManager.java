package fileControl;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import java.io.File;

public abstract class FileReadWriteManager {
    String fileName;
    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document document;

    public FileReadWriteManager(String fileName)  {
        this.fileName = fileName;

        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void read();
    public abstract void write() throws TransformerException;
}

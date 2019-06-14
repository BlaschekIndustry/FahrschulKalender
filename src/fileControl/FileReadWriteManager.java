package fileControl;

import general.ErrorDialogs;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

public abstract class FileReadWriteManager {
    String fileName;
    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document document;

    public FileReadWriteManager(String fileName)  {
        this.fileName = fileName;
        File inputFile = new File(fileName);
        if(!inputFile.exists()) {
            ErrorDialogs.showWarningMessage("Die Datei: \"" + fileName + "\" existiert nicht!\nSie wird automatisch erzeugt.");
            try {
                File xmlPreset = new File("C:\\Users\\blaschek\\IdeaProjects\\FahrschulKalendar\\src\\fileControl\\XmlVorlage.xml");//Todo Pfad anpassen
                if(xmlPreset.exists())
                    Files.copy(xmlPreset.toPath(), inputFile.toPath());
                inputFile.setWritable(true);
            } catch (IOException e) {
                ErrorDialogs.showErrorMessage("Fehler beim erzeugen der Datei: \"" + fileName + "\"!");
                e.printStackTrace();
            }
        }
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

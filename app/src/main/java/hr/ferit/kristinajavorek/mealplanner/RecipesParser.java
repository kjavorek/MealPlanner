package hr.ferit.kristinajavorek.mealplanner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
//import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;

public class RecipesParser {

    public String[] xmlParsing(String fetchurl,String roottag,String parseelemnt)
    {
        String[] temp=null;
        URL url=null;
        try {
            url = new URL(fetchurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db=null;
        try {
            db = dbf.newDocumentBuilder();
        }
        catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        Document doc=null;
        try {
            doc = db.parse(new InputSource(url.openStream()));
        } catch (SAXException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        org.w3c.dom.Element elt=doc.getDocumentElement();
        NodeList nodeList = elt.getElementsByTagName(roottag);
        temp=new String[nodeList.getLength()];

        for (int i = 0; i < nodeList.getLength(); i++)
        {
            Node node = nodeList.item(i);
            NodeList titleList = node.getChildNodes();

            for (int j = 0; j < titleList.getLength(); j++) {
                Element e = (Element) titleList.item(j);
                Node node1 = titleList.item(j);
                String name = node1.getNodeName();
                if (name.equalsIgnoreCase(parseelemnt)) {
                    temp[i] = node1.getFirstChild().getNodeValue();
                }
            }
        }
        return(temp);
    }
}

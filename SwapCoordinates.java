
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SwapCoordinates {

    /**
     * @param args
     * @throws TransformerException
     * @throws ParserConfigurationException
     * @throws IOException
     */

    public static int count = 0;

    public static void main(String argv[]) throws TransformerException, ParserConfigurationException, IOException {

        String root = "Grids";
        String subroot = "Grid";
        String element1 = "Name";
        String element2 = "KMLCode";
        String element3 = "KMLLocation";
        String element4 = "Colour";

        String data = "prabath";
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        Element rootElement = document.createElement(root);

        document.appendChild(rootElement);

        try {
            // creating a constructor of file class and parsing an XML file
            File file = new File("C:/Users/prabath/Desktop/pizz_hut_grid_kml_source.xml");
            // an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Grid");

            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                // for (int itr = 0; itr < 1; itr++) {
                List<String> KMLlocationList = new ArrayList<>();
                count++;
                System.out.println(count);
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    // System.out.println("Name: " +
                    // eElement.getElementsByTagName("Name").item(0).getTextContent());
                    // System.out.println(
                    // "Kml code: " +
                    // eElement.getElementsByTagName("KMLCode").item(0).getTextContent());
                    // System.out.println(
                    // "KML loc: " +
                    // eElement.getElementsByTagName("KMLLocation").item(0).getTextContent());
                    // System.out.println("color: " +
                    // eElement.getElementsByTagName("Colour").item(0).getTextContent());

                    String KmlLocationtring = eElement.getElementsByTagName("KMLLocation").item(0).getTextContent();
                    KmlLocationtring = KmlLocationtring.split("\\(")[2];
                    KmlLocationtring = KmlLocationtring.split("\\)")[0];

                    String[] coordinates = KmlLocationtring.split(",");
                    for (String i : coordinates) {
                        String x = i.trim().split(" ")[0];
                        String y = i.trim().split(" ")[1];
                        KMLlocationList.add(y + " " + x);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < KMLlocationList.size(); i++) {
                        sb.append(KMLlocationList.get(i));
                        if (i != KMLlocationList.size() - 1) {
                            sb.append(", ");
                        }

                    }
                    // convert in string
                    String string = sb.toString();
                    string = "POLYGON(( " + string + " ))";

                    // create xml element
                    Element sub = document.createElement(subroot);

                    Element em1 = document.createElement(element1);
                    em1.appendChild(
                            document.createTextNode(eElement.getElementsByTagName("Name").item(0).getTextContent()));

                    Element em2 = document.createElement(element2);
                    em2.appendChild(
                            document.createTextNode(eElement.getElementsByTagName("KMLCode").item(0).getTextContent()));

                    Element em3 = document.createElement(element3);
                    em3.appendChild(document.createTextNode(string));

                    Element em4 = document.createElement(element4);
                    em4.appendChild(
                            document.createTextNode(eElement.getElementsByTagName("Colour").item(0).getTextContent()));

                    sub.appendChild(em1);
                    sub.appendChild(em2);
                    sub.appendChild(em3);
                    sub.appendChild(em4);
                    rootElement.appendChild(sub);

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(document);

                    StreamResult result = new StreamResult(new StringWriter());

                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
                    transformer.transform(source, result);

                    // writing to file
                    FileOutputStream fop = null;
                    File file2;
                    try {

                        file2 = new File("newfile.xml");
                        fop = new FileOutputStream(file2);

                        // if file doesnt exists, then create it
                        if (!file2.exists()) {
                            file.createNewFile();
                        }

                        // get the content in bytes
                        String xmlString = result.getWriter().toString();
                        byte[] contentInBytes = xmlString.getBytes();

                        fop.write(contentInBytes);
                        fop.flush();
                        fop.close();

                        System.out.println("Done");

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fop != null) {
                                fop.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
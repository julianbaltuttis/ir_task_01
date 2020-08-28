package org;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Log4j
@Getter

public class Topics {
    private List<Topic> topics;


    public Topics(String filePath) {
            try {
                this.topics = parseXMLtoTopics(filePath);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                log.error(e.getMessage());
            }
    }

    public List<Topic> parseXMLtoTopics(String filePath) throws ParserConfigurationException, IOException, SAXException {

        List<Topic> topics = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        File file = new File(filePath);
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("topic");
        for(int i = 0; i < nList.getLength(); i++){

            Node node = nList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                String number = element.getElementsByTagName("number").item(0).getTextContent();
                String title = element.getElementsByTagName("title").item(0).getTextContent();

                Topic topic = new Topic(Integer.parseInt(number), title);
                topics.add(topic);
            }
        }
        return topics;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(Topic topic : topics) {
            stringBuilder.append(topic.toString());
        }
        return stringBuilder.toString();
    }
}

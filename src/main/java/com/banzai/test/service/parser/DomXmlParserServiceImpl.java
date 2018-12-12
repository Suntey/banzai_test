package com.banzai.test.service.parser;

import com.banzai.test.dto.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.Optional;

/**
 * Created by Kuznetsov A.S. 04.12.2018
 */
@Service
@Slf4j
public class DomXmlParserServiceImpl implements DomXmlParserService {

    @Override
    public Optional<Entry> parseXmlFile(final String xmlFileName, final byte[] bytes) {
        try {
            log.info("Method DomXmlParserServiceImpl.parseXmlFile(File xmlEntryFile) executing!\n" +
                     "Processing file {}...", xmlFileName);

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();

            final Document document = builder.parse(new ByteArrayInputStream(bytes));
            final Element startNode = document.getDocumentElement();
            startNode.normalize();

            final Optional<Entry> parsedEntry = proccessNode(startNode);

            parsedEntry.ifPresent(entry -> entry.setFileName(xmlFileName));

            return parsedEntry;
        } catch (Exception e) {
            log.error("Error in method DomXmlParserServiceImpl.parseXmlFile(final File xmlEntryFile)", e);
            return Optional.empty();
        }
    }

    private Optional<Entry> proccessNode(final Node start) {
        final Entry entry = new Entry();
        for (Node child = start.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                final String nodeName = child.getNodeName();

                switch(nodeName) {
                    case "content" :
                        entry.setContent(child.getTextContent());
                        break;
                    case "creationDate":
                        entry.setCreationDate(child.getTextContent());
                        break;
                    default: log.error("File have not correct xml tag names!");
                }
            }
        }

        return entry.isEmpty()? Optional.empty() : Optional.of(entry);
    }
}

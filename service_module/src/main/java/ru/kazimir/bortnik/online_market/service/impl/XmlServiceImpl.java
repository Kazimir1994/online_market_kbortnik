package ru.kazimir.bortnik.online_market.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import ru.kazimir.bortnik.online_market.service.Parsing;
import ru.kazimir.bortnik.online_market.service.XmlService;
import ru.kazimir.bortnik.online_market.service.exception.StAXParserServiceException;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlServiceImpl implements XmlService {
    private static final String SCHEMA = "classpath:itemschema.xsd";
    private final Parsing stAXParser;

    public XmlServiceImpl(Parsing stAXParser) {
        this.stAXParser = stAXParser;
    }

    private boolean validateXMLSchema(File FileXsd, InputStream FileXml) {
        try {
            SchemaFactory schemafactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemafactory.newSchema(FileXsd);
            Validator validator = schema.newValidator();
            Source xmlFile = new StreamSource(FileXml);
            validator.validate(xmlFile);
        } catch (IOException | SAXException e) {
            throw new StAXParserServiceException();
        }
        return true;
    }

    @Override
    public List<ItemDTO> parse(MultipartFile file) {
        List<ItemDTO> itemList = new ArrayList<>();
        try {
            File FileXsd = ResourceUtils.getFile(SCHEMA);
            if (validateXMLSchema(FileXsd, file.getInputStream())) {
                itemList = stAXParser.parse(file.getInputStream());
            }
        } catch (IOException e) {
            throw new StAXParserServiceException();
        }
        return itemList;
    }
}

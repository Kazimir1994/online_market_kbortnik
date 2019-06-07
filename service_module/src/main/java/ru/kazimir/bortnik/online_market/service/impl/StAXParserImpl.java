package ru.kazimir.bortnik.online_market.service.impl;

import org.springframework.stereotype.Service;
import ru.kazimir.bortnik.online_market.service.Parsing;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class StAXParserImpl implements Parsing {

    @Override
    public List<ItemDTO> parse(InputStream fileXML) {
        boolean keyName = false;
        boolean keyDescription = false;
        boolean keyPrice = false;
        ItemDTO item = null;
        List<ItemDTO> itemList = new ArrayList<>();
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(fileXML);
            while (eventReader.hasNext()) {
                XMLEvent xmlEvent = eventReader.nextEvent();
                switch (xmlEvent.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT: {
                        StartElement startElement = xmlEvent.asStartElement();
                        switch (startElement.getName().getLocalPart()) {
                            case "Item": {
                                item = new ItemDTO();
                                break;
                            }
                            case "name": {
                                keyName = true;
                                break;
                            }
                            case "description": {
                                keyDescription = true;
                                break;
                            }
                            case "price": {
                                keyPrice = true;
                                break;
                            }
                        }
                        break;
                    }
                    case XMLStreamConstants.CHARACTERS: {
                        Characters characters = xmlEvent.asCharacters();
                        if (keyName) {
                            assert item != null;
                            item.setName(characters.getData());
                            keyName = false;
                        }
                        if (keyDescription) {
                            assert item != null;
                            item.setShortDescription(characters.getData());
                            keyDescription = false;
                        }
                        if (keyPrice) {
                            assert item != null;
                            BigDecimal price = BigDecimal.valueOf(Double.valueOf(characters.getData()));
                            item.setPrice(price.toString());
                            keyPrice = false;
                        }
                        break;
                    }
                    case XMLStreamConstants.END_ELEMENT: {
                        EndElement endElement = xmlEvent.asEndElement();
                        if (endElement.getName().getLocalPart().equals("Item")) {
                            itemList.add(item);
                        }
                        break;
                    }
                }
            }
        } catch (XMLStreamException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return itemList;
    }
}

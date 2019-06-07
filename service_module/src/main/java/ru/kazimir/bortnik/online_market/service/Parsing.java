package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.ItemDTO;

import java.io.InputStream;
import java.util.List;

public interface Parsing {

    List<ItemDTO> parse(InputStream fileXML);
}

package ru.kazimir.bortnik.online_market.service;

import org.springframework.web.multipart.MultipartFile;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;

import java.util.List;

public interface XmlService {

    List<ItemDTO> parse(MultipartFile file);
}

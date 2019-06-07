package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazimir.bortnik.online_market.repository.ItemRepository;
import ru.kazimir.bortnik.online_market.repository.model.Item;
import ru.kazimir.bortnik.online_market.service.ItemService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.exception.ItemServiceException;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_ITEMS;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_GET_ITEM;

@Service
public class ItemServiceImpl implements ItemService {
    private final static Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemRepository itemRepository;
    private final Converter<ItemDTO, Item> itemConverter;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository,
                           @Qualifier("itemConverterImpl") Converter<ItemDTO, Item> itemConverter) {
        this.itemRepository = itemRepository;
        this.itemConverter = itemConverter;
    }

    @Transactional
    @Override
    public PageDTO<ItemDTO> getItems(Long limitPositions, Long currentPage) {
        PageDTO<ItemDTO> pageDTO = new PageDTO<>();
        pageDTO.setCurrentPage(currentPage);
        Long countOfUsers = itemRepository.getCountOfNotDeletedEntities();
        Long countOfPages = calculationCountOfPages(countOfUsers, limitPositions);
        pageDTO.setCountOfPages(countOfPages);

        if (currentPage > countOfPages) {
            currentPage = countOfPages;
        } else if (currentPage < countOfPages) {
            currentPage = 1L;
        }
        Long offset = getPosition(limitPositions, currentPage);
        logger.info(MESSAGES_GET_ITEMS, limitPositions, offset);
        List<Item> userList = itemRepository.findAll(offset, limitPositions);
        List<ItemDTO> userDTOList = userList.stream().map(itemConverter::toDTO).collect(Collectors.toList());
        pageDTO.setList(userDTOList);
        return pageDTO;
    }

    @Transactional
    @Override
    public List<ItemDTO> getListItems(Long offset, Long limit) {
        List<Item> article = itemRepository.findAll(offset, limit);
        logger.info(MESSAGES_GET_ITEMS, article);
        return article.stream().map(itemConverter::toDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ItemDTO getById(Long id) {
        Item item = itemRepository.findByIdNotDeleted(id);
        if (item != null) {
            return itemConverter.toDTO(item);
        } else {
            throw new ItemServiceException(String.format(USER_ERROR_GET_ITEM, id));
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Item item = itemRepository.findByIdNotDeleted(id);
        if (item != null) {
            itemRepository.remove(item);
        } else {
            throw new ItemServiceException(String.format(USER_ERROR_GET_ITEM, id));
        }
    }

    @Transactional
    @Override
    public void add(ItemDTO itemDTO) {
        itemDTO.setUniqueNumber(UUID.randomUUID().toString());
        Item item = itemConverter.fromDTO(itemDTO);
        itemRepository.persist(item);
    }

    @Transactional
    @Override
    public void add(List<ItemDTO> items) {
        items.forEach(itemDTO -> {
            itemDTO.setUniqueNumber(UUID.randomUUID().toString());
            Item item = itemConverter.fromDTO(itemDTO);
            itemRepository.persist(item);
        });
    }

    private Long getPosition(Long limitPositions, Long positions) {
        if (positions == 0) {
            positions++;
        }
        return limitPositions * (positions - 1);
    }

    private Long calculationCountOfPages(Long countOfArticle, Long limitPositions) {
        Long countOfPages;
        if (countOfArticle % limitPositions > 0) {
            countOfPages = (countOfArticle / limitPositions) + 1;
        } else {
            countOfPages = countOfArticle / limitPositions;
        }
        return countOfPages;
    }
}

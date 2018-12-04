package com.banzai.test.service;

import com.banzai.test.dto.Entry;
import com.banzai.test.service.parser.DomXmlParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilesConsumerServiceImpl implements Runnable {

    private final DomXmlParserService domXmlParserService;

    @Autowired
    public FilesConsumerServiceImpl(final DomXmlParserService domXmlParserService) {
        this.domXmlParserService = domXmlParserService;
    }

    private Collection<Entry> processFiles(final Map<String, byte[]> byteArrayCollection){
        try {
            return byteArrayCollection.entrySet().parallelStream()
                    .map(pair ->  domXmlParserService.parseXmlFile(pair.getKey(), pair.getValue())
                                                     .orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error in method FileServiceImpl.processFiles!", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void run() {

    }
}

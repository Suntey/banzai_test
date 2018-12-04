package com.banzai.test.service;

import com.banzai.test.dto.Entry;
import com.banzai.test.service.parser.DomXmlParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    private final DomXmlParserService domXmlParserService;

    @Autowired
    public FileServiceImpl(final DomXmlParserService domXmlParserService) {
        this.domXmlParserService = domXmlParserService;
    }

    @Override
    public Collection<Entry> getParsedObjectsFromFolder(String path) {

        return null;
    }

    private Collection<Entry> processFiles(Collection<File> files){
        try {
            ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            final Collection<Future<Optional<Entry>>> futureCollection = files.stream()
                    .map(file -> exec.submit(() -> domXmlParserService.parseXmlFile(file)))
                    .collect(Collectors.toList());


            final Collection<Entry> parsedEntries = futureCollection.stream()
                    .map(future -> getEntryFromFuture(future).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            exec.shutdown();

            return parsedEntries;
        } catch (Exception e) {
            log.error("Error in method FileServiceImpl.processFiles!", e);
            return Collections.emptyList();
        }
    }

    private Optional<Entry> getEntryFromFuture(final Future<Optional<Entry>> entryFuture) {
        try {
            return entryFuture.get(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Error in method FileServiceImpl.getEntryFromFuture!", e);
            return Optional.empty();
        }
    }


}

package com.banzai.test.service.consumer;

import com.banzai.test.dto.Entry;
import com.banzai.test.dto.Tuple2;
import com.banzai.test.service.entry.EntryService;
import com.banzai.test.service.folders.LocalNetworkFoldersService;
import com.banzai.test.service.parser.DomXmlParserService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class FilesConsumerServiceImpl implements Runnable {

    private final DomXmlParserService domXmlParserService;

    private final BlockingQueue<Tuple2<String, byte[]>> blockingQueue;

    private final int batchSize;

    private final EntryService entryService;

    private final AtomicInteger counter;

    private final LocalNetworkFoldersService foldersService;


    public FilesConsumerServiceImpl(final DomXmlParserService domXmlParserService, final AtomicInteger counter,
                                    final BlockingQueue<Tuple2<String, byte[]>> blockingQueue, final int batchSize,
                                    final EntryService entryService,
                                    final LocalNetworkFoldersService foldersService) {
        this.domXmlParserService = domXmlParserService;
        this.counter = counter;
        this.blockingQueue = blockingQueue;
        this.batchSize = batchSize;
        this.entryService = entryService;
        this.foldersService = foldersService;
    }

    private Optional<Entry> parseFile(final Tuple2<String, byte[]> fileToParse){
        try {
            return domXmlParserService.parseXmlFile(fileToParse.getValue1(), fileToParse.getValue2());
        } catch (Exception e) {
            log.error("Error in method FileServiceImpl.parseFile!", e);
            foldersService.moveFiles(Collections.singleton(fileToParse.getValue1()), false);
            return Optional.empty();
        }
    }

    @Override
    public void run() {
        final Collection<Entry> collectionToSave = new ArrayList<>();
        while (counter.get() != 0) {
            final Optional<Tuple2<String, byte[]>> blockingQueueTuple = takeFromBlockingQueue();
            final Optional<Entry> optionalEntry = blockingQueueTuple.flatMap(this::parseFile);

            optionalEntry.ifPresent(collectionToSave::add);

            //отправить на сохранение если size = batchSize
            if (collectionToSave.size() == batchSize ) {
                batchLoadAndMoveFiles(collectionToSave);
                collectionToSave.clear();
            }
        }

        if (!collectionToSave.isEmpty()) {
            batchLoadAndMoveFiles(collectionToSave);
        }
    }

    private Optional<Tuple2<String, byte[]>> takeFromBlockingQueue() {
        try {
            return Optional.of(blockingQueue.take());
        } catch (InterruptedException e) {
            log.error("Error in method FilesConsumerServiceImpl.takeFromBlockingQueue!", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        } finally {
            counter.decrementAndGet();
        }
    }

    private void batchLoadAndMoveFiles(final Collection<Entry> entries) {
        final boolean isSaved = batchLoadEntries(entries);

        final List<String> fileNames = entries.stream()
                .map(Entry::getFileName)
                .collect(Collectors.toList());

        foldersService.moveFiles(fileNames, isSaved);
    }

    private boolean batchLoadEntries(final Collection<Entry> entries) {
        try {
            entryService.batchSaveEntries(entries);
            return true;
        } catch (Exception e) {
            log.error("Error while batch saving. Rollback transaction!", e);
            return false;
        }
    }
}

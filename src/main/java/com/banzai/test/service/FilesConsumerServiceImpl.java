package com.banzai.test.service;

import com.banzai.test.dto.Entry;
import com.banzai.test.dto.Tuple2;
import com.banzai.test.service.parser.DomXmlParserService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class FilesConsumerServiceImpl implements Runnable {

    private final DomXmlParserService domXmlParserService;

    private final BlockingQueue<Tuple2<String, byte[]>> blockingQueue;

    private final int batchSize;

    private final EntryService entryService;

    private final AtomicInteger counter;


    public FilesConsumerServiceImpl(final DomXmlParserService domXmlParserService, final AtomicInteger counter,
                                    final BlockingQueue<Tuple2<String, byte[]>> blockingQueue, final int batchSize,
                                    final EntryService entryService) {
        this.domXmlParserService = domXmlParserService;
        this.counter = counter;
        this.blockingQueue = blockingQueue;
        this.batchSize = batchSize;
        this.entryService = entryService;
//        this.cyclicBarrier = cyclicBarrier;
    }

    private Optional<Entry> parseFile(final Tuple2<String, byte[]> fileToParse){
        try {
            return domXmlParserService.parseXmlFile(fileToParse.getValue1(), fileToParse.getValue2());
        } catch (Exception e) {
            log.error("Error in method FileServiceImpl.processFiles!", e);
            return Optional.empty();
        }
    }

    @Override
    public void run() {
        final Collection<Entry> collectionToSave = new ArrayList<>();
        while (counter.get() != 0) {
            try {
                final Tuple2<String, byte[]> fileNameAndByteArrayMap = blockingQueue.take();
                final Optional<Entry> optionalEntry = parseFile(fileNameAndByteArrayMap);
                optionalEntry.ifPresent(collectionToSave::add);

                //отправить на сохранение если size = batchSize
                if (collectionToSave.size() == batchSize ) {
                    final boolean isSaved = batchLoadEntries(collectionToSave);
                    collectionToSave.clear();
                }
            } catch (InterruptedException e) {
                log.error("FilesConsumerServiceImpl error in method run!", e);
                //TODO дописать обработку
            } finally {
                counter.decrementAndGet();
            }
        }

        if (!collectionToSave.isEmpty()) {
            final boolean isSaved = batchLoadEntries(collectionToSave);
            System.out.println(isSaved);
        }
    }


    private void batchLoadAndMoveFiles() {
        //TODO
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

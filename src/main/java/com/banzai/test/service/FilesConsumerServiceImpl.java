package com.banzai.test.service;

import com.banzai.test.dto.Entry;
import com.banzai.test.dto.Tuple2;
import com.banzai.test.service.parser.DomXmlParserService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class FilesConsumerServiceImpl implements Runnable {

    private final DomXmlParserService domXmlParserService;

    private final CountDownLatch countDownLatch;

    private final BlockingQueue<Tuple2<String, byte[]>> blockingQueue;

    private final int batchSize;


    public FilesConsumerServiceImpl(final DomXmlParserService domXmlParserService, final CountDownLatch countDownLatch,
                                    final BlockingQueue<Tuple2<String, byte[]>> blockingQueue, final int batchSize) {
        this.domXmlParserService = domXmlParserService;
        this.countDownLatch = countDownLatch;
        this.blockingQueue = blockingQueue;
        this.batchSize = batchSize;
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
        while (countDownLatch.getCount() != 0) {
            try {
                final Tuple2<String, byte[]> fileNameAndByteArrayMap = blockingQueue.take();
                final Optional<Entry> optionalEntry = parseFile(fileNameAndByteArrayMap);
                optionalEntry.ifPresent(collectionToSave::add);

                //отправить на сохранение если size = batchSize
                if (collectionToSave.size() == batchSize) {
                    batchLoadEntries(collectionToSave);
                    collectionToSave.clear();
                }
            } catch (InterruptedException e) {
                log.error("FilesConsumerServiceImpl error in method run!", e);
                //TODO дописать обработку
            } finally {
                countDownLatch.countDown();
            }
        }

        if (!collectionToSave.isEmpty()) {
            batchLoadEntries(collectionToSave);
        }
    }

    private void batchLoadEntries(final Collection<Entry> entries) {
        //вызов репозитория
        //перенос файлов
    }
}

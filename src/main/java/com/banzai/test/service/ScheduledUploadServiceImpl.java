package com.banzai.test.service;

import com.banzai.test.dto.Tuple2;
import com.banzai.test.service.parser.DomXmlParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Created by Kuznetsov A.S. 05.12.2018
 */
@Service
@Slf4j
public class ScheduledUploadServiceImpl {

    private final int batchSize;

    private final int queueSize;

    private final Path sourceDirectory;

    private final DomXmlParserService domXmlParserService;

    private final EntryService entryService;

    private static final int THREAD_NUMBER = 10;

    @Autowired
    public ScheduledUploadServiceImpl(@Value("${batchSize}") final int batchSize,
                                      @Value("${queueSize}") final int queueSize,
                                      @Value("${directory.sourceFiles}") final String sourceDirectory,
                                      final DomXmlParserService domXmlParserService,
                                      final EntryService entryService) {
        this.batchSize = batchSize;
        this.queueSize = queueSize;
        this.sourceDirectory = Paths.get(sourceDirectory);
        this.domXmlParserService = domXmlParserService;
        this.entryService = entryService;
    }

    @Scheduled(cron = "${cron.schedule}")
    public void uploadFiles() throws InterruptedException {
        final BlockingQueue<Tuple2<String, byte[]>> blockingQueue = new LinkedBlockingQueue<>(queueSize);

        final FilesProducerServiceImpl filesProducerService = new FilesProducerServiceImpl(blockingQueue, sourceDirectory);

        CompletableFuture.runAsync(filesProducerService);
        final AtomicInteger counter = new AtomicInteger(getFilesCount());

        final CountDownLatch countDownLatch = new CountDownLatch(getFilesCount());
        final ExecutorService exec = Executors.newFixedThreadPool(THREAD_NUMBER);
        final FilesConsumerServiceImpl filesConsumerService = new FilesConsumerServiceImpl(domXmlParserService, counter, blockingQueue, batchSize, entryService);

        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(filesConsumerService, exec);
        CompletableFuture.allOf(voidCompletableFuture).join();

        exec.shutdown();
    }

    private int getFilesCount() {
        try (Stream<Path> fileStream = Files.walk(sourceDirectory)) {
            final long count = fileStream
                    .filter(file -> !file.toFile().isDirectory())
                    .count();
            assert (count < Integer.MAX_VALUE);
            return (int) count;
        } catch (IOException e) {
            log.error("Error in method FilesProducerServiceImpl.putFilesIntoQueue()", e);
            return 0;
        }
    }
}

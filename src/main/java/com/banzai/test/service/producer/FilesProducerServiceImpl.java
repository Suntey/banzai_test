package com.banzai.test.service.producer;

import com.banzai.test.dto.Tuple2;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

@Slf4j
public class FilesProducerServiceImpl implements Runnable {

    private final BlockingQueue<Tuple2<String, byte[]>> blockingQueue;

    private final Path directoryFrom;

    public FilesProducerServiceImpl(final BlockingQueue<Tuple2<String, byte[]>> blockingQueue, final Path directoryFrom) {
        this.blockingQueue = blockingQueue;
        this.directoryFrom = directoryFrom;
    }

    @Override
    public void run() {
        if (!directoryFrom.toFile().isDirectory()) {
            log.error("Invalid path to directory with files!");
            return;
        }
        putFilesIntoQueue();
    }

    private void putFilesIntoQueue() {
        try (Stream<Path> fileStream = Files.walk(directoryFrom)) {
            fileStream.filter(file -> !file.toFile().isDirectory()).forEach(this::putBytesIntoQueue);
        } catch (IOException e) {
            log.error("Error in method FilesProducerServiceImpl.putFilesIntoQueue()", e);
        }
    }

    private void putBytesIntoQueue(final Path file) {
        try {
            blockingQueue.put(new Tuple2<>(file.getFileName().toString(), Files.readAllBytes(file)));
        } catch (Exception e) {
            log.error("Error while puting files into queue!", e);
        }
    }
}

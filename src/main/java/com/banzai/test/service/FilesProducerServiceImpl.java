package com.banzai.test.service;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class FilesProducerServiceImpl implements FilesProducerService {

    private final BlockingQueue<byte[]> blockingQueue;

    private final File directoryFrom;

    public FilesProducerServiceImpl(final BlockingQueue<byte[]> blockingQueue, final File directoryFrom) {
        this.blockingQueue = blockingQueue;
        this.directoryFrom = directoryFrom;
    }

    public void getEntryFiles() {
        try {
            if (!directoryFrom.isDirectory()) {
                log.error("Invalid path to directory with files!");
                return;
            }

            final File[] files = directoryFrom.listFiles();
            assert files != null;

            for (File file : files) {
                blockingQueue.put(Files.readAllBytes(file.toPath()));
            }
        } catch (Exception e) {
            log.error("Error while reading files!", e);
        }

    }
}

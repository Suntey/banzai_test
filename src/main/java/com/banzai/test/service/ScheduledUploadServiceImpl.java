package com.banzai.test.service;

import com.banzai.test.dto.Tuple2;
import com.banzai.test.service.parser.DomXmlParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Kuznetsov A.S. 05.12.2018
 */
@Service
@Slf4j
public class ScheduledUploadServiceImpl {

    private final int batchSize;

    private final DomXmlParserService domXmlParserService;

    private final EntryService entryService;

    @Autowired
    public ScheduledUploadServiceImpl(@Value("${batchSize}") final int batchSize,
                                      final DomXmlParserService domXmlParserService,
                                      final EntryService entryService) {
        this.batchSize = batchSize;
        this.domXmlParserService = domXmlParserService;
        this.entryService = entryService;
    }

    @Scheduled
    public void uploadFiles() {
        final BlockingQueue<Tuple2<String, byte[]>> blockingQueue = new LinkedBlockingQueue<>();
    }
}

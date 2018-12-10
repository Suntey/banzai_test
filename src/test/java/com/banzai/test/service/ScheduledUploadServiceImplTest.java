package com.banzai.test.service;

import com.banzai.test.dto.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ScheduledUploadServiceImplTest {

    @Autowired
    private ScheduledUploadServiceImpl scheduledUploadService;

    @Autowired
    private EntryService entryService;

    @Transactional
    @Test
    public void uploadFiles() throws InterruptedException {
        scheduledUploadService.uploadFiles();
        final Collection<Entry> entriesByCreationDate = entryService.getEntriesByCreationDate("2014-01-01 00:00:00");
        assertEquals(1, entriesByCreationDate.size());

    }
}
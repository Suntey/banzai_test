package com.banzai.test.service;

import com.banzai.test.dto.Entry;
import com.banzai.test.service.entry.EntryService;
import com.banzai.test.service.folders.LocalNetworkFoldersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.FieldSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ScheduledUploadServiceImplTest {

    @Autowired
    private ScheduledUploadServiceImpl scheduledUploadService;

    @Mock
    private LocalNetworkFoldersService localNetworkFoldersService;

    @Autowired
    private EntryService entryService;

    private Path testXmlFile;

    @Before
    public void setUp() {
        final ClassLoader classLoader = getClass().getClassLoader();
        testXmlFile = Paths.get(classLoader.getResource("entry/test1.xml").getFile());

        FieldSetter.setField(scheduledUploadService);
    }

    @Transactional
    @Test
    public void uploadFiles() {
        scheduledUploadService.uploadFiles();
        final Collection<Entry> entriesByCreationDate = entryService.getEntriesByCreationDate("2014-01-01 00:00:00");
        assertEquals(1, entriesByCreationDate.size());

    }
}
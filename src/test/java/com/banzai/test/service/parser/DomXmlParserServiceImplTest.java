package com.banzai.test.service.parser;

import com.banzai.test.dto.Entry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DomXmlParserServiceImplTest {

    private Path testXmlFile;

    @Autowired
    private DomXmlParserService domXmlParserService;

    @Before
    public void setUp() {
        final ClassLoader classLoader = getClass().getClassLoader();
        testXmlFile = Paths.get(classLoader.getResource("entry/test1.xml").getFile());
    }

    @Test
    public void parseXmlFile() throws IOException {
        final byte[] bytes = Files.readAllBytes(testXmlFile);
        final Optional<Entry> test1 = domXmlParserService.parseXmlFile("test1", bytes);
        assertTrue(test1.isPresent());
        assertNotNull(test1.get());
        assertEquals("Содержимое записи", test1.get().getContent());
        assertEquals("2014-01-01 00:00:00", test1.get().getCreationDate());
        assertEquals("test1", test1.get().getFileName());
    }
}
package com.banzai.test.service.parser;

import com.banzai.test.dto.Entry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class DomXmlParserServiceImplTest {

    @Value("${directory.sourceFiles}")
    private String directoryFrom;

    @Autowired
    private DomXmlParserService domXmlParserService;

    @Test
    public void parseXmlFile() throws IOException {
        final byte[] bytes = Files.readAllBytes(Paths.get(directoryFrom));
        final Optional<Entry> test1 = domXmlParserService.parseXmlFile("test1", bytes);
        assertNotNull(test1);
        assertTrue(test1.isPresent());
    }
}
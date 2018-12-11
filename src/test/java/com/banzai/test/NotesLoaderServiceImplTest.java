//package com.banzai.test;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.internal.util.reflection.FieldSetter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.transaction.annotation.Transactional;
//import ru.alfacapital.dwhnotesupload.dto.FtpFolder;
//import ru.alfacapital.dwhnotesupload.repository.notesave.StructureNotesRepository;
//import ru.alfacapital.dwhnotesupload.service.XmlToRootObjectService;
//import ru.alfacapital.dwhnotesupload.service.email.EmailSenderService;
//import ru.alfacapital.dwhnotesupload.service.excel.ExcelTransformerService;
//import ru.alfacapital.dwhnotesupload.service.ftp.FtpFolderService;
//import ru.alfacapital.dwhnotesupload.service.ftp.FtpService;
//import ru.alfacapital.dwhnotesupload.service.notesave.UltimateNotesLoaderService;
//import ru.alfacapital.dwhnotesupload.service.validation.XmlValidatorService;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Collections;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.ArgumentMatchers.isNull;
//import static org.mockito.Mockito.*;
//
///**
// * Created by Kuznetsov A.S. 12.10.2018
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@ActiveProfiles("test")
//public class NotesLoaderServiceImplTest {
//
//    @Mock
//    private FtpFolderService ftpFolderService;
//
//    @Mock
//    private FtpService ftpService;
//
//    private NotesLoaderService notesLoaderService;
//
//    @Autowired
//    private UltimateNotesLoaderService ultimateNotesLoaderService;
//
//    @Autowired
//    private XmlToRootObjectService xmlToRootObjectService;
//
//    @Mock
//    private EmailSenderService emailSenderService;
//
//    @Autowired
//    private XmlValidatorService xmlValidatorService;
//
//    @Autowired
//    private ExcelTransformerService excelTransformerService;
//
//    @Autowired
//    private StructureNotesRepository structureNotesRepository;
//
//    @Before
//    public void init() throws NoSuchFieldException, IOException {
//        notesLoaderService = new NotesLoaderServiceImpl(ultimateNotesLoaderService, xmlToRootObjectService,
//                                                                ftpService, ftpFolderService, emailSenderService,
//                                                                xmlValidatorService, excelTransformerService);
//        FieldSetter.setField(notesLoaderService, notesLoaderService.getClass().getDeclaredField("email"), "testvalue@test.ru");
//        when(ftpFolderService.getFolders()).thenReturn(getTestFtpFoldersCollection());
//
//        doReturn(getTestXmlFile()).when(ftpService).getCurrentDateXmlFilesByCurrentDate(any(FtpFolder.class), anyString());
//
//        doNothing().when(emailSenderService).sendMailWithCopies(any(String[].class), any(String[].class), any(String[].class),
//                anyString(), anyString(), any());
//        doNothing().when(emailSenderService).sendMailWithCopies(any(String[].class), any(String[].class), any(String[].class),
//                anyString(), anyString(), isNull());
//    }
//
//    private Collection<FtpFolder> getTestFtpFoldersCollection() {
//        return Collections.singletonList(FtpFolder.builder()
//                .ftpHost("0.0.0.0")
//                .pass("testPass")
//                .rootFolder("test")
//                .user("test")
//                .port(0)
//                .build());
//    }
//
//    private Collection<File> getTestXmlFile() throws IOException {
//        ClassLoader classLoader = getClass().getClassLoader();
//        final File testXmlFile = new File(classLoader.getResource("xml/testNote.xml").getFile());
//        final File testNoteTmp = File.createTempFile("testNote", ".xml");
//        FileUtils.copyFile(testXmlFile, testNoteTmp);
//
//        return Collections.singletonList(testNoteTmp);
//    }
//
//    @Transactional
//    @Test
//    public void cronNotesLoad() {
//        notesLoaderService.cronNotesLoad();
//        final Collection<String> structNotesResponse = structureNotesRepository.getAlreadyExistedIsins(Collections.singletonList("TS0000000000"));
//        assertFalse(structNotesResponse.isEmpty());
//        assertEquals(1, structNotesResponse.size());
//        assertEquals("TS0000000000", structNotesResponse.toArray()[0]);
//    }
//}
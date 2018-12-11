package com.banzai.test.service.folders;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

/**
 * Created by Kuznetsov A.S. 06.11.2018
 */
@Service
@Slf4j
public class LocalNetworkFoldersServiceImpl implements LocalNetworkFoldersService {

    private final File sourceFile;

    private final String successProcessFileDir;

    private final File errorProcessFileDir;

    public LocalNetworkFoldersServiceImpl(@Value("${directory.sourceFiles}") final File sourceFile,
                                          @Value("${directory.completedFiles}") final String successProcessFileDir,
                                          @Value("${directory.notCompletedFiles}") final String errorProcessFileDir) {
        this.sourceFile = sourceFile;
        this.successProcessFileDir = successProcessFileDir;
        this.errorProcessFileDir = new File(errorProcessFileDir);
    }


    @Override
    public void moveFiles(final Collection<String> fileNames, final boolean isSuccess) {
       fileNames.forEach(fileName -> moveFile(fileName, isSuccess));
    }

    private void moveFile(final String fileName, final boolean isSuccess) {
        try {
            if (isSuccess) {
                FileUtils.moveFile(sourceFile, new File(successProcessFileDir + fileName));
                log.info("File {} moved to success-directory", fileName);
            } else {
                FileUtils.moveFile(sourceFile, new File(errorProcessFileDir + fileName));
                log.info("File {} moved to error-directory", fileName);
            }

        } catch (Exception e) {
            final String errorMessage = String.format("Error while moving file %s to directory", fileName);
            log.error(errorMessage, e);
        }
    }
}

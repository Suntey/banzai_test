package com.banzai.test.service.folders;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

/**
 * Created by Kuznetsov A.S. 06.11.2018
 */
@Service
@Profile("!test")
@Slf4j
public class LocalNetworkFoldersServiceImpl implements LocalNetworkFoldersService {

    private final String sourceFile;

    private final String successProcessFileDir;

    private final String errorProcessFileDir;

    public LocalNetworkFoldersServiceImpl(@Value("${directory.sourceFiles}") final String sourceFile,
                                          @Value("${directory.completedFiles}") final String successProcessFileDir,
                                          @Value("${directory.notCompletedFiles}") final String errorProcessFileDir) {
        this.sourceFile = sourceFile;
        this.successProcessFileDir = successProcessFileDir;
        this.errorProcessFileDir = errorProcessFileDir;
    }


    @Override
    public void moveFiles(final Collection<String> fileNames, final boolean isSuccess) {
       fileNames.forEach(fileName -> moveFile(fileName, isSuccess));
    }

    private void moveFile(final String fileName, final boolean isSuccess) {
        try {
            if (isSuccess) {
                FileUtils.moveFile(createFile(sourceFile, fileName), createFile(successProcessFileDir, fileName));
                log.info("File {} moved to success-directory", fileName);
            } else {
                FileUtils.moveFile(createFile(sourceFile, fileName), createFile(errorProcessFileDir, fileName));
                log.info("File {} moved to error-directory", fileName);
            }

        } catch (Exception e) {
            final String errorMessage = String.format("Error while moving file %s to directory", fileName);
            log.error(errorMessage, e);
        }
    }

    private File createFile(final String sourcePath, final String fileName) {
        return new File(sourcePath+fileName);
    }
}

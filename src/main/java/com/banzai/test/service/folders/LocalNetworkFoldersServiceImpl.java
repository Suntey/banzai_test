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

    public LocalNetworkFoldersServiceImpl(@Value("${Path.dirFrom}") final File sourceFile,
                                          @Value("${Path.dirMoveTo}") final String dirFileTo,
                                          @Value("${Path.dirError}") final String backupFileTo) {
        this.sourceFile = sourceFile;
        this.successProcessFileDir = dirFileTo;
        this.errorProcessFileDir = new File(backupFileTo);
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

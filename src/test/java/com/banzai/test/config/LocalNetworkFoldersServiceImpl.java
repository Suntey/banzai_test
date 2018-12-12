package com.banzai.test.config;

import com.banzai.test.service.folders.LocalNetworkFoldersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Kuznetsov A.S. 12.12.2018
 */
@Slf4j
@Service
@Profile("test")
public class LocalNetworkFoldersServiceImpl implements LocalNetworkFoldersService {
    @Override
    public void moveFiles(final Collection<String> fileNames, final boolean isSuccess) {
        log.info("Test method moveFiles executing and do nothing!");
    }
}

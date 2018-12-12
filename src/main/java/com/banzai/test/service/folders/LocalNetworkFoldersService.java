package com.banzai.test.service.folders;

import java.util.Collection;

/**
 * Created by Kuznetsov A.S. 06.11.2018
 */
public interface LocalNetworkFoldersService {
    void moveFiles(Collection<String> fileNames, boolean isSuccess);
}

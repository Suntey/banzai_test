package com.banzai.test.service;

import java.io.File;
import java.util.Collection;

public interface FileService {
    Collection<File> getFilesFromFolder(String path);
}

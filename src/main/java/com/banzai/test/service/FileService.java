package com.banzai.test.service;

import com.banzai.test.dto.Entry;

import java.io.File;
import java.util.Collection;

public interface FileService {
    Collection<Entry> getParsedObjectsFromFolder(String path);
}

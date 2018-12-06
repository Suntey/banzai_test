package com.banzai.test.service;

import com.banzai.test.dto.Entry;

import java.util.Collection;

public interface EntryService {
    void batchSaveEntries(Collection<Entry> entries);
}

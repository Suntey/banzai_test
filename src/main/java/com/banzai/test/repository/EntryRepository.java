package com.banzai.test.repository;

import com.banzai.test.dto.Entry;

import java.util.Collection;

public interface EntryRepository {
    void batchSaveEntries(Collection<Entry> entries);
}

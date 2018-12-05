package com.banzai.test.repository;

import com.banzai.test.dto.Entry;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class EntryRepositoryImpl implements EntryRepository {
    @Override
    public void batchSaveEntries(Collection<Entry> entries) {
        return;
    }
}

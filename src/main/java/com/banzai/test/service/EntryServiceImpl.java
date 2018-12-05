package com.banzai.test.service;

import com.banzai.test.dto.Entry;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class EntryServiceImpl implements EntryService {

    @Override
    public Collection<String> batchSaveEntries(final Collection<Entry> entries) {
        return null;
    }
}

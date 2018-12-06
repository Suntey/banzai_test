package com.banzai.test.service;

import com.banzai.test.dto.Entry;
import com.banzai.test.repository.EntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Slf4j
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;

    @Autowired
    public EntryServiceImpl(final EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchSaveEntries(final Collection<Entry> entries) {
        log.info("Method EntryServiceImpl.batchSaveEntries executing!" +
                 "\nCollection size: {}", entries.size());

        entryRepository.batchSaveEntries(entries);
    }
}

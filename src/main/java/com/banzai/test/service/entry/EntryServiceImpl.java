package com.banzai.test.service.entry;

import com.banzai.test.dto.Entry;
import com.banzai.test.repository.EntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

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

    @Transactional(readOnly = true)
    @Override
    public Collection<Entry> getEntriesByCreationDate(final String date) {
        try {
            return entryRepository.getEntriesByCreationDate(date);
        } catch (Exception e) {
            log.error("Error in method EntryServiceImpl.getEntriesByCreationDate(String date)", e);
            return Collections.emptyList();
        }
    }
}

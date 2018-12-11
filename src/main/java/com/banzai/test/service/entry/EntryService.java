package com.banzai.test.service.entry;

import com.banzai.test.dto.Entry;

import java.util.Collection;

public interface EntryService {
    void batchSaveEntries(Collection<Entry> entries);

    /**
     * Находит по заданной дате сохраненные сущности entry
     * @param date - строка даты вида yyyy-MM-dd HH:mm:ss (Ex: 2014-01-01 00:00:00)
     * @return
     */
    Collection<Entry> getEntriesByCreationDate(final String date);
}

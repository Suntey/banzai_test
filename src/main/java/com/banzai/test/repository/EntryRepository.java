package com.banzai.test.repository;

import java.util.Collection;

public interface EntryRepository {
    boolean batchSaveEntries(Collection entries);
}

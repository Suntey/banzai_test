package com.banzai.test.service.parser;

import com.banzai.test.dto.Entry;
import java.util.Optional;

/**
 * Created by Kuznetsov A.S. 04.12.2018
 */
public interface DomXmlParserService {
    Optional<Entry> parseXmlFile(final String xmlFileName, final byte[] bytes);
}

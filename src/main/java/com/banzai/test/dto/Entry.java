package com.banzai.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Entry {
    private String content;
    private String creationDate;
}

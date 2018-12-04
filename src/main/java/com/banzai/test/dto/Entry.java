package com.banzai.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Entry {
    private String content;
    private String creationDate;
    private String fileName;

    public boolean isEmpty() {
        return content == null && creationDate == null;
    }
}

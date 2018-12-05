package com.banzai.test.dto;

import lombok.*;

/**
 * Created by Kuznetsov A.S. 18.09.2018
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Tuple2<T, V> {
    @Getter
    @Setter
    private T value1;

    @Getter
    @Setter
    private V value2;
}

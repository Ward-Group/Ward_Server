package com.ward.ward_server.api.item.entity.enumtype;

import com.ward.ward_server.global.Object.enums.Sort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SortTest {

    @Test
    void string변환값_체크() {
        Sort sort = Sort.DUE_TODAY;
        Assertions.assertThat(sort.toString()).isEqualTo("DUE_TODAY");
    }

}
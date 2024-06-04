package com.ward.ward_server.api.item.entity.enumtype;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SortTest {

    @Test
    void string변환값_체크() {
        ItemSort sort = ItemSort.DUE_TODAY;
        Assertions.assertThat(sort.toString()).isEqualTo("DUE_TODAY");
    }

}
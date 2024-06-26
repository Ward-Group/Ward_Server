package com.ward.ward_server.api.item.entity.enumtype;

import com.ward.ward_server.global.Object.enums.HomeSort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class HomeSortTest {

    @Test
    void string변환값_체크() {
        HomeSort homeSort = HomeSort.DUE_TODAY;
        Assertions.assertThat(homeSort.toString()).isEqualTo("DUE_TODAY");
    }

}
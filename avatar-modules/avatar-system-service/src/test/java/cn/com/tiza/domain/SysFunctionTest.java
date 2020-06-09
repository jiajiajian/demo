package cn.com.tiza.domain;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SysFunctionTest {

    @Test
    void testEquals() {
        Set<Long> ids = Sets.newHashSet(123456L, 2390984L);
        assertTrue(ids.contains(123456L));
    }
}
package cn.com.tiza.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OrganizationTest {

    @ParameterizedTest(name = "{0}, {1}, {2} = {3}")
    @CsvSource({
            "1,    -1,   -1/, -1/1/",
            "2,    1,   -1/1/, -1/1/2/",
            "3,  1, -1/1/, -1/1/3/",
            //"1,  100, 101"
    })
    @DisplayName("check Organization.path is right")
    public void genPath(Long id, Long parentId, String parentPath, String expectPath) {
        Organization parent = new Organization();
        parent.setId(parentId);
        parent.setPath(parentPath);

        Organization org = new Organization();
        org.setId(id);
        org.setPath(org.genPath(parent));
        org.setParentOrgId(parentId);

        assertThat(org.getPath(), is(expectPath));
    }
}
package com.techdegree.instateam.model;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class RoleTest {
    // using reflection to test @Pattern, not 100% sure how it is
    // working, but thanks to this post:
    // http://stackoverflow.com/questions/29069956/how-to-test-validation-annotations-of-a-class-using-junit
    // won't check NoSuchFieldException, because it's testing
    private void nameRegex(String name, boolean validates)
            throws NoSuchFieldException {
        Field field = Role.class.getDeclaredField("name");
        javax.validation.constraints.Pattern[] annotations =
                field.getAnnotationsByType(
                        javax.validation.constraints.Pattern.class
                );
        assertEquals(name.matches(annotations[0].regexp()), validates);
    }

    @Test
    public void roleWithOnlySpacesIsWrong() throws Exception {
        nameRegex(" ", false);
    }

    @Test
    public void roleWithOneNameNoSpacesWorks() throws Exception {
        nameRegex("name", true);
    }

    @Test
    public void roleWithOneNameTrailingLeadingSpacesWorks() throws Exception {
        nameRegex("  name   ", true);
    }

    @Test
    public void roleWithFourNamesTrailingLeadingSpacesWorks() throws Exception {
        nameRegex("  Name  name2 name 3 ", true);
    }
}
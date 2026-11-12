package io.github.tvallot.cvsearch;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import io.github.tvallot.cvsearch.model.Applicant;
import io.github.tvallot.cvsearch.model.ApplicantBuilder;
import io.github.tvallot.cvsearch.model.ApplicantList;
import io.github.tvallot.cvsearch.model.ApplicantListBuilder;
import io.github.tvallot.cvsearch.model.Experience;
import java.util.List;


/**
 * Tests for the Applicant class.
 */
public class ApplicantTest {

    /** Check that the builder works for a basic Yaml file. */
    @Test
    public void readApplicant() {
        // Given
        ApplicantBuilder builder = new ApplicantBuilder("data/applicants/applicant1.yaml");

        // When
        Applicant a = builder.build();

        // Then
        assertThat(70, is(a.getSkill("c++")));
        assertThat("John Smith", is(a.getName()));
    }

    /** Check that the builder can read several files in the same directory. */
    @Test
    public void readManyApplicant() {
        // Given
        ApplicantListBuilder builder = new ApplicantListBuilder(new File("data/applicants/"));

        // When
        ApplicantList list = builder.build();

        // Then
        boolean johnFound = false;
        for (Applicant a : list) {
            if (a.getName().equals("John Smith")) {
                assertThat(90, is(a.getSkill("c")));
                assertThat(70, is(a.getSkill("c++")));
                johnFound = true;
            }
        }
        assertThat(johnFound, is(true));
    }

    /** Test setting and getting a skill. */
    @Test
    public void testSetAndGetSkill() {
        Applicant a = new Applicant();
        a.setSkill("java", 85);
        assertThat(a.getSkill("java"), is(85));
    }

    /** Test getting a skill that was not set returns zero. */
    @Test
    public void testGetUnknownSkillReturnsZero() {
        Applicant a = new Applicant();
        assertThat(a.getSkill("nonexistent"), is(0));
    }

    /** Test setting and getting the name. */
    @Test
    public void testSetAndGetName() {
        Applicant a = new Applicant();
        a.setName("Alice");
        assertThat(a.getName(), is("Alice"));
    }

    /** Test adding and getting experiences. */
    @Test
    public void testAddAndGetExperience() {
        Applicant a = new Applicant();
        Experience exp = new Experience("Google", 2018, 2021, List.of("java", "cloud"));
        a.addExperience(exp);

        List<Experience> experiences = a.getExperiences();
        assertThat(experiences.size(), is(1));
        assertThat(experiences.get(0).getCompany(), is("Google"));
    }

}

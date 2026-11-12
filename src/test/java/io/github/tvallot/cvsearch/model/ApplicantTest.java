package io.github.tvallot.cvsearch.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link Applicant} and the YAML builders.
 */
class ApplicantTest {

    @Test
    void readsApplicantFromYaml() {
        Applicant a = new ApplicantBuilder("data/applicants/applicant1.yaml").build();

        assertThat(a.getName(), is("John Smith"));
        assertThat(a.getSkill("c++"), is(70));
        assertThat(a.getExperiences().size(), is(2));
        assertThat(a.getExperiences().get(0).company(), is("Google"));
    }

    @Test
    void readsEveryFileOfDirectory() {
        ApplicantList list = new ApplicantListBuilder(new File("data/applicants/")).build();

        assertThat(list.size(), is(12));
        boolean johnFound = false;
        for (Applicant a : list) {
            if (a.getName().equals("John Smith")) {
                assertThat(a.getSkill("c"), is(90));
                johnFound = true;
            }
        }
        assertThat(johnFound, is(true));
    }

    @Test
    void storesSkillNamesInLowerCase(@TempDir final Path dir) throws IOException {
        Path file = dir.resolve("a.yaml");
        Files.writeString(file, "name: Jane\nskills:\n  Java: 80\n");

        Applicant a = new ApplicantBuilder(file.toFile()).build();

        assertThat(a.getSkill("java"), is(80));
    }

    @Test
    void rejectsFileWithoutName(@TempDir final Path dir) throws IOException {
        Path file = dir.resolve("a.yaml");
        Files.writeString(file, "skills:\n  java: 80\n");

        assertThrows(IllegalArgumentException.class,
                () -> new ApplicantBuilder(file.toFile()).build());
    }

    @Test
    void rejectsMissingDirectory() {
        assertThrows(IllegalArgumentException.class,
                () -> new ApplicantListBuilder(new File("does/not/exist")).build());
    }

    @Test
    void unknownSkillHasLevelZero() {
        assertThat(new Applicant().getSkill("cobol"), is(0));
    }

    @Test
    void emptySearchUsesAllSkills() {
        Applicant a = new Applicant();
        a.setSkill("java", 80);
        a.setSkill("go", 40);

        assertThat(a.skillsToEvaluate(List.of()), containsInAnyOrder("java", "go"));
        assertThat(a.skillsToEvaluate(null), containsInAnyOrder("java", "go"));
        assertThat(a.skillsToEvaluate(List.of("rust")), contains("rust"));
    }

    @Test
    void sumsYearsOfExperiencePerSkill() {
        Applicant a = new Applicant();
        a.addExperience(new Experience("A", 2010, 2015, List.of("java")));
        a.addExperience(new Experience("B", 2015, 2018, List.of("java", "go")));

        assertThat(a.yearsOfExperience("java"), is(8));
        assertThat(a.yearsOfExperience("go"), is(3));
        assertThat(a.yearsOfExperience("rust"), is(0));
    }

    @Test
    void experienceCannotEndBeforeItStarts() {
        assertThrows(IllegalArgumentException.class,
                () -> new Experience("A", 2020, 2010, List.of()));
    }
}

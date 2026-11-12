package io.github.tvallot.cvsearch.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * Builds an {@link Applicant} from a YAML file.
 *
 * <p>Expected format:
 * <pre>
 * name: John Smith
 * skills:
 *   java: 50
 * experience:
 *   Google:
 *     start: 2005
 *     end: 2010
 *     keywords: [java]
 * </pre>
 * Skill names are stored in lower case so that searches are case-insensitive.
 */
public class ApplicantBuilder {

    private final File file;

    /**
     * @param file YAML file describing the applicant.
     */
    public ApplicantBuilder(final File file) {
        this.file = file;
    }

    /**
     * @param filename path of the YAML file describing the applicant.
     */
    public ApplicantBuilder(final String filename) {
        this(new File(filename));
    }

    /**
     * Reads the file and builds the applicant.
     *
     * @throws UncheckedIOException     if the file cannot be read
     * @throws IllegalArgumentException if the file content is not a valid applicant
     */
    public Applicant build() {
        Map<String, Object> content = readYaml();
        Applicant applicant = new Applicant();

        if (!(content.get("name") instanceof String name)) {
            throw invalid("missing name");
        }
        applicant.setName(name);

        for (Map.Entry<String, Object> skill : asMap(content.get("skills"), "skills").entrySet()) {
            if (!(skill.getValue() instanceof Number level)) {
                throw invalid("level of skill " + skill.getKey() + " is not a number");
            }
            applicant.setSkill(normalize(skill.getKey()), level.intValue());
        }

        Object experience = content.get("experience");
        if (experience != null) {
            for (Map.Entry<String, Object> entry : asMap(experience, "experience").entrySet()) {
                applicant.addExperience(toExperience(entry.getKey(), entry.getValue()));
            }
        }
        return applicant;
    }

    private Map<String, Object> readYaml() {
        try (InputStream in = new FileInputStream(file)) {
            return asMap(new Yaml().load(in), "root");
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read applicant file " + file, e);
        }
    }

    private Experience toExperience(final String company, final Object value) {
        Map<String, Object> data = asMap(value, "experience." + company);
        if (!(data.get("start") instanceof Number start)
                || !(data.get("end") instanceof Number end)) {
            throw invalid("experience at " + company + " needs a numeric start and end");
        }
        List<String> keywords = data.get("keywords") instanceof List<?> list
                ? list.stream().map(k -> normalize(String.valueOf(k))).toList()
                : List.of();
        return new Experience(company, start.intValue(), end.intValue(), keywords);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(final Object value, final String field) {
        if (!(value instanceof Map)) {
            throw invalid(field + " should be a mapping");
        }
        return (Map<String, Object>) value;
    }

    private IllegalArgumentException invalid(final String reason) {
        return new IllegalArgumentException("Invalid applicant file " + file + ": " + reason);
    }

    private static String normalize(final String skill) {
        return skill.strip().toLowerCase(Locale.ROOT);
    }
}

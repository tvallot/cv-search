package io.github.tvallot.cvsearch.model;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Builds an {@link ApplicantList} from every YAML file of a directory.
 */
public class ApplicantListBuilder {

    private final File directory;

    /**
     * @param directory directory containing one YAML file per applicant.
     */
    public ApplicantListBuilder(final File directory) {
        this.directory = directory;
    }

    /**
     * Reads every {@code .yaml} file of the directory, in alphabetical order.
     *
     * @throws IllegalArgumentException if the directory does not exist
     */
    public ApplicantList build() {
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".yaml"));
        if (files == null) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }
        Arrays.sort(files, Comparator.comparing(File::getName));

        ApplicantList applicants = new ApplicantList();
        for (File file : files) {
            applicants.add(new ApplicantBuilder(file).build());
        }
        return applicants;
    }
}

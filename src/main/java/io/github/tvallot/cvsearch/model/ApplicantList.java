package io.github.tvallot.cvsearch.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The set of applicants the search runs on.
 */
public class ApplicantList implements Iterable<Applicant> {
    private final List<Applicant> list = new ArrayList<>();

    /** Adds an applicant to the list. */
    public void add(final Applicant applicant) {
        list.add(applicant);
    }

    /** Returns the number of applicants in the list. */
    public int size() {
        return list.size();
    }

    @Override
    public Iterator<Applicant> iterator() {
        return list.iterator();
    }
}

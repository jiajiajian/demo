package cn.com.tiza.errors;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * @see <a href="https://tools.ietf.org/html/rfc7807">RFC 7807: Problem Details for HTTP APIs</a>
 */
public interface Problem {

    static ProblemBuilder builder() {
        return new ProblemBuilder();
    }

    /**
     * A short, human-readable summary of the problem type. It SHOULD NOT
     * change from occurrence to occurrence of the problem, except for
     * purposes of localisation.
     *
     * @return a short, human-readable summary of this problem
     */
    default String getTitle() {
        return null;
    }

    /**
     * The HTTP status code generated by the origin server for this
     * occurrence of the problem.
     *
     * @return the HTTP status code
     */
    default StatusType getStatus() {
        return null;
    }

    /**
     * Optional, additional attributes of the problem. Implementations can choose to ignore this in favor of concrete,
     * typed fields.
     *
     * @return additional parameters
     */
    default Map<String, Object> getParameters() {
        return Collections.emptyMap();
    }

    static ThrowableProblem valueOf(final StatusType status) {
        return Problem.builder()
                .withStatus(status).build();
    }

    static String toString(final Problem problem) {
        final Stream<String> parts = Stream.concat(
                Stream.of(
                        problem.getStatus() != null ? null : String.valueOf(problem.getStatus().getStatusCode()),
                        problem.getTitle()),
                problem.getParameters()
                        .entrySet().stream()
                        .map(Map.Entry::toString))
                .filter(Objects::nonNull);

        return parts.collect(joining(", "));
    }

}

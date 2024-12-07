// Rahul Padhi 
// ECS 160

package hwstreams;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GitHubProc {

    /**
     * Returns the number of occurrences of a word in the body of the provided comments.
     */
    public static Long getWordCount(Stream<GitHubComment> stream, String word) {
        return stream
                .parallel()
                .flatMap(comment -> Stream.of(Util.getWords(comment.body())))
                .filter(w -> w.equals(word))
                .count();
    }

    /**
     * Returns a stream of GitHubComments that contain a URL in the body.
     */
    public static Stream<GitHubComment> filterCommentsWithUrl(Stream<GitHubComment> comments) {
        return comments
                .parallel()
                .filter(comment -> comment.body().contains("http://") || comment.body().contains("https://"));
    }

    /**
     * Returns a map where the keys are authors and the values are the number of their
     * comments that contain any URL in the body.
     */
    public static Map<String, Long> getCommentUrlAuthorCount(Stream<GitHubComment> stream) {
        return filterCommentsWithUrl(stream)
                .collect(Collectors.groupingBy(GitHubComment::author, Collectors.counting()));
    }

    /**
     * Returns a map where the keys are projects, and the values are the number of comments
     * relating to it in the dataset.
     */
    public static Map<String, Long> getPerProjectCount(Stream<GitHubComment> stream) {
        return stream
                .parallel()
                .collect(Collectors.groupingBy(Util::getProject, Collectors.counting()));
    }

    /**
     * Returns a map where the keys are authors, and the values are the number of comments they have.
     */
    public static Map<String, Long> getAuthorActivity(Stream<GitHubComment> stream) {
        return stream
                .parallel()
                .collect(Collectors.groupingBy(GitHubComment::author, Collectors.counting()));
    }

    /**
     * Returns a map where the keys are authors, and the values are the average length of each author's comments.
     */
    public static Map<String, Double> getAuthorAverageVerbosity(Stream<GitHubComment> stream) {
        return stream
                .parallel()
                .collect(Collectors.groupingBy(
                        GitHubComment::author,
                        Collectors.averagingInt(comment -> Util.getWords(comment.body()).length)
                ));
    }

    /**
     * Returns a nested map where the keys are projects, and the values are maps. These inner maps
     * have authors as keys and the number of occurrences of a given word in the body as values.
     */
    public static Map<String, Map<String, Long>> getAuthorWordCountPerProject(
            Stream<GitHubComment> stream, String word) {
        return stream
                .parallel()
                .collect(Collectors.groupingBy(
                        Util::getProject,
                        Collectors.groupingBy(
                                GitHubComment::author,
                                Collectors.summingLong(
                                        comment -> Stream.of(Util.getWords(comment.body()))
                                                .filter(w -> w.equals(word))
                                                .count()
                                )
                        )
                ));
    }
}


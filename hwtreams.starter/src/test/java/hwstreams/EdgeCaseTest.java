// Rahul Padhi 
// ECS 160

// extra testing 

package hwstreams;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class EdgeCaseTest {

    // Test case to ensure handling of projects with no comments
    @Test
    public void shouldHandleProjectsWithNoComments() {
        var testComments = Stream.of(
            new GitHubComment("1", "abc", "https://github.com/project1/repo1", "author1", "2023-12-06", "Some comment"),
            new GitHubComment("2", "def", "https://github.com/project2/repo2", "author2", "2023-12-06", "Another comment"));

        // Filter for a project with no comments
        var result = GitHubProc.getPerProjectCount(testComments.filter(comment -> comment.url().contains("repo3")));

        assertEquals(Map.of(), result);
    }

    // Test case for authors with no comments
    @Test
    public void shouldHandleAuthorsWithNoComments() {
        var testComments = Stream.of(
            new GitHubComment("1", "abc", "https://github.com/project1/repo1", "author1", "2023-12-06", "Some comment"),
            new GitHubComment("2", "def", "https://github.com/project1/repo1", "author2", "2023-12-06", "Another comment"));

        // Get activity for an author with no comments
        var result = GitHubProc.getAuthorActivity(testComments.filter(comment -> comment.author().equals("author3")));

        assertEquals(Map.of(), result);
    }

    // Test case for identical comments across different projects
    @Test
    public void shouldHandleIdenticalCommentsAcrossProjects() {
        var testComments = Stream.of(
            new GitHubComment("1", "abc", "https://github.com/project1/repo1", "author1", "2023-12-06", "Fix this bug"),
            new GitHubComment("2", "def", "https://github.com/project2/repo2", "author1", "2023-12-06", "Fix this bug"),
            new GitHubComment("3", "ghi", "https://github.com/project2/repo2", "author2", "2023-12-06", "Fix this bug"));

        var result = GitHubProc.getAuthorWordCountPerProject(testComments, "Fix");

        var expected = new HashMap<String, Map<String, Long>>();
        expected.put("project1/repo1", Map.of("author1", 1L));
        expected.put("project2/repo2", Map.of("author1", 1L, "author2", 1L));

        assertEquals(expected, result);
    }

    // Test case for duplicate comments in the same project
    @Test
    public void shouldHandleDuplicateCommentsInSameProject() {
        var testComments = Stream.of(
            new GitHubComment("1", "abc", "https://github.com/project1/repo1", "author1", "2023-12-06", "Duplicate comment"),
            new GitHubComment("2", "def", "https://github.com/project1/repo1", "author1", "2023-12-06", "Duplicate comment"));

        var result = GitHubProc.getWordCount(testComments, "Duplicate");

        assertEquals(2L, result); // Counts duplicates correctly
    }

    // Test case for large datasets with identical data
    @Test
    public void shouldHandleLargeIdenticalDataset() {
        var largeStream = Stream.generate(() -> 
            new GitHubComment("1", "abc", "https://github.com/project1/repo1", "author1", "2023-12-06", "Repeated comment"))
            .limit(1_000_000); // 1 million identical comments

        var result = GitHubProc.getWordCount(largeStream, "Repeated");

        assertEquals(1_000_000L, result); // Handles large datasets
    }
}


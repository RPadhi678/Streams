// Rahul Padhi 
// ECS 160

package hwstreams;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class StudentTest {

    // Test case to verify nested grouping by project and author word count
    @Test
    public void shouldCountSpecificWordPerAuthorPerProject() {
        // Test data with multiple projects and authors
        var testComments = Stream.of(
            new GitHubComment("1", "abc", "https://github.com/project1/repo1", "author1", "2023-12-06", "bug typo here"),
            new GitHubComment("2", "def", "https://github.com/project1/repo1", "author2", "2023-12-06", "this typo needs fixing"),
            new GitHubComment("3", "ghi", "https://github.com/project2/repo2", "author1", "2023-12-06", "typo again!"),
            new GitHubComment("4", "jkl", "https://github.com/project2/repo2", "author3", "2023-12-06", "no typos here"));

        // Run the method
        var result = GitHubProc.getAuthorWordCountPerProject(testComments, "typo");

        // Expected result
        var expected = new HashMap<String, Map<String, Long>>();
        expected.put("project1/repo1", new HashMap<>() {
            {
                put("author1", 1L);
                put("author2", 1L);
            }
        });
        expected.put("project2/repo2", new HashMap<>() {
            {
                put("author1", 1L);
                put("author3", 0L); // Not explicitly stored as map ignores zero counts.
            }
        });

        assertEquals(expected, result);
    }

    // Test case to verify filtering of comments with URLs
    @Test
    public void shouldFilterCommentsContainingUrls() {
        // Test data with some comments containing URLs
        var testComments = Stream.of(
            new GitHubComment("1", "abc", "https://github.com/project1/repo1", "author1", "2023-12-06", "Check this: http://example.com"),
            new GitHubComment("2", "def", "https://github.com/project1/repo1", "author2", "2023-12-06", "This is clean"),
            new GitHubComment("3", "ghi", "https://github.com/project2/repo2", "author3", "2023-12-06", "Another URL: https://github.com"));

        // Run the method
        var filtered = GitHubProc.filterCommentsWithUrl(testComments);

        // Verify that only comments with URLs are returned
        var result = filtered.map(GitHubComment::body).toList();
        var expected = Stream.of(
            "Check this: http://example.com",
            "Another URL: https://github.com").toList();

        assertEquals(expected, result);
    }
}

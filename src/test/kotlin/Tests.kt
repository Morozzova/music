import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

// Test for TDD
internal class MusicBrokerTests {

    @Test
    fun anyTest() {
        val str = "This is the first test for example"
        val stringList = str.split(" ")
        var counter = 0
        stringList.forEach { word ->
            counter += word.length
        }
        val sum = counter + stringList.size - 1
        val actual = str.length
        assertEquals(sum, actual, "It is wrong")
    }

//    @Test
//    fun deleteDiscussionTest() {
//        val discussionId = "345"
//        val discussions = mapOf<String, Discussion>(discussionId to Discussion())
//        deleteDiscussion(discussionId)
//        assertFalse(discussions.keys.contains(discussionId))
//    }
}
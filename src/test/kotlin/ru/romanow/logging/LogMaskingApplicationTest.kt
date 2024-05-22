package ru.romanow.logging

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class LogMaskingApplicationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun test() {
        val request = TestObject(email = "romanowalex@mail.ru", firstName = "Алексей", lastName = "Романов")
        mockMvc.post("/api/v1/send") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
            .andExpect {
                status { isAccepted() }
            }
    }
}

data class TestObject(
    val email: String,
    val firstName: String,
    val lastName: String
)

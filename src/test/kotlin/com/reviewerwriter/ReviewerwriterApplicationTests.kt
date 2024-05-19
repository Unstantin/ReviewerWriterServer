package com.reviewerwriter

import com.fasterxml.jackson.databind.ObjectMapper
import com.reviewerwriter.dto.UserDTO
import com.reviewerwriter.dto.requests.ReviewCreateRequest
import com.reviewerwriter.dto.response.JwtInfo
import com.reviewerwriter.dto.response.PhotoUploadResponse
import com.reviewerwriter.dto.response.ReviewCreateResponse
import com.reviewerwriter.dto.response.ReviewInfo
import com.reviewerwriter.models.Criteria
import com.reviewerwriter.models.Tag
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTests @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
   companion object {
        val baseurl = "/v1/auth"
        var username = ""
        var password = ""

        @JvmStatic
        @BeforeAll
        fun init() {
            username = "TEST_USERNAME ${Random.nextInt()}"
            password = "TEST_PASSWORD ${Random.nextInt()}"
        }
    }

    @Test
    @DisplayName("REGISTRATION")
    fun registration() {
        val requestBody = UserDTO(username, password)

        mockMvc.post("$baseurl/reg") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }
    }

    @Test
    @DisplayName("LOG IN")
    fun login() {
        val requestBody = UserDTO(username, password)

        mockMvc.post("$baseurl/log") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
        }
            .andDo {
                print()
            }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }
    }
}

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ReviewControllerTests @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    companion object {
        val baseurl = "/v1"
        var username = ""
        var password = ""
        var jwt_token = ""
        var review_id = 0
        var filename = ""

        @JvmStatic
        @BeforeAll
        fun init() {
            username = "TEST_USERNAME ${Random.nextInt()}"
            password = "TEST_PASSWORD ${Random.nextInt()}"
        }
    }

    @Test
    @Order(1)
    @DisplayName("REGISTRATION")
    fun registration() {
        val requestBody = UserDTO(username, password)

        mockMvc.post("$baseurl/auth/reg") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }
    }

    @Test
    @Order(2)
    @DisplayName("LOG IN")
    fun login() {
        val requestBody = UserDTO(username, password)

        val response = mockMvc.post("$baseurl/auth/log") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
        }
            .andDo {
                print()
            }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andReturn().response.contentAsString

        val responseObject = objectMapper.readValue(response, JwtInfo::class.java)
        jwt_token = "Bearer ${responseObject.token}"
        print("JWT $jwt_token")
    }

    @Test
    @Order(3)
    @DisplayName("UPLOAD PHOTO")
    fun upload_photo() {
        val fileContent =
            File("C:\\Users\\KONSTANTIN\\Desktop\\разное\\3f5cf3845430bba518bc5e82e0f224b7.jpg").readBytes()

        val multipartFile = MockMultipartFile(
            "file",
            "filename.jpg",
            "image/jpeg",
            fileContent
        )

        val response = mockMvc.perform(multipart("$baseurl/photos")
            .file(multipartFile)
            .header("Authorization", jwt_token))
            .andReturn().response.contentAsString


        val responseObject = objectMapper.readValue(response, PhotoUploadResponse::class.java)
        filename = responseObject.filename
    }

    @Test
    @Order(4)
    @DisplayName("CREATE REVIEW")
    fun createReview() {
        val requestBody = ReviewCreateRequest(
            mainText = "MAIN TEXT EXMPL",
            shortText = "",
            tags = arrayListOf(Tag(name = "tag1", criteria = arrayListOf(Criteria(name = "criteria1")))),
            title = "TITLE",
            photos = arrayListOf(filename)
        )

        val response = mockMvc.post("$baseurl/reviews") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
            header("Authorization", jwt_token)
        }
            .andDo {
                print()
            }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andReturn().response.contentAsString

        val responseObject = objectMapper.readValue(response, ReviewCreateResponse::class.java)
        review_id = responseObject.reviewId
        print("review_id $review_id")
    }

    @Test
    @Order(5)
    @DisplayName("GET REVIEW INFO")
    fun getReviewInfo() {
        val response = mockMvc.get("$baseurl/reviews/$review_id") {
            header("Authorization", jwt_token)
        }
            .andDo {
                print()
            }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
            }.andReturn().response.contentAsString

        val cur_filename = objectMapper.readValue(response, ReviewInfo::class.java).photos?.get(0)
        val photo_code = mockMvc.get("$baseurl/photos/$cur_filename") {
            header("Authorization", jwt_token)
        }
            .andDo {
                print()
            }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_OCTET_STREAM) }
            }.andReturn().response.contentAsByteArray


        val destination = "C:\\Users\\KONSTANTIN\\Desktop\\test_input\\${Random.nextInt()}.jpg"
        val file = File(destination)
        val fos = FileOutputStream(file)
        fos.write(photo_code)
        fos.close()
    }

    @Test
    @Order(6)
    @DisplayName("DELETE REVIEW")
    fun deleteReview() {
        val response = mockMvc.delete("$baseurl/reviews/$review_id") {
            header("Authorization", jwt_token)
        }
            .andDo {
                print()
            }
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString
    }
}

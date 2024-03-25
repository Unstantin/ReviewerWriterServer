package com.reviewerwriter.entities

import com.google.gson.annotations.SerializedName
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

@Entity
@Table(name="logs")
class LogEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    var id: Int? = null,

    @Column(name = "request_date_time")
    var requestDateTime: LocalDateTime? = null,

    @Column(name = "response_date_time")
    var responseDateTime: LocalDateTime? = null,

    @Column(name = "username")
    var username: String? = null,

    @Column(name = "method")
    var method: String? = null,

    @Column(name = "endpoint")
    var endpoint: String? = null,

    @Column(name = "response_code")
    var statusCode: Int? = null,

    @SerializedName("request_body")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var requestBody: Any? = null,

    @SerializedName("response_body")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    var responseBody: Any? = null
)
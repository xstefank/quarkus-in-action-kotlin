package org.acme.reservation

import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.common.http.TestHTTPResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.acme.reservation.reservation.Reservation
import org.acme.reservation.rest.ReservationResource
import org.hamcrest.Matchers.notNullValue
import org.junit.jupiter.api.Test
import java.net.URL
import java.time.LocalDate

@QuarkusTest
class ReservationResourceTest {
    @TestHTTPEndpoint(ReservationResource::class)
    @TestHTTPResource
    var reservationResource: URL? = null

    @Test
    fun testReservationIds() {
        val reservation = Reservation()
        reservation.carId = 12345L
        reservation.startDay = LocalDate.parse("2025-03-20")
        reservation.endDay = LocalDate.parse("2025-03-29")
        RestAssured
            .given()
            .contentType(ContentType.JSON)
            .body(reservation)
            .`when`()
            .post(reservationResource)
            .then()
            .statusCode(200)
            .body("id", notNullValue())
    }
}
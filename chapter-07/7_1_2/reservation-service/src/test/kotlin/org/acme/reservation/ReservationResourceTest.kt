package org.acme.reservation

import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.common.http.TestHTTPResource
import io.quarkus.test.junit.DisabledOnIntegrationTest
import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.acme.reservation.inventory.Car
import org.acme.reservation.inventory.GraphQLInventoryClient
import org.acme.reservation.entity.Reservation
import org.acme.reservation.rest.ReservationResource
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.net.URL
import java.time.LocalDate


@QuarkusTest
class ReservationResourceTest {
    @TestHTTPEndpoint(ReservationResource::class)
    @TestHTTPResource
    var reservationResource: URL? = null

    @TestHTTPEndpoint(ReservationResource::class)
    @TestHTTPResource("availability")
    var availability: URL? = null

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

    @DisabledOnIntegrationTest(forArtifactTypes = [DisabledOnIntegrationTest.ArtifactType.NATIVE_BINARY])
    @Test
    fun testMakingAReservationAndCheckAvailability() {
        val mock: GraphQLInventoryClient = Mockito.mock(GraphQLInventoryClient::class.java)
        val peugeot = Car(1L, "ABC123", "Peugeot", "406")
        Mockito.`when`(mock.allCars()).thenReturn(listOf(peugeot))
        QuarkusMock.installMockForType(mock, GraphQLInventoryClient::class.java)
        val startDate = "2022-01-01"
        val endDate = "2022-01-10"

        // List available cars for our requested timeslot and choose one
        val cars = RestAssured.given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .`when`()[availability]
            .then()
            .statusCode(200)
            .extract().`as`(Array<Car>::class.java)

        val car = cars[0]

        // Prepare a Reservation object
        val reservation = Reservation()
        reservation.carId = car.id
        reservation.startDay = LocalDate.parse(startDate)
        reservation.endDay = LocalDate.parse(endDate)

        // Submit the reservation
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(reservation)
            .`when`().post(reservationResource)
            .then().statusCode(200)
            .body("carId", `is`(car.id.toInt()))

        // Verify that this car doesn't show as available anymore
        RestAssured.given()
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .`when`()[availability]
            .then().statusCode(200)
            .body("findAll { car -> car.id == " + car.id + "}", hasSize<Any>(0))
    }
}
package org.acme.reservation

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.acme.reservation.entity.Reservation
import org.acme.reservation.reservation.ReservationsRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@QuarkusTest
class ReservationRepositoryTest {
    @Inject
    lateinit var repository: ReservationsRepository

    @Test
    fun testCreateReservation() {
        val reservation = Reservation()
        reservation.startDay = LocalDate.now().plus(5, ChronoUnit.DAYS)
        reservation.endDay = LocalDate.now().plus(12, ChronoUnit.DAYS)
        reservation.carId = 384L
        repository.save(reservation)
        Assertions.assertNotNull(reservation.id)
        Assertions.assertTrue(repository.findAll().contains(reservation))
    }
}
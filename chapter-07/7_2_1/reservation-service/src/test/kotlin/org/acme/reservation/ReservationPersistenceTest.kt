package org.acme.reservation

import io.quarkus.test.junit.QuarkusTest
import jakarta.transaction.Transactional
import org.acme.reservation.entity.Reservation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@QuarkusTest
class ReservationPersistenceTest {

    @Test
    @Transactional
    fun testCreateReservation() {
        val reservation = Reservation()
        reservation.startDay = LocalDate.now().plus(5, ChronoUnit.DAYS)
        reservation.endDay = LocalDate.now().plus(12, ChronoUnit.DAYS)
        reservation.carId = 384L
        reservation.persist()
        Assertions.assertNotNull(reservation.id)
        Assertions.assertEquals(1, Reservation.count())
        val persistedReservation = Reservation.findById(reservation.id!!)
        Assertions.assertNotNull(persistedReservation)
        Assertions.assertEquals(reservation.carId, persistedReservation!!.carId)
    }
}
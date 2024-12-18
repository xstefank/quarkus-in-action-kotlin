package org.acme.reservation.reservation

import org.acme.reservation.entity.Reservation

interface ReservationsRepository {
    fun findAll(): List<Reservation>
    fun save(reservation: Reservation): Reservation
}
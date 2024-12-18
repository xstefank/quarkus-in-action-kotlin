package org.acme.reservation.reservation

interface ReservationsRepository {
    fun findAll(): List<Reservation>
    fun save(reservation: Reservation): Reservation
}
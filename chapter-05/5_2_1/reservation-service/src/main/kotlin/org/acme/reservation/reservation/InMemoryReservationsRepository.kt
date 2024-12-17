package org.acme.reservation.reservation

import jakarta.inject.Singleton
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

@Singleton
class InMemoryReservationsRepository : ReservationsRepository {
    private val ids = AtomicLong(0)
    private val store: MutableList<Reservation> = CopyOnWriteArrayList()

    override fun findAll(): List<Reservation> {
        return Collections.unmodifiableList(store)
    }

    override fun save(reservation: Reservation): Reservation {
        reservation.id = ids.incrementAndGet()
        store.add(reservation)
        return reservation
    }
}
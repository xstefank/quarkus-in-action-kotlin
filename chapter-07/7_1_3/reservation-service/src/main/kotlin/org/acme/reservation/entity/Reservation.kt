package org.acme.reservation.entity

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import java.time.LocalDate

@Entity
class Reservation(var carId: Long? = null, var userId: String? = null, var startDay: LocalDate? = null, var endDay: LocalDate? = null) : PanacheEntity() {

    companion object: PanacheCompanion<Reservation> {}

    /**
     * Check if the given duration overlaps with this reservation
     * @return true if the dates overlap with the reservation, false
     * otherwise
     */
    fun isReserved(startDay: LocalDate?, endDay: LocalDate?): Boolean {
        return (!(this.endDay!!.isBefore(startDay) || this.startDay!!.isAfter(endDay)))
    }

    override fun toString(): String {
        return ("Reservation{id=$id").toString() + ", carId=" + carId + ", userId='" + userId + '\'' + ", startDay=" + startDay + ", endDay=" + endDay + '}'
    }

}
package org.acme.reservation.reservation

import java.time.LocalDate

class Reservation(var id: Long? = null, var carId: Long? = null, var startDay: LocalDate? = null, var endDay: LocalDate? = null) {

    /**
     * Check if the given duration overlaps with this reservation
     * @return true if the dates overlap with the reservation, false
     * otherwise
     */
    fun isReserved(startDay: LocalDate?, endDay: LocalDate?): Boolean {
        return (!(this.endDay!!.isBefore(startDay) || this.startDay!!.isAfter(endDay)))
    }
}
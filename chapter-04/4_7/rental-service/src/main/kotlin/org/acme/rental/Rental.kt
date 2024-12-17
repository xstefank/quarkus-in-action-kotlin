package org.acme.rental

import java.time.LocalDate

class Rental(val id: Long, val userId: String, val reservationId: Long, val startDate: LocalDate) {
    override fun toString(): String {
        return "Rental{id=$id, userId='$userId', reservationId=$reservationId, startDate=$startDate}"
    }
}
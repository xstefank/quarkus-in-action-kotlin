package org.acme.users.model

import java.time.LocalDate

class Reservation(var id: Long? = null, var userId: String? = null, var carId: Long? = null, var startDay: LocalDate? = null, var endDay: LocalDate? = null) {
}
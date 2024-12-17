package org.acme.rental

import io.quarkus.logging.Log
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicLong

@Path("/rental")
class RentalResource {
    private val id = AtomicLong(0)

    @Path("/start/{userId}/{reservationId}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun start(userId: String, reservationId: Long): Rental {
        Log.infof("Starting rental for %s with reservation %s", userId, reservationId)
        return Rental(id.incrementAndGet(), userId, reservationId, LocalDate.now())
    }
}
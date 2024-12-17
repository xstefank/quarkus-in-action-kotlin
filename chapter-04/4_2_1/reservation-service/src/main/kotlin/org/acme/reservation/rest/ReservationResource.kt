package org.acme.reservation.rest

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.acme.reservation.inventory.Car
import org.acme.reservation.inventory.InventoryClient
import org.acme.reservation.reservation.ReservationsRepository
import org.jboss.resteasy.reactive.RestQuery
import java.time.LocalDate

@Path("reservation")
@Produces(MediaType.APPLICATION_JSON)
class ReservationResource(private val reservationsRepository: ReservationsRepository, private val inventoryClient: InventoryClient) {
    @GET
    @Path("availability")
    fun availability(@RestQuery startDate: LocalDate?, @RestQuery endDate: LocalDate?): Collection<Car> {
        // obtain all cars from inventory
        val availableCars = inventoryClient.allCars()
        // create a map from id to car
        val carsById: MutableMap<Long?, Car> = HashMap()
        for (car in availableCars!!) {
            carsById[car!!.id] = car
        }

        // get all current reservations
        val reservations = reservationsRepository.findAll()
        // for each reservation, remove the car from the map
        for (reservation in reservations) {
            if (reservation.isReserved(startDate, endDate)) {
                carsById.remove(reservation.carId)
            }
        }
        return carsById.values
    }
}
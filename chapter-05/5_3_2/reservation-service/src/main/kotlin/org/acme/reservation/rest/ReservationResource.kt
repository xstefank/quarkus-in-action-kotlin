package org.acme.reservation.rest

import io.quarkus.logging.Log
import io.smallrye.graphql.client.GraphQLClient
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.acme.reservation.inventory.Car
import org.acme.reservation.inventory.GraphQLInventoryClient
import org.acme.reservation.rental.RentalClient
import org.acme.reservation.reservation.Reservation
import org.acme.reservation.reservation.ReservationsRepository
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.resteasy.reactive.RestQuery
import java.time.LocalDate

@Path("reservation")
@Produces(MediaType.APPLICATION_JSON)
class ReservationResource(
    private val reservationsRepository: ReservationsRepository,
    @GraphQLClient("inventory") private val inventoryClient: GraphQLInventoryClient,
    @RestClient private val rentalClient: RentalClient
) {
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    fun make(reservation: Reservation): Reservation {
        val result = reservationsRepository.save(reservation)
        // this is just a dummy value for the time being
        val userId = "x"
        if (reservation.startDay == LocalDate.now()) {
            val rental = rentalClient.start(userId, result.id)
            Log.info("Successfully started rental $rental")
        }
        return result
    }

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
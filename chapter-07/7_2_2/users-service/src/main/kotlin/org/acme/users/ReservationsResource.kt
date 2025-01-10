package org.acme.users

import io.quarkus.qute.CheckedTemplate
import io.quarkus.qute.TemplateInstance
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import org.acme.users.model.Car
import org.acme.users.model.Reservation
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.RestQuery
import org.jboss.resteasy.reactive.RestResponse
import java.time.LocalDate

@Path("/")
class ReservationsResource(
    val securityContext: SecurityContext,
    @RestClient val client: ReservationsClient
) {
    @CheckedTemplate
    object Templates {
        @JvmStatic
        external fun index(startDate: LocalDate?, endDate: LocalDate?, name: String?): TemplateInstance?

        @JvmStatic
        external fun listofreservations(reservations: Collection<Reservation?>?): TemplateInstance?

        @JvmStatic
        external fun availablecars(cars: Collection<Car?>?, startDate: LocalDate?, endDate: LocalDate?): TemplateInstance?
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun index(@RestQuery startDate: LocalDate?, @RestQuery endDate: LocalDate?): TemplateInstance? {
        var startDate = startDate
        var endDate = endDate
        if (startDate == null) {
            startDate = LocalDate.now().plusDays(1L)
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusDays(7)
        }
        return Templates.index(startDate, endDate, securityContext.userPrincipal.name)
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/get")
    fun getReservations(): TemplateInstance? {
        val reservationCollection = client.allReservations()
        return Templates.listofreservations(reservationCollection)
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/available")
    fun getAvailableCars(@RestQuery startDate: LocalDate?, @RestQuery endDate: LocalDate?): TemplateInstance? {
        val availableCars = client.availability(startDate, endDate)
        return Templates.availablecars(availableCars, startDate, endDate)
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Path("/reserve")
    fun create(@RestForm startDate: LocalDate?, @RestForm endDate: LocalDate?, @RestForm carId: Long?): RestResponse<TemplateInstance?> {
        val reservation = Reservation()
        reservation.startDay = startDate!!
        reservation.endDay = endDate!!
        reservation.carId = carId!!
        client.make(reservation)
        return RestResponse.ResponseBuilder
            .ok(getReservations())
            .header("HX-Trigger-After-Swap",
                "update-available-cars-list")
            .build()
    }
}
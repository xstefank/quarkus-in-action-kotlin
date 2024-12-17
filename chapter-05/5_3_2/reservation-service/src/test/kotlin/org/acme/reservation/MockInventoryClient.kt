package org.acme.reservation

import io.quarkus.test.Mock
import org.acme.reservation.inventory.Car
import org.acme.reservation.inventory.GraphQLInventoryClient

@Mock
class MockInventoryClient : GraphQLInventoryClient {
    override fun allCars(): List<Car> {
        val peugeot = Car(1L, "ABC123", "Peugeot", "406")
        return listOf(peugeot)
    }
}
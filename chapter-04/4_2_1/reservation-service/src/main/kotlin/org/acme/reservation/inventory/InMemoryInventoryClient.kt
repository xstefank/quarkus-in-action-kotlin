package org.acme.reservation.inventory

import jakarta.inject.Singleton

@Singleton
class InMemoryInventoryClient : InventoryClient {
    override fun allCars(): List<Car?> {
        return ALL_CARS
    }

    companion object {
        private val ALL_CARS: List<Car?> = listOf(
            Car(1L, "ABC-123", "Toyota", "Corolla"),
            Car(2L, "ABC-987", "Honda", "Jazz"),
            Car(3L, "XYZ-123", "Renault", "Clio"),
            Car(4L, "XYZ-987", "Ford", "Focus"))
    }
}
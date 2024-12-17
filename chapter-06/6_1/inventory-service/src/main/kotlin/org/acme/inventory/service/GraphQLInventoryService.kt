package org.acme.inventory.service

import jakarta.inject.Inject
import org.acme.inventory.database.CarInventory
import org.acme.inventory.model.Car
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

@GraphQLApi
class GraphQLInventoryService(@Inject var inventory: CarInventory) {

    @Query
    fun cars(): List<Car>? {
        return inventory.getCars()
    }

    @Mutation
    fun register(car: Car): Car {
        car.id = CarInventory.ids.incrementAndGet()
        inventory.getCars()!!.add(car)
        return car
    }

    @Mutation
    fun remove(licensePlateNumber: String): Boolean {
        val cars = inventory.getCars()
        val toBeRemoved = cars!!.stream()
            .filter { car: Car -> car.licensePlateNumber == licensePlateNumber }.findAny()
        return if (toBeRemoved.isPresent) {
            cars.remove(toBeRemoved.get())
        } else {
            false
        }
    }
}
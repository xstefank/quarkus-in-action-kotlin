package org.acme.inventory.database

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.acme.inventory.model.Car
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

@ApplicationScoped
class CarInventory {
    private var cars: MutableList<Car>? = null

    @PostConstruct
    fun initialize() {
        cars = CopyOnWriteArrayList()
        initialData()
    }

    fun getCars(): MutableList<Car>? {
        return cars
    }

    private fun initialData() {
        val mazda = Car()
        mazda.id = ids.incrementAndGet()
        mazda.manufacturer = "Mazda"
        mazda.model = "6"
        mazda.licensePlateNumber = "ABC123"
        cars!!.add(mazda)

        val ford = Car()
        ford.id = ids.incrementAndGet()
        ford.manufacturer = "Ford"
        ford.model = "Mustang"
        ford.licensePlateNumber = "XYZ987"
        cars!!.add(ford)
    }

    companion object {
        val ids: AtomicLong = AtomicLong(0)
    }
}
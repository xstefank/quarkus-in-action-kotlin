package org.acme.reservation.inventory

interface InventoryClient {
    fun allCars(): List<Car?>?
}
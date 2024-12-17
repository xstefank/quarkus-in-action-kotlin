package org.acme.inventory.grpc

import io.quarkus.grpc.GrpcService
import io.quarkus.logging.Log
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import org.acme.inventory.database.CarInventory
import org.acme.inventory.model.*

@GrpcService
class GrpcInventoryService(@Inject var inventory: CarInventory) : InventoryService {

    override fun add(requests: Multi<InsertCarRequest>): Multi<CarResponse> {
        return requests.map { request: InsertCarRequest ->
            val car = Car()
            car.licensePlateNumber = request.licensePlateNumber
            car.manufacturer = request.manufacturer
            car.model = request.model
            car.id = CarInventory.ids.incrementAndGet()
            car
        }.onItem().invoke { car: Car ->
            Log.info("Persisting $car")
            inventory.getCars()!!.add(car)
        }.map { car: Car ->
            CarResponse.newBuilder()
                .setLicensePlateNumber(car.licensePlateNumber)
                .setManufacturer(car.manufacturer)
                .setModel(car.model)
                .setId(car.id!!)
                .build()
        }
    }

    override fun remove(request: RemoveCarRequest): Uni<CarResponse> {
        val optionalCar = inventory.getCars()!!.stream()
            .filter { car: Car ->
                (request.licensePlateNumber
                        == car.licensePlateNumber)
            }
            .findFirst()
        if (optionalCar.isPresent) {
            val removedCar = optionalCar.get()
            inventory.getCars()!!.remove(removedCar)
            return Uni.createFrom().item(
                CarResponse.newBuilder()
                    .setLicensePlateNumber(removedCar.licensePlateNumber)
                    .setManufacturer(removedCar.manufacturer)
                    .setModel(removedCar.model)
                    .setId(removedCar.id!!).build()
            )
        }
        return Uni.createFrom().nullItem()
    }
}
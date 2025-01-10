package org.acme.inventory.grpc

import io.quarkus.grpc.GrpcService
import io.quarkus.logging.Log
import io.quarkus.narayana.jta.QuarkusTransaction
import io.smallrye.common.annotation.Blocking
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.acme.inventory.model.*
import org.acme.inventory.repository.CarRepository

@GrpcService
class GrpcInventoryService(@Inject var carRepository: CarRepository) : InventoryService {

    @Blocking
    override fun add(requests: Multi<InsertCarRequest>): Multi<CarResponse> {
        return requests.map { request: InsertCarRequest ->
            val car = Car()
            car.licensePlateNumber = request.licensePlateNumber
            car.manufacturer = request.manufacturer
            car.model = request.model
            car
        }.onItem().invoke { car: Car ->
            QuarkusTransaction.requiringNew().run {
                carRepository.persist(car)
                Log.info("Persisting $car")
            }
        }.map { car: Car -> CarResponse.newBuilder()
            .setLicensePlateNumber(car.licensePlateNumber)
            .setManufacturer(car.manufacturer)
            .setModel(car.model)
            .setId(car.id!!)
            .build()
        }
    }

    @Blocking
    @Transactional
    override fun remove(request: RemoveCarRequest): Uni<CarResponse> {
        val optionalCar = carRepository.findByLicensePlateNumberOptional(request.licensePlateNumber)
        return optionalCar?.let {
            carRepository.delete(optionalCar)
            Uni.createFrom().item(CarResponse.newBuilder()
                .setLicensePlateNumber(optionalCar.licensePlateNumber)
                .setManufacturer(optionalCar.manufacturer)
                .setModel(optionalCar.model)
                .setId(optionalCar.id!!)
                .build())
        } ?: Uni.createFrom().nullItem()
    }
}
package org.acme.inventory.repository

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import org.acme.inventory.model.Car


@ApplicationScoped
class CarRepository : PanacheRepository<Car> {

    fun findByLicensePlateNumberOptional(licensePlateNumber: String): Car? {
        return find("licensePlateNumber", licensePlateNumber).firstResult()
    }
}
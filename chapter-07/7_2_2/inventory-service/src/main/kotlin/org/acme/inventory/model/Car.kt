package org.acme.inventory.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*

@Entity
class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var licensePlateNumber: String? = null
    var manufacturer: String? = null
    var model: String? = null

    override fun toString(): String {
        return "Car{" +
                "id=" + id +
                ", licensePlateNumber='" + licensePlateNumber + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                '}'
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Car) return false
        return id == other.id && licensePlateNumber == other.licensePlateNumber && manufacturer == other.manufacturer && model == other.model
    }

    override fun hashCode(): Int {
        return Objects.hash(id, licensePlateNumber, manufacturer, model)
    }
}
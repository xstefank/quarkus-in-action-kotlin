package org.acme.inventory.client

import io.quarkus.grpc.GrpcClient
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import org.acme.inventory.model.CarResponse
import org.acme.inventory.model.InsertCarRequest
import org.acme.inventory.model.InventoryService
import org.acme.inventory.model.RemoveCarRequest

@QuarkusMain
class InventoryCommand(
    @GrpcClient("inventory")
    var inventory: InventoryService
) : QuarkusApplication {

    override fun run(vararg args: String): Int {
        val action = if (args.size > 0) args[0] else null
        if ("add" == action && args.size >= 4) {
            add(args[1], args[2], args[3])
            return 0
        } else if ("remove" == action && args.size >= 2) {
            remove(args[1])
            return 0
        }
        System.err.println(USAGE)
        return 1
    }

    fun add(licensePlateNumber: String?, manufacturer: String?, model: String?) {
        inventory!!.add(
            InsertCarRequest.newBuilder()
                .setLicensePlateNumber(licensePlateNumber)
                .setManufacturer(manufacturer)
                .setModel(model)
                .build()
        )
            .onItem().invoke { carResponse: CarResponse -> println("Inserted new car $carResponse") }
            .await().indefinitely()
    }

    fun remove(licensePlateNumber: String?) {
        inventory!!.remove(
            RemoveCarRequest.newBuilder()
                .setLicensePlateNumber(licensePlateNumber)
                .build()
        )
            .onItem().invoke { carResponse: CarResponse -> println("Removed car $carResponse") }
            .await().indefinitely()
    }

    companion object {
        private const val USAGE = "Usage: inventory <add>|<remove> " + "<license plate number> <manufacturer> <model>"
    }
}
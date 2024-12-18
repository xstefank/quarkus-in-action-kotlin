package org.acme.reservation.inventory;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi
import org.eclipse.microprofile.graphql.Query

@GraphQLClientApi(configKey = "inventory")
interface GraphQLInventoryClient : InventoryClient {
    @Query("cars")
    override fun allCars(): List<Car?>?
}
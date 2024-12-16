package org.acme

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext
import org.eclipse.microprofile.config.inject.ConfigProperty


@Path("/hello")
class GreetingResource(@ConfigProperty(name = "greeting") val greeting: String) {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun hello() = greeting

    @GET
    @Path("/whoami")
    @Produces(MediaType.TEXT_PLAIN)
    fun whoAmI(@Context securityContext: SecurityContext): String {
        val userPrincipal = securityContext.userPrincipal
        return userPrincipal?.let { userPrincipal.name } ?: "anonymous"
    }
}
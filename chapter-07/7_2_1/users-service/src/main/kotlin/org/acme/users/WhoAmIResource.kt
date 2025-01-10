package org.acme.users

import io.quarkus.qute.Template
import io.quarkus.qute.TemplateInstance
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.SecurityContext

@Path("/whoami")
class WhoAmIResource(@Inject var whoami: Template, @Inject var securityContext: SecurityContext) {

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun get(): TemplateInstance {
        val userId = if (securityContext.userPrincipal != null) securityContext.userPrincipal.name else null
        return whoami.data("name", userId)
    }
}
package org.jboss.resteasy.client.microprofile;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.internal.ClientConfiguration;
import org.jboss.resteasy.client.jaxrs.internal.ClientInvocationBuilder;
import org.jboss.resteasy.client.jaxrs.internal.ClientWebTarget;

/**
 * An extension of ClientWebTarget for implementing MP REST Client
 * 
 * @author <a href="mailto:alessio.soldano@jboss.com">Alessio Soldano</a>
 *
 */
public class MPClientWebTarget extends ClientWebTarget
{

   protected MPClientWebTarget(ResteasyClient client, ClientConfiguration configuration)
   {
      super(client, configuration);
   }
   
   public MPClientWebTarget(ResteasyClient client, String uri, ClientConfiguration configuration) throws IllegalArgumentException, NullPointerException
   {
      super(client, uri, configuration);
   }

   public MPClientWebTarget(ResteasyClient client, URI uri, ClientConfiguration configuration) throws NullPointerException
   {
      super(client, uri, configuration);
   }

   public MPClientWebTarget(ResteasyClient client, UriBuilder uriBuilder, ClientConfiguration configuration) throws NullPointerException
   {
      super(client, uriBuilder, configuration);
   }

   @Override
   protected ClientWebTarget newInstance(ResteasyClient client, UriBuilder uriBuilder, ClientConfiguration configuration)
   {
      return new MPClientWebTarget(client, uriBuilder, configuration);
   }

   @Override
   protected ClientInvocationBuilder createClientInvocationBuilder(ResteasyClient client, URI uri, ClientConfiguration configuration)
   {
      return new MPClientInvocationBuilder(client, uri, configuration);
   }
   
}

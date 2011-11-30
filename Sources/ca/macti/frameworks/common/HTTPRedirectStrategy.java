package ca.macti.frameworks.common;

import java.net.URI;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.HttpContext;

public class HTTPRedirectStrategy extends DefaultRedirectStrategy {

  private URI finalHost;

  public HTTPRedirectStrategy()  {
  }

  public URI finalHost() {
    return finalHost;
  }

  @Override
  public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
    HttpUriRequest redirectUri = super.getRedirect(request, response, context);
    finalHost = redirectUri.getURI();
    return redirectUri;
  }

  @Override
  public boolean isRedirected(HttpRequest arg0, HttpResponse arg1, HttpContext arg2) throws ProtocolException {
    boolean isRedirected = super.isRedirected(arg0, arg1, arg2);
    if (!isRedirected) {
      finalHost = ((HttpGet)((RequestWrapper)arg0).getOriginal()).getURI();
    }
    return isRedirected;
  }

}

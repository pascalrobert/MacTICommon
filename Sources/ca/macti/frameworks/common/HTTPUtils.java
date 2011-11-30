package ca.macti.frameworks.common;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;

public class HTTPUtils {

  /**
   * This static method will tell you if a Web site is valid (host is available 
   * and returns a valid status code).
   * @param url : URL to check
   * @param handleRedirects : if you want the check to follow redirects (e.g., 
   * if the URL returns a 301, 302 or 303 code, it should try the linked URL).
   * @return true if he host is available and returns a status code < 400 (if handleRedirects 
   * is false) or if the status code < 500 (if handleRedirects is true), false otherwise.
   */
  public static boolean isValidWebSite(String url, boolean handleRedirects) {
    HttpClient httpclient = new DefaultHttpClient();
    httpclient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, handleRedirects);
    try {
      HttpGet httpget = new HttpGet(url);

      HttpResponse response = httpclient.execute(httpget);
      int statusCode = response.getStatusLine().getStatusCode();
      if (handleRedirects) {
        if (statusCode >= 200 && statusCode < 300) {
          return true;
        }
      } else {
        if (statusCode >= 200 && statusCode < 400) {
          return true;
        }
      }
      
    } catch (ClientProtocolException e) {
      return false;
    } catch (IOException e) {
      return false;
    } finally {
      httpclient.getConnectionManager().shutdown();
    }
    return false;
  }
  
  /**
   * This static method will follow all redirections (HTTP status codes 301/302/303) and find the final 
   * URL (e.g. the value of the Location header if it's a redirect) of the requested URL.
   * @param url
   * @param handleRedirects
   */
  public static String findFinalUrl(String url) {
    DefaultHttpClient httpclient = new DefaultHttpClient();
    httpclient.setRedirectStrategy(new HTTPRedirectStrategy());
    try {
    
      HttpGet httpget = new HttpGet(url);
      httpclient.execute(httpget);
      return ((HTTPRedirectStrategy)httpclient.getRedirectStrategy()).finalHost().toASCIIString();

    } catch (ClientProtocolException e) {
      return null;
    } catch (IOException e) {
      return null;
    } finally {
      httpclient.getConnectionManager().shutdown();
    }
  }

}

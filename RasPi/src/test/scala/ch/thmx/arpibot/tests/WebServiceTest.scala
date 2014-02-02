package ch.thmx.arpibot.tests

import org.scalatest._

import javax.jws.{WebMethod, WebService}
import javax.xml.ws.Endpoint
import javax.xml.ws.Service

@WebService
trait SoapTrait {
  @WebMethod
  def test(value: String): String
}

@WebService(endpointInterface = "ch.thmx.arpibot.tests.SoapTrait")
private class MinimalSoapServer extends SoapTrait {

  def test(value: String) = "Hi " + value

}

import java.net.URL
import javax.xml.namespace.QName

class WebServiceTest extends FlatSpec {

  it should "launch the WebService and reach it" in {

    val wsURL = "http://localhost:8080/wstest"

    val endpoint = Endpoint.publish(wsURL, new MinimalSoapServer())
    System.out.println("WebService launched... Waiting for requests...")

    val url = new URL(wsURL + "?wsdl")
    val qname = new QName("http://tests.arpibot.thmx.ch/", "MinimalSoapServerService")

    val service = Service.create(url, qname)

    val port = service.getPort(classOf[SoapTrait])

    println(port.test("salut"))

  }

}
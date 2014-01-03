package ch.thmx.raspbot.tests

import org.scalatest._

import com.pi4j.io.i2c._

class I2CTest extends FlatSpec {

	it should "send and receive back the data" in {
		val bus = I2CFactory.getInstance(I2CBus.BUS_1)
		val device = bus.getDevice(0x78)

		for (write <- 0 to 255) {
			device.write(write.toByte)
			print("Wrote " + write)
			val read = device.read()
			println(" - Read " + read)
			assert(write == read)
		}
	}

}

package ch.thmx.arpibot.tests

import org.scalatest._

import cwiid._
import cwiid.CwiidLibrary._

import com.sun.jna.Pointer

import ch.thmx.cwiid.CWiid._

class CWiidTest extends FlatSpec {

	it should "connect and switch on the LEDs" in {

		val errorCallback = new cwiid_err_t() {
			def apply(wm: Pointer, str: Pointer, ap: va_list): Unit = {
				println("error callback")
			}
		}

		val callback = new cwiid_mesg_callback_t {
			def apply(wm: Pointer, msg_count: Int, mesg: Pointer, timestamp: timespec): Unit = {
				println("callback")
			}
		}

		val addr = "00:00:00:00:00:00" // Any Bluetooth devices

		val bdaddr: bdaddr_t = new bdaddr_t(hstr2barr(addr))

		val cwiid = CwiidLibrary.INSTANCE

		println("Press button 1 + 2...")
		Thread.sleep(3000l)

		import cwiid._

		cwiid_set_err(errorCallback)

		println("Trying to connect to " + addr)
		val wiimote = cwiid_open(bdaddr, 0)
		if (wiimote == null) println("\tError while trying to connect.")
		else {
			println("\tSuccessfuly connected.")

			println("Assigning callback...")
			cwiid_set_mesg_callback(wiimote, callback)

			cwiid_enable(wiimote, CWIID_FLAG_MESG_IFC)
			cwiid_set_rpt_mode(wiimote, CWIID_RPT_BTN.toByte)

			println("Trying to switch on LEDs")
			val led = cwiid_set_led(wiimote, 0xAA.toByte)
			if (led == 0) println("\tSccessfuly switched the LEDs");
			else println("\tError setting LEDs with error code " + led);

			Thread.sleep(5000l)

			println("Trying to disconnect from " + addr)
			val error = cwiid_close(wiimote)
			if (error == 0) println("\tSuccessfuly disconnected.")
			else println("\tError while disconnecting with error code " + error)
		}
	}
}

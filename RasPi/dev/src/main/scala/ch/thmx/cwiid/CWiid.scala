package ch.thmx.cwiid

object CWiid {
	def hstr2barr(addr: String) = addr.split(":").map(Integer.valueOf(_, 16).toByte).reverse.toArray
}

class CWiid {

}

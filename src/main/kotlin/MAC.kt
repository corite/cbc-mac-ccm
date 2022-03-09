import java.math.BigInteger

@OptIn(ExperimentalUnsignedTypes::class)
class MAC {

    fun cbcMac(message: UByteArray, key: UByteArray): UByteArray {
        val encHandler = EncryptionHandler()

        val cbcEncryptedMessage = encHandler.encrypt(message, key, CipherMode.CBC)
        return cbcEncryptedMessage.copyOfRange(cbcEncryptedMessage.size - 16, cbcEncryptedMessage.size)
        //returning the last block of the cbc encrypted message
    }

    fun cbcMacCcm(x: UByteArray, key: UByteArray, nonce: ULong): UByteArray {
        val encHandler = EncryptionHandler()
        val y = encHandler.cryptCTR(x, key, nonce)
        val tmp = cbcMac(x, key)
        val tZero = BigInteger(getCtr(nonce).toString()).to16UByteArray()
        val z = tmp xor tZero
        return y + z
    }

    fun verifyCbcMac(message:UByteArray, key:UByteArray, hash:UByteArray):Boolean {
        return cbcMac(message, key).contentEquals(hash)
    }

    fun verifyCbcMacCcm(y: UByteArray, key: UByteArray, nonce: ULong):Boolean {
        val encHandler = EncryptionHandler()
        val yZeroToN = y.copyOfRange(0, y.size-16)
        val z = y.copyOfRange(y.size-16, y.size)
        val x = encHandler.cryptCTR(yZeroToN, key, nonce)
        val tZero = BigInteger(getCtr(nonce).toString()).to16UByteArray()

        return z.contentEquals(tZero xor cbcMac(x,key))
    }

    private fun getCtr(nonce:ULong):ULong {
        val leadingZeros = nonce.countLeadingZeroBits()
        return  nonce shl leadingZeros
        //shifts the number so that the first bit is always 1 (as long as the number doesn't equal 0)
    }

    private fun BigInteger.to16UByteArray():UByteArray {
        val filledBytes = this.toByteArray().toUByteArray()
        return UByteArray(16-filledBytes.size)+filledBytes
    }

    private infix fun UByteArray.xor(other:UByteArray):UByteArray {
        if (this.size != other.size) throw IllegalArgumentException("arrays must have the same length")

        return this.mapIndexed { i: Int, uByte: UByte -> uByte xor other[i] }.toUByteArray()
    }
}
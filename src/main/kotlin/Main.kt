@file:OptIn(ExperimentalUnsignedTypes::class)

import kotlin.random.Random
import kotlin.random.nextULong

fun main() {
    prettyPrintCbcMac()
    prettyPrintCbcMacCcm()
}

private fun prettyPrintCbcMac() {
    println(":".repeat(16)+" CBC-MAC "+":".repeat(16))
    val mac = MAC()
    val m = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    println("m: $m")
    val mAsBytes = m.encodeToByteArray().asUByteArray()
    println("mAsBytes: ${mAsBytes.hexStr()}")
    val key = getKey()
    val hash = mac.cbcMac(mAsBytes, key)
    println("hash: ${hash.hexStr()}")
    println("isValid: ${mac.verifyCbcMac(mAsBytes, key, hash)}")
}

private fun prettyPrintCbcMacCcm() {
    println(":".repeat(16)+" CBC-MAC-CCM "+":".repeat(16))

    val mac = MAC()
    val random = Random.Default
    val m = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    println("m: $m")
    val mAsBytes = m.encodeToByteArray().asUByteArray()
    println("mAsBytes: ${mAsBytes.hexStr()}")
    val key = getKey()
    val nonce = random.nextULong()
    val hash = mac.cbcMacCcm(mAsBytes, key, nonce)
    println("hash: ${hash.hexStr()}")
    println("isValid: ${mac.verifyCbcMacCcm(hash, key, nonce)}")
}

private fun getKey():UByteArray {
    return ubyteArrayOf(1u,2u,3u,4u,5u,6u,7u,8u,9u,10u,11u,12u,13u,14u,15u,16u)

}

private fun UByteArray.hexStr():String {
    return this.joinToString(", ") { it.toString(16) }
}
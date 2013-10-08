/*
   Copyright 2013 Technical University of Denmark, DTU Compute. 
   All rights reserved.
   
   This file is part of the time-predictable VLIW processor Patmos.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are met:

      1. Redistributions of source code must retain the above copyright notice,
         this list of conditions and the following disclaimer.

      2. Redistributions in binary form must reproduce the above copyright
         notice, this list of conditions and the following disclaimer in the
         documentation and/or other materials provided with the distribution.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ``AS IS'' AND ANY EXPRESS
   OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
   OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
   NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
   THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

   The views and conclusions contained in the software and documentation are
   those of the authors and should not be interpreted as representing official
   policies, either expressed or implied, of the copyright holder.
 */

/*
 * Utility functions for Patmos.
 * 
 * Author: Martin Schoeberl (martin@jopdesign.com)
 * 
 */

package patmos

import Chisel._
import Node._

import Constants._

object Utility {

  /**
   * Read a binary file into the ROM vector
   */
  def readBin(fileName: String, width: Int): Vec[Bits] = {

    val bytesPerWord = (width+7) / 8

    println("Reading " + fileName)
    // an encoding to read a binary file? Strange new world.
    val source = scala.io.Source.fromFile(fileName)(scala.io.Codec.ISO8859)
    val byteArray = source.map(_.toByte).toArray
    source.close()

    // using a vector for a ROM
    val v = Vec(math.max(1, byteArray.length / bytesPerWord)) { Bits(width = width) }

    if (byteArray.length == 0) {
      v(0) = Bits(0, width = width)
    }

    for (i <- 0 until byteArray.length / bytesPerWord) {
      var word = 0
      for (j <- 0 until bytesPerWord) {
        word <<= 8
        word += byteArray(i * bytesPerWord + j).toInt & 0xff
      }
      printf("%08x\n", word)
      v(i) = Bits(word, width = width)
    }
    v
  }
  
  def printConfig(): Unit = {
    printf("\nPatmos configuration:\n")
    printf("\tFrequency: %d MHz\n", Constants.CLOCK_FREQ/1000000)
    printf("\tMethod cache size: %d KB\n", Constants.MCACHE_SIZE/1024)
    printf("\tInstruction SPM  size: %d KB\n", (1 << Constants.ISPM_BITS)/1024)
    printf("\tData SPM size: %d KB\n", (1 << Constants.DSPM_BITS)/1024)
    printf("\tBoot SPM size: %d KB\n", (1 << Constants.BOOTSPM_BITS)/1024)
    printf("\n")
  }
}
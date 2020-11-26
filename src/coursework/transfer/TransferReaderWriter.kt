package coursework.transfer

import java.io.File

class TransferReaderWriter {

     fun writeTransfer(transferString: String) {
        // write the string to the transfer file
        File("src/coursework/transfer/data transfer.txt").writeText(transferString)
    }

    fun readTransfer(): String {
        // read the string to the transfer file
        var returnLine = ""
        File("src/coursework/transfer/data transfer.txt").useLines { lines -> lines.forEach { line -> returnLine = line} }
        return returnLine
    }


}
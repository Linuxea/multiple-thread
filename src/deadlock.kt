import java.util.concurrent.TimeUnit

class ReadAndWrite {

    private val readLock = Object()
    private val writeLock = Object()

    fun read() {
        // 持有读锁
        synchronized(readLock) {
            println("我写写写写写....")
            TimeUnit.SECONDS.sleep(3)
            synchronized(writeLock) {
                println("我读读读读读")
                TimeUnit.SECONDS.sleep(3)
            }
        }
    }

    fun write() {
        // 持有读锁
        synchronized(writeLock) {
            println("我读读读读读....")
            TimeUnit.SECONDS.sleep(3)
            synchronized(readLock) {
                println("我写写写写写")
                TimeUnit.SECONDS.sleep(3)
            }
        }
    }


}

fun main() {
    val readAndWrite = ReadAndWrite()
    while (true) {
        Thread(Runnable { readAndWrite.read() }, "read-thread").start()
        Thread(Runnable { readAndWrite.write() }, "write-thread").start()
        TimeUnit.SECONDS.sleep(10)
    }
}
//Found one Java-level deadlock:
//=============================
//"write-thread":
//waiting to lock monitor 0x000000001cb53708 (object 0x000000076b57d3f0, a java.lang.Object),
//which is held by "read-thread"
//"read-thread":
//waiting to lock monitor 0x000000001cb534f8 (object 0x000000076b57d400, a java.lang.Object),
//which is held by "write-thread"
//
//Java stack information for the threads listed above:
//===================================================
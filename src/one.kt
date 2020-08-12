import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FirstThread : Thread() {

    override fun run() {
        println(currentThread().name)
        println("现在是时间${DateTimeFormatter.ISO_DATE.format(LocalDateTime.now())}")
    }
}

class FirstRun : Runnable {

    override fun run() {
        println(Thread.currentThread().name)
        println("小猪快跑")
    }

}

fun main() {
    println(Thread.currentThread().name)
    val firstThread = FirstThread()
    firstThread.name = "sub-first-thread"
    firstThread.start()
    println(Thread.currentThread().name)
    Thread(FirstRun()).start()

}
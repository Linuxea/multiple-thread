/**
 * 简易叫号处理逻辑
 */
class TicketRun : Runnable {

    var index = 0

    override fun run() {
        synchronized(this) {
            while (index < 500) {
                index++
                println("现在叫到的是${index}")
            }
        }
    }

}

fun main() {
    // 创建唯一的 runnable 实例
    val ticketRun = TicketRun()
    // 共享实例
    Thread(ticketRun, "窗口1").start()
    Thread(ticketRun, "窗口2").start()
    Thread(ticketRun, "窗口3").start()
    Thread(ticketRun, "窗口4").start()
}
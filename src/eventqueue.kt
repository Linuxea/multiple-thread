import java.util.concurrent.TimeUnit

class EventQueue<T> {

    private val maxLengthLimit = 10
    private val array = mutableListOf<T>()
    private val lock = Object()

    /**
     * 弹出
     */
    @ExperimentalStdlibApi
    fun lPop(): T? {
        synchronized(lock) {
            // 使用 while 而不是 if 防止多个消费者同时唤醒
            while (array.size == 0) {
                println("队列空了")
                lock.wait()
            }
            val fist = array.removeFirst()
            println("弹出并通知${fist}")
            lock.notifyAll()
            return fist
        }
    }

    /**
     * 放入
     */
    fun rPush(t: T) {
        synchronized(lock) {
            // 使用 while 而不是 if 防止多个生产者同时唤醒
            while (array.size >= maxLengthLimit) {
                println("队列满了")
                print()
                lock.wait()
            }
            println("加入${t}并通知")
            array.add(t)
            lock.notifyAll()
        }
    }

    fun print() {
        synchronized(lock) {
            println("当前队列情况${array}")
        }
    }

}

@ExperimentalStdlibApi
fun main() {
    val eventQueue = EventQueue<String>()
    var i = 0
    while (true) {
        Thread { eventQueue.lPop() }.start()
        Thread { eventQueue.lPop() }.start()
        Thread { eventQueue.rPush(i++.toString()) }.start()
        Thread { eventQueue.rPush(i++.toString()) }.start()
        Thread { eventQueue.rPush(i++.toString()) }.start()
        TimeUnit.MILLISECONDS.sleep(100)
    }
}
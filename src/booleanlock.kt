import java.util.concurrent.TimeUnit

interface BooleanLock {

    /**
     * 无限等待锁
     */
    fun lock()

    /**
     * 超时等待锁
     */
    fun lock(second: Long)

    /**
     * 解锁
     */
    fun unlock()

    /**
     * 当前线程信息
     */
    fun currentThread(): Thread

    /**
     * 当前阻塞线程队列
     */
    fun blockThreads(): List<Thread>

}


class BooleanLockImpl : BooleanLock {

    /**
     * 是否锁标志
     */
    @Volatile
    private var lock = false

    /**
     * 当前锁持有的线程
     */
    private var holdThread: Thread? = null

    /**
     * 阻塞线程队列
     */
    private val blockThread = mutableListOf<Thread>()

    private val mutex = Object()

    override fun lock() {
        println("${currentThread()}尝试获取锁中...")
        synchronized(mutex) {
            while (lock) {
                val currentThread = currentThread()
                if (blockThread.contains(currentThread) == false) {
                    blockThread.add(currentThread)
                }
                mutex.wait()
            }
            this.lock = true
            this.holdThread = currentThread()
            this.blockThread.remove(currentThread())
        }
    }

    override fun lock(second: Long) {
        if (second <= 0) {
            throw IllegalArgumentException("等待时间不能小于等于0")
        }

        var remain = second * 1000
        val endTime = System.currentTimeMillis() + remain
        while (this.lock) {
            // 当还有等待时间时
            remain = endTime - System.currentTimeMillis()
            if (remain <= 0) {
                throw Exception("已经等了${second}还是没有拿到锁")
            }
            // 不放入线程阻塞队列中
        }
        this.lock = true
        this.holdThread = currentThread()
        this.blockThread.remove(currentThread())

    }


    override fun unlock() {
        println("${currentThread()}释放锁")
        synchronized(mutex) {
            val currentThread = currentThread()
            if (this.holdThread == currentThread) {
                this.lock = false
                this.holdThread = null
                mutex.notifyAll()
            }
        }
    }

    override fun currentThread(): Thread {
        return Thread.currentThread()
    }

    override fun blockThreads(): List<Thread> {
        return blockThread
    }

}

var i = 0
val booleanLockImpl = BooleanLockImpl()
fun main() {
//    lockMethod()
    Thread { lockTimeOutMethod() }.start()
    Thread { lockTimeOutMethod() }.start()
}

fun lockMethod() {
    val map = (0 until 10000).map { Thread { demo() } }
    map.forEach { it.start() }
    map.forEach { it.join() }
    println("i的最终值为${i}")
}


fun lockTimeOutMethod() {
    try {
        booleanLockImpl.lock(3)
        println("我故意睡久点")
        TimeUnit.SECONDS.sleep(4)
    } finally {
        booleanLockImpl.unlock()
    }
}

fun demo() {
    try {
        booleanLockImpl.lock()
        i++
    } finally {
        booleanLockImpl.unlock()
    }
}







































import java.util.*


// 线程池
// 概念
// - 任务队列
// - init 初始化线程数量
// - core 核心线程数量
// - max 最大线程数量
// intit <= core <= max

interface ThreadPool {

    /**
     * 执行 runnable
     */
    fun execute(runnable: Runnable)

    /**
     * 关闭线程池
     */
    fun shutdown()

    /**
     * 获取初始化线程大小
     */
    fun initSize():Int

    /**
     * 获取核心线程大小
     */
    fun coreSize():Int

    /**
     * 获取最大线程大小
     */
    fun maxSize():Int

    /**
     * 任务队列大小
     */
    fun queueSize():Int

    /**
     * 活跃线程大小
     */
    fun myActiveCount():Int

    /**
     * 线程池是否关闭
     */
    fun isShutdown(): Boolean


}


/**
 * 任务队列
 */
interface RunnableQueue {

    /**
     * 入队
     */
    fun offer(runnable: Runnable)

    /**
     * 任务出队
     */
    fun take(): Runnable

    /**
     * 队列大小
     */
    fun size():Int


}


interface ThreadFactory {

    fun createThread(thread: Thread): Thread

}


interface DenyPolicy {

    fun reject(runnable: Runnable, threadPool: ThreadPool)


}

class DiscardPolicy : DenyPolicy {
    override fun reject(runnable: Runnable, threadPool: ThreadPool) {
        // 直接丢弃
    }
}

class AbortPolicy: DenyPolicy {
    override fun reject(runnable: Runnable, threadPool: ThreadPool) {
        throw RunnableDenyException("线程池已满")
    }
}

class RunnerDenyPolicy: DenyPolicy {
    override fun reject(runnable: Runnable, threadPool: ThreadPool) {
        if(threadPool.isShutdown() == false) {
            runnable.run()
        }
    }

}

class RunnableDenyException() : RuntimeException() {

    constructor(message: String) {
        RuntimeException(message)
    }

}


class InternalTask: Runnable {

    lateinit var runnableQueue: RunnableQueue
    @Volatile var running = true





    override fun run() {

        while (running && Thread.currentThread().isInterrupted == false) {
            runnableQueue.take().run()
        }

    }

    fun stop(){
        this.running = false
    }

}



class LinkedListRunnableQueue :  RunnableQueue {

    var maxLimit: Int = 0

    lateinit var denyPolicy: DenyPolicy

    private val runnableList: LinkedList<Runnable> = LinkedList()

    lateinit var threadPool: ThreadPool

    private val mutex = Object()




    override fun offer(runnable: Runnable) {
        synchronized(mutex) {
            if(runnableList.size > maxLimit) {
                denyPolicy.reject(runnable, threadPool)
            } else {
                runnableList.addLast(runnable)
                mutex.notifyAll()
            }
        }
    }

    override fun take(): Runnable {
        synchronized(mutex) {
            while (runnableList.isEmpty()) {
                mutex.wait()
            }
            return runnableList.removeFirst()
        }
    }

    override fun size(): Int {
        synchronized(mutex) {
            return runnableList.size
        }
    }

}

class BasicThreadPool: Thread(), ThreadPool {

    var initSize: Int = 0

    var coreSize: Int = 0

    var maxSize: Int = 0

    var activeCount: Int = 0

    lateinit var threadFactory: ThreadFactory

    lateinit var runnableQueue: RunnableQueue

    var shutDown: Boolean = false

    //工作线程队列
    val threadQueue: Queue<Any>  = ArrayDeque<Any>();

    var denyPolicy: DenyPolicy = AbortPolicy()


    fun init(){
        this.start()
        repeat(initSize) {
            //创建任务线程，并且启动
            val internalTask = InternalTask()
            internalTask.runnableQueue = this.runnableQueue
            val thread: Thread = threadFactory.createThread(Thread(internalTask))
//            val threadTask = ThreadTask(thread, internalTask)
//            threadQueue.offer(threadTask)
            activeCount++
            thread.start()
        }
    }

    override fun execute(runnable: Runnable) {
        if(this.shutDown) {
            throw IllegalAccessException("已关闭")
        }
        this.runnableQueue.offer(runnable)

    }

    override fun shutdown() {
        TODO("Not yet implemented")
    }

    override fun initSize(): Int {
        TODO("Not yet implemented")
    }

    override fun coreSize(): Int {
        TODO("Not yet implemented")
    }

    override fun maxSize(): Int {
        TODO("Not yet implemented")
    }

    override fun queueSize(): Int {
        TODO("Not yet implemented")
    }

    override fun myActiveCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isShutdown(): Boolean {
        TODO("Not yet implemented")
    }

}

















import java.util.concurrent.TimeUnit

interface FetchNumTask {

    fun fetch(): Int

}

class OneTask : FetchNumTask {
    override fun fetch(): Int {
        TimeUnit.SECONDS.sleep(3)
        return 3
    }
}


class TwoTask : FetchNumTask {
    override fun fetch(): Int {
        TimeUnit.SECONDS.sleep(5)
        return 5
    }
}

class ThreeTask : FetchNumTask {
    override fun fetch(): Int {
        TimeUnit.SECONDS.sleep(10)
        return 10
    }
}

fun main() {

    val currentTimeMillis = System.currentTimeMillis()
    val oneTask = Thread(Runnable { OneTask().fetch() })
    oneTask.start()
    val twoTask = Thread(Runnable { TwoTask().fetch() })
    twoTask.start()
    val threeTask = Thread(Runnable { ThreeTask().fetch() })
    threeTask.start()
    threeTask.join()
    oneTask.join()
    twoTask.join()
    println((System.currentTimeMillis() - currentTimeMillis) / 1000)

}
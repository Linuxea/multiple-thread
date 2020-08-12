fun createThreadWithPrefixName(index: Int, run: Runnable, threadGroup: ThreadGroup): Thread {
    val prefix = "雷神"
    return Thread(threadGroup, run, "${prefix}-${index}")
}

fun main() {
    val threadGroup = ThreadGroup("黑虎帮")
    (0..10).map { createThreadWithPrefixName(it, Runnable { println("没事说几句") }, threadGroup) }.forEach {
        println("${it.name}#${it.threadGroup.name}准备启动")
        println("线程id是${it.id}")
        it.start()
    }
}



class ExceptionRunnable : Runnable {
    override fun run() {
        println("${1 / 0}")
    }

}


fun main() {
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
        println("${t.name}发生了异常${e.message}")
    }

    Thread(ExceptionRunnable(), "exception-runnable").start()
}
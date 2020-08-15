


fun main() {

    Runtime.getRuntime().addShutdownHook(Thread{
        // 线程会在 jvm 进程退出时执行
        // 但 kill -9 会来不及执行
        println("我被关闭了")
    })
    
}
#### 异步线程池
异步线程池，可用来记录日志、发送消息等异步操作；
* 配置AsyncConfigurer，定义线程池及线程异常处理程序
* @EnableAsync 配置允许使用异步线程
* @Async 注解方法，异步执行
#### 注意：
* 异步方法必须在其他bean里调用才会生效，调用@Async注解的方法时，spring会通过代理，在子线程里执行，达到异步调用与并行执行的目的
* 如果线程有返回值使用Future的包装类 AsyncResult获取返回值

#### Spring的TaskExecutor和JUC中的ExecutorService区别
* Spring 3.0开始TaskExecutor继承Executor，更方便融入Spring bean生态  
* ExecutorService的实现类：
    1. ForkJoinPool
    Java1.7引入了一种新的并发框架,主要用于实现“分而治之”的算法,配合ManagedBlocker适合计算密集型任务。
    2. ScheduledThreadPoolExecutor
    用于延时或者定期执行的异步任务/线程
    3. ThreadPoolExecutor
    提供线程池执行任务  
* TaskExecutor的实现类
    1. ThreadPoolTaskExecutor 
    提供线程池执行任务，内部使用ThreadPoolExecutor，即ThreadPoolExecutor的Spring包装类
    2. ScheduledThreadPoolExecutor
    ScheduledThreadPoolExecutor的Spring包装类。Spring中任务的调度使用的就是这个类，比如@Scheduled
#### 线程池Order
If fewer than corePoolSize threads are running, the Executor always prefers adding a new thread rather than queuing.  
If corePoolSize or more threads are running, the Executor always prefers queuing a request rather than adding a new thread.  
If a request cannot be queued, a new thread is created unless this would exceed maximumPoolSize, in which case, the task will be rejected.  
corePoolSize -> workQueue -> maximumPoolSize -> rejectHandler

#### Ali check
1. 创建线程或线程池时请指定有意义的线程名称，方便出错时回溯。  
   创建线程池的时候请使用带ThreadFactory的构造函数，并且提供自定义ThreadFactory实现或者使用第三方实现。
2. 多线程并行处理定时任务时，Timer运行多个TimeTask时，只要其中之一没有捕获抛出的异常，其它任务便会自动终止运行，使用ScheduledExecutorService则没有这个问题。 
3. 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。  
   说明：Executors返回的线程池对象的弊端如下：
    1. FixedThreadPool和SingleThreadPool:  
    允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
    2. CachedThreadPool:  
    允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
4. 线程资源必须通过线程池提供，不允许在应用中自行显式创建线程。  
   说明：使用线程池的好处是减少在创建和销毁线程上所花的时间以及系统资源的开销，解决资源不足的问题。
   如果不使用线程池，有可能造成系统创建大量同类线程而导致消耗完内存或者“过度切换”的问题。
        




# AOP

#### AOP中的相关概念

* **Aspect**（切面）： 由切点（Pointcut）和增强（Advice/Introduction）组成。
* **Pointcut** （切点）：通过逻辑关系或通配符等,定义advice生效的执行点,类似于查询条件查询符合条件的JoinPoint。
* **JoinPoint** （连接点）：程序中定义的点，典型的包括方法调用、异常处理、类初始化的执行等。
* **Advice** （增强）：定义在Pointcut里面的具体操作，包括before、after和around。
* **Target** （目标对象）：织入Advice的目标对象。

#### AOP中的基本操作
1. 通过Pointcut和Advice定位到连接点(Jointpoint)上
2. 在Advice中编写切面的代码。

#### Spring中的注解
+ **@Aspect** 把当前类标识为一个切面供容器读取  
+ **@Pointcut** 定义切点，切点方法不用任何代码，返回值是void，重要的是条件表达式,常用execution,@target,@annotation   
+ **@Before** 标识一个前置增强方法，相当于BeforeAdvice的功能
+ **@AfterThrowing** 异常抛出增强，相当于ThrowsAdvice

#### 命名及匿名切入点
命名切入点可以被其他切入点引用，而匿名切入点是不可以的,只有@AspectJ支持命名切入点，而Schema风格不支持命名切入点。
如下所示，@AspectJ使用如下方式引用命名切入点,log()为切入点命名：
```
    @Pointcut("execution(public * com.example.demo.controller.*.*(..))")
    public void log() {
    }
    @Before("log()")
    public void deBefore(JoinPoint joinPoint)
```
AspectJ使用 且（&&）、或（||）、非（！）来组合切入点表达式,支持 * .. +通配符

#### 过滤器(Filter)
参数是request、response，对于request可过滤URL、提前设置参数/字符集、去除掉一些非法字符；
对于response可对返回数据设置编码、文件数据转换等

#### 拦截器（Interceptor）
切面编程的一种实现,用于方法前、后处理，可用于权限校验等；

#### 拦截器和过滤器的区别：
1. 拦截器是基于Java的反射机制的，而过滤器是基于函数回调。
2. 过滤器依赖于servlet容器，拦截器依赖于web框架，拦截器在SpringMVC中就是依赖于SpringMVC框架。。
3. 拦截器可以访问action上下文、值栈里的对象，而过滤器不能访问。
4. 拦截器可以获取IOC容器中的各个bean，而过滤器就不行，这点很重要，在拦截器里注入一个service，可以调用业务逻辑  
As the doc said, fine-grained handler-related preprocessing tasks are candidates for HandlerInterceptor implementations, 
especially factored-out common handler code and authorization checks. 
On the other hand, a Filter is well-suited for request content and view content handling, 
like multipart forms and GZIP compression. 
This typically shows when one needs to map the filter to certain content types (e.g. images), or to all requests.

#### 示例代码

切面类`LogAspect`
```java
@Aspect
@Component
public class LogAspect {
    @Resource
    private HttpServletRequest request;

    @Pointcut("execution(public * com.example.springbootaop.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void deBefore(JoinPoint joinPoint) {
        System.out.println(" -----------Before advice-----------");
        System.out.println("HTTP_METHOD : " + request.getMethod());
        System.out.println("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        System.out.println("ARGS : " + Arrays.toString(joinPoint.getArgs()));

    }

    @After("log()")
    public void after(JoinPoint joinPoint) {
        System.out.println("-----------After finally advice-----------");
    }

    @AfterReturning(returning = "returning", pointcut = "log()")
    public void doAfterReturning(JoinPoint joinPoint, Object returning) {
        System.out.println("After returning advice returning : " + returning);
    }

    @AfterThrowing(throwing = "throwing", pointcut = "log()")
    public void throwss(JoinPoint joinPoint, Exception throwing) {
        System.out.println("After throwing advice ：throwing " + throwing.getMessage());
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        System.out.println("-----------Around advice--------");
        try {
            Object returning = proceedingJoinPoint.proceed();
            System.out.println("Around advice returning : " + returning);
            return returning;
        } catch (Throwable throwing) {
            System.out.println("Around advice ：throwing " + throwing.getMessage());
            return null;
        }
    }
}
```
```java
@RestController
public class UserController {
    @RequestMapping("/test1")
    public Object test1() {
        return "test1";
    }

    @RequestMapping("/test2")
    public Object test2() {
        return 1 / 0;
    }

    @RequestMapping("/test3")
    @UserLog(desc = "test3")
    public Object test3() {
        return "test3";
    }
}
```
自定义拦截器MyInterceptor
```java
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        request.setAttribute("t1", System.currentTimeMillis());
        System.out.println("拦截器:拦截到URL请求：" + request.getRequestURI());
        if (request.getRequestURI().contains("test3")) {
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView mav)
            throws Exception {
        long t1 = (Long) request.getAttribute("t1");
        long t2 = System.currentTimeMillis();
        System.out.println("拦截器:请求接口 " + request.getRequestURI() + " 处理时间：" + (t2 - t1) + "ms");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception exc)
            throws Exception {
        System.out.println("拦截器:请求结束，清理资源Over");
    }
}
```
拦截器配置
```java
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    /**
     * 添加拦截器
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/*");
    }
}
```
过滤器
```java
@WebFilter(urlPatterns = "/*", filterName = "myFilter")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest res, ServletResponse req, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) res;
        HttpServletResponse response = (HttpServletResponse) req;
        System.out.println("过滤器：过滤到URL请求：" + request.getRequestURI());
        long start = System.currentTimeMillis();
        filterChain.doFilter(res, req);
        System.out.println("过滤器：处理完URL结果：" + response.getStatus());
    }

    @Override
    public void destroy() {

    }
}
```
输出
```
过滤器：过滤到URL请求：/test2
拦截器:拦截到URL请求：/test2
-----------Around advice--------
 -----------Before advice-----------
HTTP_METHOD : GET
CLASS_METHOD : com.example.springbootaop.controller.UserController.test2
ARGS : []
Around advice ：throwing / by zero
-----------After finally advice-----------
After returning advice returning : null
拦截器:请求接口 /test2 处理时间：60ms
拦截器:请求结束，清理资源Over
过滤器：处理完URL结果：200
```
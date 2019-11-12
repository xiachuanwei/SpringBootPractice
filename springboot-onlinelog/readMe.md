# 线上日志

### Reference Documentation
* 使用log4j2实现，logback应该也差不多
* 采用重写AbstractAppender中的方法，自定义日志输出的位置
* 线上日志，则是系统生成日志后，直接将日志信息通过websocket发送到前端
* websocket及页面使用通用配置即可
AbstractAppender重写
 ```java
@Plugin(name = "HtmlAppender", category = "Core", elementType = "appender", printObject = true)
public class HtmlAppender extends AbstractAppender {

    // 构造函数，除了实现父类需要的参数以外，还可以自定义需要的参数
    private HtmlAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout,
                         final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }

    /* 覆盖append方法，实现自定义功能，比如将日志写入文件或者发送到日志收集中心等 */
    @Override
    public void append(LogEvent event) {
        try {
            String message = new String(getLayout().toByteArray(event));
            LogWebSocketServer.sendMessageToAll(message);
        } catch (Exception ex) {
            if (!ignoreExceptions()) {
                throw new AppenderLoggingException(ex);
            }
        }
    }

    // 下面这个方法可以接收配置文件中的参数信息
    @PluginFactory
    public static HtmlAppender createAppender(@PluginAttribute("name") String name,
                                              @PluginElement("Filter") final Filter filter,
                                              @PluginElement("Layout") Layout<? extends Serializable> layout,
                                              @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
        if (name == null) {
            LOGGER.error("No name provided for MyCustomAppenderImpl");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new HtmlAppender(name, filter, layout, true);
    }

}
```
xml配置
```xml
<Configuration status="WARN" monitorInterval="300">  
    <properties>  
        <!-- 配置为绝对路径 -->
        <property name="LOG_HOME">D:logs/onlineLog/</property>
        <!-- 文件输出格式 -->
		<property name="PATTERN">%date{yyyy-MM-dd HH:mm:ss.SSS} %level [%thread][%file:%line] - %msg%n</property> 
    </properties>  
    <Appenders>  
        <Console name="Console" target="SYSTEM_OUT">  
            <PatternLayout pattern="${PATTERN}" />  
        </Console>    
        <!-- 中间还有其他配置 -->
        <HtmlAppender name="HtmlAppender">
            <PatternLayout pattern="${PATTERN}" />
        </HtmlAppender>
    </Appenders>

    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console" />
            <AppenderRef ref="HtmlAppender" />
        </Root>
    </Loggers>  
</Configuration>
```
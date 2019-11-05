package com.example.springbootonlinelog.log;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;

/**
 * 自定义Appender，继承 AbstractAppender 只需要覆盖自已想要的方法即可<br>
 * 类上面的注解是用来设置配置文件中的标签。
 */
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

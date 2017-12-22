package com.aliyun.openservices.cms.metric.reporter;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.cms.metric.impl.Metric;
import com.aliyun.openservices.cms.metric.registry.MetricName;
import com.aliyun.openservices.cms.metric.registry.MetricRegistry;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.registry.wrapper.MetricWrapper;
import org.slf4j.Logger;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;


public class Slf4jReporter extends ConsoleReporter {



    public enum LoggingLevel {TRACE, DEBUG, INFO, WARN, ERROR}

    private final LoggerProxy loggerProxy;


    public Slf4jReporter(ScheduledExecutorService service, MetricRegistry registry, Logger logger, LoggingLevel loggingLevel) {
        super(service, registry);
        switch (loggingLevel) {
            case TRACE:
                loggerProxy = new TraceLoggerProxy(logger);
                break;
            case INFO:
                loggerProxy = new InfoLoggerProxy(logger);
                break;
            case WARN:
                loggerProxy = new WarnLoggerProxy(logger);
                break;
            case ERROR:
                loggerProxy = new ErrorLoggerProxy(logger);
                break;
            default:
            case DEBUG:
                loggerProxy = new DebugLoggerProxy(logger);
                break;

        }
    }

    @Override
    protected void reportCustomMetric(MetricName name, MetricWrapper wrapper, RecordLevel level, Date date) {
        Metric metric = wrapper.getMetricByRecordLevel(level);
        if(metric == null) {
            return;
        }
        if(loggerProxy.isEnabled()) {
            loggerProxy.log(JSONObject.toJSONString(covert(name, metric, date, level)));
        }
    }





    /* private class to allow logger configuration */
    static abstract class LoggerProxy {
        protected final Logger logger;

        public LoggerProxy(Logger logger) {
            this.logger = logger;
        }

        abstract void log(String format, Object... arguments);

        abstract boolean isEnabled();
    }

    /* private class to allow logger configuration */
    private static class DebugLoggerProxy extends LoggerProxy {
        public DebugLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(String format, Object... arguments) {
            logger.debug(format, arguments);
        }

        @Override
        public boolean isEnabled() {
            return logger.isDebugEnabled();
        }
    }

    /* private class to allow logger configuration */
    private static class TraceLoggerProxy extends LoggerProxy {
        public TraceLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(String format, Object... arguments) {
            logger.trace(format, arguments);
        }

        @Override
        public boolean isEnabled() {
            return logger.isTraceEnabled();
        }
    }

    /* private class to allow logger configuration */
    private static class InfoLoggerProxy extends LoggerProxy {
        public InfoLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(String format, Object... arguments) {
            logger.info(format, arguments);
        }

        @Override
        public boolean isEnabled() {
            return logger.isInfoEnabled();
        }
    }

    /* private class to allow logger configuration */
    private static class WarnLoggerProxy extends LoggerProxy {
        public WarnLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(String format, Object... arguments) {
            logger.warn(format, arguments);
        }

        @Override
        public boolean isEnabled() {
            return logger.isWarnEnabled();
        }
    }

    /* private class to allow logger configuration */
    private static class ErrorLoggerProxy extends LoggerProxy {
        public ErrorLoggerProxy(Logger logger) {
            super(logger);
        }

        @Override
        public void log(String format, Object... arguments) {
            logger.error(format, arguments);
        }

        @Override
        public boolean isEnabled() {
            return logger.isErrorEnabled();
        }
    }

}

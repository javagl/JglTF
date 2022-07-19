/*
 * www.javagl.de - JglTF
 *
 * Copyright 2015-2016 Marco Hutter - http://www.javagl.de
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package de.javagl.jgltf.model.io.test;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Utility class for configuring a logger to produce "nice" output. <br>
 * <br>
 * Yes, I know that this should be done using a properties file.
 */
public class LoggerUtil
{
    /**
     * The set of configured loggers
     */
    private static final Set<Logger> configuredLoggers = new HashSet<Logger>();
    
    /**
     * Utility method to initialize the logging
     */
    public static void initLogging()
    {
        Logger logger = null;
        
        logger = Logger.getLogger("");
        LoggerUtil.configureDefault(logger);
        logger.setLevel(Level.CONFIG);
    }

    /**
     * Configure the given logger to produce "nice" output
     * 
     * @param logger The logger to configure
     */
    public static void configureDefault(Logger logger)
    {
        configuredLoggers.add(logger);
        
        for (Handler handler : logger.getHandlers())
        {
            logger.removeHandler(handler);
        }
        logger.setUseParentHandlers(false);
        Handler handler = new Handler()
        {
            @Override
            public void publish(LogRecord record)
            {
                String s = getFormatter().format(record);
                System.out.println(s);
            }
            
            @Override
            public void flush()
            {
                // Nothing to do here
            }
            
            @Override
            public void close() throws SecurityException
            {
                // Nothing to do here
            }
        };
        logger.addHandler(handler);
        
        handler.setFormatter(new Formatter()
        {
            @Override
            public String format(LogRecord record)
            {
                String className = record.getSourceClassName();
                String simpleName = getUnqualifiedClassName(className);
                String level = 
                    String.format("%-7s", record.getLevel().toString());
                return level+": "+simpleName+": "+record.getMessage();
            }
            
            private String getUnqualifiedClassName(String className)
            {
                return className.substring(className.lastIndexOf('.')+1);
            }
            
        });
    }

    /**
     * Private constructor to prevent instantiation
     */
    private LoggerUtil()
    {
        // Private constructor to prevent instantiation
    }

    /*
    static void configureDefault()
    {
        Logger logger = null;

        logger = Logger.getLogger("");
        configureDefault(logger);
        logger.setLevel(Level.FINE);
    }
    
    public static void main(String args[])
    {
        configureDefault();
        
        Logger logger = Logger.getLogger(LoggerUtil.class.getName());

        //configureLoggerDefault(logger);
        //logger.setLevel(Level.FINER);
        
        logger.finest("Finest");
        logger.finer("Finer");
        logger.fine("Fine");
        logger.info("Info");
        logger.warning("Warning");
        logger.severe("Severe");
    }
    */
    
}

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
package de.javagl.jgltf.browser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.function.DoubleConsumer;
import java.util.logging.Logger;

import de.javagl.swing.tasks.executors.ObservableExecutorCompletionService;
import de.javagl.swing.tasks.executors.ObservableExecutorService;

/**
 * Utility methods related to executor services
 */
class ExecutorServiceUtils
{
    /**
     * The logger used in this class
     */
    private static final Logger logger = 
        Logger.getLogger(ExecutorServiceUtils.class.getName());
    
    /**
     * Invoke all callables from the given lists by submitting them to the
     * given executor service, and wait for the results. If any of the 
     * callables throws an exception, a message will be printed. If the
     * future that belongs to one of the callables throws an 
     * InterruptedException or a CancellationException, then this
     * method will no longer wait for the other futures, but return the
     * exception instead.
     * 
     * @param <T> The type of the callables
     * 
     * @param observableExecutorService The observable executor service
     * @param progressConsumer A consumer for the progress, in [0,1]
     * @param callableLists The lists of callables
     * @return The exception if an InterruptedException or CancellationException
     * was thrown, or <code>null</code> if all tasks completed.
     */
    @SafeVarargs
    static <T> Throwable invokeAll(
        ObservableExecutorService observableExecutorService,
        DoubleConsumer progressConsumer, 
        List<? extends Callable<T>> ... callableLists)
    {
        List<Callable<T>> callables = new ArrayList<Callable<T>>();
        for (List<? extends Callable<T>> callableList : callableLists)
        {
            for (Callable<T> callable : callableList)
            {
                callables.add(callable);
            }
        }
        if (callables.isEmpty())
        {
            return null;
        }
        
        ObservableExecutorCompletionService<T> completionService = 
            new ObservableExecutorCompletionService<T>(
                observableExecutorService);
        for (Callable<T> callable : callables)
        {
            completionService.submit(callable);
        }
        
        Throwable result = null;
        for (int i = 0; i < callables.size(); i++)
        {
            try
            {
                completionService.take().get();
            } 
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                result = e;
                break;
            } 
            catch (CancellationException e)
            {
                result = e;
                break;
            }
            catch (ExecutionException e)
            {
                logger.warning(e.getMessage());
                //e.printStackTrace();
            }
            double progress = (double)i / callables.size();
            progressConsumer.accept(progress);
        }
        progressConsumer.accept(1.0);
        return result;
    }

    /**
     * Private constructor to prevent instantiation
     */
    private ExecutorServiceUtils()
    {
        // Private constructor to prevent instantiation
    }
}

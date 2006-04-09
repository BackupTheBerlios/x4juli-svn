/*
 * Copyright 2005, x4juli.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.x4juli.global.components;

import java.util.logging.ErrorManager;
import java.util.logging.LogManager;

import junit.framework.TestCase;

import org.x4juli.global.resources.MessageProperties;
import org.x4juli.global.spi.AbstractComponent;
import org.x4juli.global.spi.Component;
import org.x4juli.global.spi.ExtendedLogger;
import org.x4juli.global.spi.LogIllegalStateException;
import org.x4juli.global.spi.LoggerRepository;
import org.x4juli.logger.NOPLogger;

/**
 * Testcase for x4juli.
 * @since 0.5
 */
public abstract class AbstractJuliTestCase extends TestCase implements
        Component {
    private final static int ERROR_COUNT_LIMIT = 3;

    protected LoggerRepository repository;

    protected ErrorManager errManager = new ErrorManager();

    private ExtendedLogger logger;

    private int errorCount = 0;

    /**
     * 
     */
    public AbstractJuliTestCase() {
        super();
    }

    /**
     * @param name
     */
    public AbstractJuliTestCase(String name) {
        super(name);
    }

	/**
	 * @see org.x4juli.global.spi.Component#getMessageProperties()
	 */
	public MessageProperties getMessageProperties() {
		return null;
	}
	
	
    /**
     * {@inheritDoc}
     * @since
     */
    protected void setUp() throws Exception {
        System.out.flush();
        System.err.flush();
    }

    /**
     * {@inheritDoc}
     * @since
     */
    protected void tearDown() {
        //TODO reset wiederherstellen 
        //manager.reset();
        System.out.flush();
        System.err.flush();
    }

    /**
     * {@inheritDoc}
     * @since 0.7
     */
    public void setLoggerRepository(final LoggerRepository repository) {
        if (this.repository == null) {
            this.repository = repository;
        } else if (this.repository != repository) {
            throw new LogIllegalStateException("Repository has been already set");
        }
    }

    /**
     * Return the {@link LoggerRepository} this component is attached to.
     *
     * @return Owning LoggerRepository
     * @since 0.7
     */
    protected LoggerRepository getLoggerRepository() {
        return this.repository;
    }

    /**
     * Called by derived classes when they deem that the component has recovered
     * from an erroneous state.
     */
    protected void resetErrorCount() {
        errorCount = 0;
    }

    /**
     * Return an instance specific logger to be used by the component itself.
     * This logger is not intended to be accessed by the end-user, hence the
     * protected keyword.
     * 
     * <p>
     * This logger always sends output to an
     * 
     * @link{ConsoleHandler}, which outputs to System.err
     *                        </p>
     * 
     * @return A ExtendedLogger instance.
     */
    protected ExtendedLogger getLogger() {
        if (logger == null) {
            MessageProperties messageProperties = getMessageProperties();
            String resource = null;
            if (messageProperties != null) {
                resource = messageProperties.getValueAsString();
            }
            //TODO Message Properterties
            this.logger = this.repository.getLogger(this.getClass().getName());
        }
        return logger;
    }

    /**
     * Frequently called methods in log4j components can invoke this method in
     * order to avoid flooding the output when logging lasting error conditions.
     * 
     * @return a regular logger, or a NOPLogger if called too frequently.
     */
    protected ExtendedLogger getNonFloodingLogger() {
        if (errorCount++ >= ERROR_COUNT_LIMIT) {
            return NOPLogger.NOP_LOGGER;
        } else {
            return getLogger();
        }
    }

}

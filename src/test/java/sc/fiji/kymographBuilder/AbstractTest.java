/*-
 * #%L
 * KymographBuilder: Yet Another Kymograph Fiji plugin.
 * %%
 * Copyright (C) 2016 - 2022 Fiji developers.
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

package sc.fiji.kymographBuilder;

import org.junit.After;
import org.junit.Before;
import org.scijava.Context;
import org.scijava.command.CommandService;
import org.scijava.io.IOService;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;

public class AbstractTest {

	@Parameter
	protected Context context;

	@Parameter
	protected LogService log;

	@Parameter
	protected IOService io;

	@Parameter
	protected CommandService command;

	/** Subclasses can override to create a context with different services. */
	protected Context createContext() {
		return new Context();
	}

	@Before
	public void setUp() {
		createContext().inject(this);
	}

	@After
	public synchronized void cleanUp() {
		if (context != null) {
			context.dispose();
			context = null;
			log = null;
			io = null;
			command = null;
		}
	}

}

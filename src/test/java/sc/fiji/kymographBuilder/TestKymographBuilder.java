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

import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import net.imagej.Dataset;

import org.junit.Test;
import org.scijava.command.CommandModule;

public class TestKymographBuilder extends AbstractTest {

	/*	@Test
			public void TestKymographBuilderCommand() throws IOException, InterruptedException,
				ExecutionException
			{
				final String sampleImage =
					"8bit-unsigned&pixelType=uint8&indexed=true&lengths=10,10,5&axes=X,Y,Time.fake";
		
				Dataset dataset = (Dataset) io.open(sampleImage);
		
				Map<String, Object> inputs = new HashMap<>();
				inputs.put("input", dataset);
		
				// Add a line
				// TODO: I can't run this test on Travis because ROIManager can't run headless...
				RoiManager rm = RoiManager.getRoiManager();
				Line line = new Line(1, 1, 5, 5);
				rm.addRoi(line);
		
				CommandModule module = command.run(KymographBuilder.class, true, inputs).get();
				Dataset output = (Dataset) module.getOutput("kymograph");
		
				assertEquals(output.numDimensions(), dataset.numDimensions() - 1);
			}*/

	/*	@Test
	public void TestKymographBuilderPolyline() throws IOException, InterruptedException,
		ExecutionException
	{
		final String sampleImage =
			"8bit-unsigned&pixelType=uint8&indexed=true&lengths=10,10,5&axes=X,Y,Time.fake";
	
		Dataset dataset = (Dataset) io.open(sampleImage);
	
		Map<String, Object> inputs = new HashMap<>();
		inputs.put("input", dataset);
	
		// Add a line
		// TODO: I can't run this test on Travis because ROIManager can't run headless...
		float[] xPoints = new float[] { 1, 2, 5 };
		float[] yPoints = new float[] { 1, 3, 3 };
		PolygonRoi line = new PolygonRoi(xPoints, yPoints, Roi.POLYLINE);
		rm.addRoi(line);
	
		CommandModule module = command.run(KymographBuilder.class, true, inputs).get();
		Dataset output = (Dataset) module.getOutput("kymograph");
	
		assertEquals(output.numDimensions(), dataset.numDimensions() - 1);
	}*/

	@Test
	public void TestKymographBuilderCommandException() throws IOException, InterruptedException,
		ExecutionException
	{
		final String sampleImage =
			"8bit-unsigned&pixelType=uint8&indexed=true&lengths=10,10,5&axes=X,Y,Time.fake";

		Dataset dataset = (Dataset) io.open(sampleImage);

		Map<String, Object> inputs = new HashMap<>();
		inputs.put("input", dataset);

		// Don't add line.

		CommandModule module = command.run(KymographBuilder.class, true, inputs).get();
		Dataset output = (Dataset) module.getOutput("kymograph");

		assertNull(output);
	}

	@Test
	public void TestKymographBuilderCommandTwoDimensions() throws IOException, InterruptedException,
		ExecutionException
	{
		final String sampleImage = "8bit-unsigned&pixelType=uint8&lengths=10,10&axes=X,Y.fake";

		Dataset dataset = (Dataset) io.open(sampleImage);

		Map<String, Object> inputs = new HashMap<>();
		inputs.put("input", dataset);

		// Don't add line.

		CommandModule module = command.run(KymographBuilder.class, true, inputs).get();
		Dataset output = (Dataset) module.getOutput("kymograph");

		assertNull(output);
	}

}

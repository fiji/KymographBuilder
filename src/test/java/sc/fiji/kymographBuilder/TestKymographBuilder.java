
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
		final String sampleImage =
			"8bit-unsigned&pixelType=uint8&lengths=10,10&axes=X,Y.fake";

		Dataset dataset = (Dataset) io.open(sampleImage);

		Map<String, Object> inputs = new HashMap<>();
		inputs.put("input", dataset);
		
		// Don't add line.

		CommandModule module = command.run(KymographBuilder.class, true, inputs).get();
		Dataset output = (Dataset) module.getOutput("kymograph");

		assertNull(output);
	}

}

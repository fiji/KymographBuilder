
package sc.fiji.kymographBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import net.imagej.Dataset;

import org.junit.Test;
import org.scijava.command.CommandModule;

import ij.gui.Line;
import ij.plugin.frame.RoiManager;

public class TestKymographBuilder extends AbstractTest {

		@Test
		public void TestKymographBuilderCommand() throws IOException, InterruptedException,
			ExecutionException
		{
			final String sampleImage =
				"8bit-unsigned&pixelType=uint8&indexed=true&lengths=10,10,5&axes=X,Y,Time.fake";
	
			Dataset dataset = (Dataset) io.open(sampleImage);
	
			Map<String, Object> inputs = new HashMap<>();
			inputs.put("input", dataset);
	
			// Add a line
			RoiManager rm = RoiManager.getRoiManager();
			Line line = new Line(10, 10, 50, 50);
			rm.addRoi(line);
	
			CommandModule module = command.run(KymographBuilder.class, true, inputs).get();
			Dataset output = (Dataset) module.getOutput("kymograph");
	
			assertEquals(output.numDimensions(), dataset.numDimensions() - 1);
		}

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

}

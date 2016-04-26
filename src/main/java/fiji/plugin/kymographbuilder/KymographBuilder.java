/*
 * #%L
 * KymographBuilder: Yet Another Kymograph Fiji plugin
 * %%
 * Copyright (C) 2016 Hadrien Mary
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

package fiji.plugin.kymographbuilder;

import ij.gui.Roi;
import io.scif.services.DatasetIOService;
import java.util.ArrayList;
import java.util.List;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.axis.Axes;
import net.imagej.display.ImageDisplay;
import net.imagej.display.ImageDisplayService;
import net.imagej.display.OverlayService;
import net.imagej.overlay.Overlay;
import net.imagej.patcher.LegacyInjector;

import org.scijava.ItemIO;
import org.scijava.app.StatusService;
import org.scijava.command.Command;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

@Plugin(type = Command.class, menuPath = "Plugins>Kymograph>KymographBuilder")
public class KymographBuilder implements Command {

    static {
        // NB: Needed if you mix-and-match IJ1 and IJ2 in this class.
        // And even then: do not use IJ1 classes in the API!
        LegacyInjector.preinit();
    }

    @Parameter
    private ImageJ ij;

    @Parameter
    private LogService log;

    @Parameter
    private StatusService statusService;

    @Parameter
    private DatasetIOService datasetIOService;

    @Parameter
    private UIService ui;

    @Parameter
    private ImageDisplayService idService;

    @Parameter
    private OverlayService overlayService;

    @Parameter(type = ItemIO.INPUT)
    private ImageDisplay imageDisplay;

    @Parameter(type = ItemIO.OUTPUT)
    private Dataset kymograph;

    // Which channel is used to build the kymograph. -1 for building on all channels.
    private int channelToUse = -1;

    public static final String PLUGIN_NAME = "KymographBuilder";
    public static final String VERSION = version();

    private static String version() {
        String version = null;
        final Package pack = KymographBuilder.class.getPackage();
        if (pack != null) {
            version = pack.getImplementationVersion();
        }
        return version == null ? "DEVELOPMENT" : version;
    }

    @Override
    public void run() {

        log.info("Running " + PLUGIN_NAME + " version " + VERSION);

        Dataset dataset = (Dataset) imageDisplay.getActiveView().getData();
        
        Roi roi = Utils.checkForROIs(imageDisplay, ij.convert(), ij.ui());
        if (roi == null) {
            // Close the plugin
            return;
        }
                
        // Check if T and Z need to be swapped.
        Utils.swapTimeAndZDimensions(ij, dataset);
        
        // Print some infos
        log.info(Utils.getInfo(dataset, "\t"));

        // Decide which channels to use
        List<Integer> channelsUsed = new ArrayList<>();
        if (channelToUse == -1) {
            // Use all channels
            int channelIdx = dataset.dimensionIndex(Axes.CHANNEL);
            for (int i = 0; i < dataset.dimension(channelIdx); i++) {
                channelsUsed.add(i);
            }
        } else {
            // Use only one channel
            channelsUsed.add(channelToUse);
        }
        log.info("The following channels will be used : " + channelsUsed);

        KymographFactory factory = new KymographFactory(ij.context(), dataset, roi, channelsUsed);
        factory.build();

        // Get the results, add to command output.
        this.kymograph = factory.getKymograph();

        log.info("Kymograph \"" + kymograph + "\" has been correcly generated.");
    }

    public static void main(final String... args) throws Exception {
        // Launch ImageJ as usual.
        final ImageJ ij = net.imagej.Main.launch(args);

        // Launch the command.
        ij.command().run(KymographBuilder.class, true);
    }
}

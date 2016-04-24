package fiji.plugin.kymographbuilder;

import ij.plugin.frame.RoiManager;
import io.scif.services.DatasetIOService;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.display.ImageDisplayService;
import net.imagej.display.OverlayService;
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
    private Dataset dataset;

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

    }

    public void cancel() {
        log.info("Closing " + PLUGIN_NAME);
    }

    public static void main(final String... args) throws Exception {
        // Launch ImageJ as usual.
        final ImageJ ij = net.imagej.Main.launch(args);

        // Load image and rois test data
        KymographBuilder.loadTestData(ij);

        // Launch the command.
        ij.command().run(KymographBuilder.class, true);
    }

    public static void loadTestData(ImageJ ij) throws IOException {

        // Open image
        String fpath;
        fpath = KymographBuilder.class.
                getResource("/testdata/mt.tif").getPath();
        Dataset ds = ij.dataset().open(fpath);
        ij.display().createDisplay(ds);

        ij.log().info("Load mt.tif data.");

        // Add rois
        RoiManager rm = RoiManager.getRoiManager();
        rm.runCommand("Open", KymographBuilder.class
                .getResource("/testdata/mt.roi").getPath());
        rm.runCommand("Show All");

        ij.log().info("Load ROIs data.");
    }
}

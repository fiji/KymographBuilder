import fiji.plugin.kymographbuilder.KymographBuilder;

import ij.plugin.frame.RoiManager;

import java.io.IOException;

import net.imagej.Dataset;
import net.imagej.ImageJ;

public class Main {

    public static void main(final String... args) throws Exception {
        // Launch ImageJ as usual.
        final ImageJ ij = net.imagej.Main.launch(args);

        // Load image and rois test data
        Main.loadTestData(ij);

        // Launch the command.
        ij.command().run(KymographBuilder.class, true);
    }

    public static void loadTestData(ImageJ ij) throws IOException {

        // Open image
        String fpath;
        fpath = Main.class.getResource("/testdata/mt.tif").getPath();
        Dataset ds = ij.dataset().open(fpath);
        ij.display().createDisplay(ds);

        ij.log().info("Load mt.tif data.");

        // Add rois
        RoiManager rm = RoiManager.getRoiManager();
        rm.runCommand("Open", Main.class.getResource("/testdata/mt.roi").getPath());
        rm.runCommand("Show All");

        ij.log().info("Load ROIs data.");

    }
}

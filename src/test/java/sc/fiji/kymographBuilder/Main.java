/*-
 * #%L
 * KymographBuilder: Yet Another Kymograph Fiji plugin.
 * %%
 * Copyright (C) 2016 - 2017 Fiji developers.
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
import ij.plugin.frame.RoiManager;
import sc.fiji.kymographBuilder.KymographBuilder;

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

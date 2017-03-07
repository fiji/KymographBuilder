/*
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

import ij.IJ;
import ij.gui.Roi;
import java.util.concurrent.atomic.AtomicInteger;
import net.imagej.Dataset;
import net.imagej.DatasetService;
import org.scijava.Context;
import org.scijava.convert.ConvertService;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import org.scijava.ui.UIService;

/**
 *
 * @author Hadrien Mary
 */
public class KymographFactory {

    public static AtomicInteger IDcounter = new AtomicInteger(-1);
    private final int ID;

    @Parameter
    private Context context;

    @Parameter
    private DatasetService dsService;

    @Parameter
    private LogService log;

    @Parameter
    private ConvertService convert;

    @Parameter
    private UIService ui;

    private final Dataset dataset;
    private Dataset kymograph;

    private final Roi roi;

    public KymographFactory(Context context, Dataset dataset, Roi roi) {
        context.inject(this);
        this.roi = roi;
        this.dataset = dataset;

        this.ID = IDcounter.incrementAndGet();
    }

    public Dataset getKymograph() {
        return this.kymograph;
    }

    public void build() {

        // Find which Z stack is currently set (the kymograph will be build along this position.
        // TODO : do it the IJ2 way (imageDisplay.getPosition(Axes.Z) does not work).
        int zPosition = IJ.getImage().getZ();

        // Build lines from the ROI
        LinesBuilder linesBuilder = new LinesBuilder(this.roi);
        linesBuilder.build();

        log.info(linesBuilder.getLines().size() + " lines with a width of "
                + linesBuilder.getlineWidth() + " will be used for the kymograph " + this.ID + ".");

        log.info("Creating kymograph for the channel.");
        KymographCreator creator = new KymographCreator(this.context, this.dataset,
                linesBuilder, zPosition);
        creator.build();

        this.kymograph = creator.getProjectedKymograph();
    }
}

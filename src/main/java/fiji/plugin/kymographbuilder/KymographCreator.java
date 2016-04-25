/*
 * The MIT License
 *
 * Copyright 2016 Fiji.
 *
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
 */
package fiji.plugin.kymographbuilder;

import ij.ImagePlus;
import ij.gui.Line;
import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imglib2.RandomAccess;
import net.imglib2.type.Type;
import org.scijava.Context;
import org.scijava.convert.ConvertService;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;

/**
 * The main class that actually build the kymograph for one channel.
 *
 * @author Hadrien Mary <hadrien.mary@gmail.com>
 */
public class KymographCreator {

    @Parameter
    private ConvertService convert;

    @Parameter
    private LogService log;

    @Parameter
    private DatasetService dsService;

    private Dataset dataset;
    private Dataset kymograph;
    private Dataset projectedKymograph;
    private int channel;
    private LinesBuilder linesBuilder;
    private RandomAccess datasetCursor;
    private RandomAccess kymographCursor;

    public KymographCreator(Context context, Dataset dataset,
            int channel, LinesBuilder linesBuilder) {
        
        context.inject(this);
        this.channel = channel;
        this.linesBuilder = linesBuilder;
        this.dataset = dataset;
    }

    public Dataset getKymograph() {
        return this.kymograph;
    }

    public Dataset getProjectedKymograph() {
        return this.projectedKymograph;
    }

    public void build() {

        // Create kymograph dataset
        // A 3D dataset because it contains one kymograph by "width" unit
        long[] dimensions = new long[3];
        dimensions[0] = this.dataset.dimension(this.dataset.dimensionIndex(Axes.TIME));
        dimensions[1] = this.linesBuilder.getTotalLength() - this.linesBuilder.getLines().size() + 1;
        dimensions[2] = this.linesBuilder.getlineWidth();

        AxisType[] axisTypes = new AxisType[3];
        axisTypes[0] = Axes.X;
        axisTypes[1] = Axes.Y;
        axisTypes[2] = Axes.Z;

        String title = dataset.getName() + " (Kymograph)";

        this.kymograph = dsService.create(dimensions, title, axisTypes,
                dataset.getValidBits(), dataset.isSigned(), !dataset.isInteger());

        // Get cursors to access and set pixels.
        this.datasetCursor = this.dataset.getImgPlus().randomAccess();
        this.kymographCursor = this.kymograph.getImgPlus().randomAccess();

        int offset = 0;
        Segment line;
        double[] vectorScaled;
        int length;

        // Iterate over each lines (for polyline) and fill the kymograph dataset.
        for (int i = 0; i < this.linesBuilder.getLines().size(); i++) {

            line = this.linesBuilder.getLines().get(i);
            vectorScaled = this.linesBuilder.getLinesVectorScaled().get(i);
            length = this.linesBuilder.getLinesLength().get(i);

            this.fillKymograph(line, vectorScaled, offset);

            offset += length - 1;
        }

    }

    private < T extends Type< T>> void fillKymograph(Segment line, double[] vectorScaled, int offset) {

        double dx = vectorScaled[0];
        double dy = vectorScaled[1];
        int n;
        int lineWidth = this.linesBuilder.getlineWidth();
        int new_xStart;
        int new_yStart;
        int new_xEnd;
        int new_yEnd;

        int timeDimension = (int) this.dataset.dimension(this.dataset.dimensionIndex(Axes.TIME));

        Line currentLine;

        float[] xpoints;
        float[] ypoints;
        int npoints;
        int x;
        int y;
        
        ImagePlus imp = convert.convert(this.dataset, ImagePlus.class);

        // Iterate over all parallel lines (defined by lineWidth)
        for (int i = 0; i < lineWidth; i++) {

            n = i - lineWidth / 2;
            new_xStart = (int) (line.xStart + n * dy);
            new_yStart = (int) (line.yStart - n * dx);
            new_xEnd = (int) (line.xEnd + n * dy);
            new_yEnd = (int) (line.yEnd - n * dx);

            currentLine = new Line(new_xStart, new_yStart, new_xEnd, new_yEnd);
            currentLine.setStrokeWidth(1);
            imp.setRoi(currentLine);

            xpoints = currentLine.getInterpolatedPolygon().xpoints;
            ypoints = currentLine.getInterpolatedPolygon().ypoints;
            npoints = currentLine.getInterpolatedPolygon().npoints;

            // Iterate over every pixels defining the line
            for (int j = 0; j < npoints - 1; j++) {
                x = Math.round(xpoints[j]);
                y = Math.round(ypoints[j]);

                // Iterate over the time axis
                for (int t = 0; t < timeDimension; t++) {
                    // TODO : Build position according to dataset dimension indexes
                    this.datasetCursor.setPosition(new int[]{x, y, this.channel, t});
                    this.kymographCursor.setPosition(new int[]{t, offset + j, i});
                    final T pixel = (T) this.kymographCursor.get();
                    pixel.set((T) this.datasetCursor.get());
                }

            }

        }

    }

}

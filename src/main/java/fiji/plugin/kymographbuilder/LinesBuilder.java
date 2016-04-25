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

import ij.gui.Line;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hadrien Mary
 */
public class LinesBuilder {

    private Roi roi;

    List<Segment> lines;
    private int lineWidth;
    private List<Integer> linesLength;
    private List<double[]> linesVectorScaled;
    private int totalLength;

    public LinesBuilder(Roi roi) {
        this.roi = roi;
    }

    public List<Segment> getLines() {
        return this.lines;
    }

    public List<Integer> getLinesLength() {
        return this.linesLength;
    }

    public List<double[]> getLinesVectorScaled() {
        return this.linesVectorScaled;
    }

    public int getTotalLength() {
        return this.totalLength;
    }

    public int getlineWidth() {
        return this.lineWidth;
    }

    public void build() {
        this.buildLines();
        this.buildVector();

        this.lineWidth = Math.round(roi.getStrokeWidth());
        this.lineWidth = this.lineWidth < 1 ? 1 : this.lineWidth;
    }

    private void buildLines() {

        lines = new ArrayList<>();

        if (this.roi.getTypeAsString().equals("Straight Line")) {
            Line lineRoi = (Line) this.roi;
            Segment line = new Segment(lineRoi.x1, lineRoi.y1, lineRoi.x2, lineRoi.y2);
            lines.add(line);

        } else {
            PolygonRoi roiPoly = (PolygonRoi) this.roi;

            int xStart;
            int yStart;
            int xEnd;
            int yEnd;

            for (int i = 0; i < roiPoly.getPolygon().npoints - 1; i++) {
                xStart = roiPoly.getPolygon().xpoints[i];
                yStart = roiPoly.getPolygon().ypoints[i];
                xEnd = roiPoly.getPolygon().xpoints[i + 1];
                yEnd = roiPoly.getPolygon().ypoints[i + 1];
                Segment line = new Segment(xStart, yStart, xEnd, yEnd);
                lines.add(line);
            }
        }
    }

    private void buildVector() {

        this.linesLength = new ArrayList<>();
        this.linesVectorScaled = new ArrayList<>();

        totalLength = 0;
        int length;

        for (Segment line : this.lines) {
            length = (int) Math.round(line.getLength());
            this.linesLength.add(length);
            this.linesVectorScaled.add(line.getScaledVector());
            totalLength += length;
        }

    }
}

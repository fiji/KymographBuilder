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
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.axis.Axes;
import org.scijava.convert.ConvertService;
import org.scijava.ui.DialogPrompt.MessageType;
import org.scijava.ui.DialogPrompt.OptionType;
import org.scijava.ui.DialogPrompt.Result;
import static org.scijava.ui.DialogPrompt.Result.YES_OPTION;
import org.scijava.ui.UIService;

/**
 *
 * @author Hadrien Mary <hadrien.mary@gmail.com>
 */
public class Utils {

    /**
     * Return some common informations about a dataset.
     *
     * @param dataset
     * @param prefix
     * @return
     */
    public static String getInfo(Dataset dataset, String prefix) {
        String s = new String();

        s += prefix + "Dataset : " + dataset.toString() + "\n";

        s += prefix + "Width = ";
        s += dataset.dimension(dataset.dimensionIndex(Axes.X)) + "\n";

        s += prefix + "Height = ";
        s += dataset.dimension(dataset.dimensionIndex(Axes.Y)) + "\n";

        s += prefix + "Depth = ";
        s += dataset.dimension(dataset.dimensionIndex(Axes.Z)) + "\n";

        s += prefix + "Timepoints = ";
        s += dataset.dimension(dataset.dimensionIndex(Axes.TIME)) + "\n";

        s += prefix + "Number of channels = ";
        s += dataset.dimension(dataset.dimensionIndex(Axes.CHANNEL)) + "\n";

        return s;
    }

    public static String getInfo(Dataset dataset) {
        return getInfo(dataset, "");
    }

    /**
     * Check if Z and Time dimensions should be swapped in a given dataset. If it does then ask user
     * if he wants to swap them.
     *
     * @param ij
     * @param dataset
     */
    public static void askToSwapTimeAndZDimensions(ImageJ ij, Dataset dataset) {

        int zIdx = dataset.dimensionIndex(Axes.Z);
        int timeIdx = dataset.dimensionIndex(Axes.TIME);

        long timeDim = dataset.dimension(timeIdx);
        long zDim = dataset.dimension(zIdx);

        if (timeDim < zDim) {
            String mess = new String();
            mess += "It appears this image has " + timeDim + " timepoints";
            mess += " and " + zDim + " Z slices.\n";
            mess += "Do you want to swap Z and T axes ?";
            Result result = ij.ui().showDialog(mess,
                    MessageType.QUESTION_MESSAGE,
                    OptionType.YES_NO_OPTION);
            result.equals(YES_OPTION);

            if (result.equals(YES_OPTION)) {
                if (zIdx != -1) {
                    dataset.axis(zIdx).setType(Axes.TIME);
                }
                if (timeIdx != -1) {
                    dataset.axis(timeIdx).setType(Axes.Z);
                }
            }

        }

    }

    /**
     * Check if Z and Time dimensions should be swapped in a given dataset. If it does then swap
     * them without asking.
     *
     * @param ij
     * @param dataset
     */
    public static void swapTimeAndZDimensions(ImageJ ij, Dataset dataset) {

        int zIdx = dataset.dimensionIndex(Axes.Z);
        int timeIdx = dataset.dimensionIndex(Axes.TIME);

        long timeDim = dataset.dimension(timeIdx);
        long zDim = dataset.dimension(zIdx);

        if (timeDim < zDim) {
            if (zIdx != -1) {
                dataset.axis(zIdx).setType(Axes.TIME);
            }
            if (timeIdx != -1) {
                dataset.axis(timeIdx).setType(Axes.Z);
            }
        }

    }

    static Roi checkForROIs(Dataset dataset, ConvertService convert, UIService ui) {
        ImagePlus imp = convert.convert(dataset, ImagePlus.class);
        Roi roi = imp.getRoi();

        if (roi == null) {
            // Look in ROI manager
            RoiManager rm = RoiManager.getRoiManager();
            roi = rm.getRoi(0);
        }

        if (roi == null) {
            ui.showDialog("Please define a line in order to build the kymograph.");
            return null;
        }

        if (!"Straight Line".equals(roi.getTypeAsString())
                && !"Polyline".equals(roi.getTypeAsString())) {
            ui.showDialog("Please use the Straight Line or Segmented Line selection tool.");
            return null;
        }

        return roi;
    }

}

# @Context context
# @Dataset dataset
# @ImageJ ij
# @LogService log


import ij.plugin.frame.RoiManager as RM
import fiji.plugin.kymographbuilder.KymographFactory as KFactory


def validDataset(dataset):
    """Assess if dataset has suitable dimensions"""
    from net.imagej.axis import Axes
    z = dataset.dimension(dataset.dimensionIndex(Axes.Z))
    t = dataset.dimension(dataset.dimensionIndex(Axes.TIME))
    return z * t > 1


rm = RM.getInstance()
counter = 0
if validDataset(dataset) and rm and rm.getCount():
    for roi in rm.getRoisAsArray():
        if roi.isLine():
            kfactory = KFactory(context, dataset, roi)
            kfactory.build()
            counter += 1
            title = "Kymograph" + str(counter).zfill(3) + "_" + roi.getName()
            ij.ui().show(title, kfactory.getKymograph())
    log.info("MultiKymographBuilder Finished. " + str(counter) + " ROIs processed")
else:
    log.error("Either the ROI Manager is empty or " + dataset.getName() +" has invalid dimensions")

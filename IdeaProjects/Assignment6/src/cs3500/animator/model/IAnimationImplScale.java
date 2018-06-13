package cs3500.animator.model;

/**
 * Represents an IAnimation that changes the dimensions of an IFigure.
 */
public class IAnimationImplScale extends IAnimationAbstract implements IAnimation {
  private double xFactor;
  private double yFactor;
  private String type = "scales";

  /**
   * Constructor for an IAnimation that changes the color of the IFigure.
   *
   * @param model     The current IModel.
   * @param fig       The name of the IFigure to which this IAnimation will be applied.
   * @param xFactor   The scale in x dimensions that should be applied in this IAnimation.
   * @param yFactor   The scale in y dimensions that should be applied in this IAnimation.
   * @param startTime The start time of this IAnimation.
   * @param endTime   The end time of this IAnimation.
   * @throws IllegalArgumentException if the given scaling factors are less than or equal to 0.
   */
  public IAnimationImplScale(IModel model, String fig, double xFactor, double yFactor,
                             int startTime, int endTime) throws IllegalArgumentException {
    super();
    super.model = model;
    super.fig = model.getFigure(fig);
    super.type = type;
    super.startTime = startTime;
    super.endTime = endTime;
    // can't use 0 to make it "disappear" earlier because it will make the dims 0
    if (xFactor <= 0 || yFactor <= 0) {
      throw new IllegalArgumentException("Invalid scaling factor given.");
    }
    this.xFactor = xFactor;
    this.yFactor = yFactor;
    checkTimes();
  }

  @Override
  public double[] getNewColor() {
    return new double[0];
  }

  @Override
  public double getDX() {
    return 0;
  }

  @Override
  public double getDY() {
    return 0;
  }

  @Override
  public double getXFactor() {
    double copy = this.xFactor;
    return copy;
  }

  @Override
  public double getYFactor() {
    double copy = this.yFactor;
    return copy;
  }

  // make sure this gets updated if a circle with just a radius is added.
  @Override
  public IFigure accept(IFigure f) {
    return f.updateDims(this.xFactor, this.yFactor);
  }
}

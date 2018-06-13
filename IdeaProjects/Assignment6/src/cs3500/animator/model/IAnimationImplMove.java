package cs3500.animator.model;

/**
 * Represents an IAnimation that changes the position of an IFigure.
 */
public class IAnimationImplMove extends IAnimationAbstract implements IAnimation {
  private double dx;
  private double dy;
  private String type = "moves";

  /**
   * Constructor for an IAnimation that changes the color of the IFigure.
   *
   * @param model     The current IModel.
   * @param fig       The name of the IFigure to which this IAnimation will be applied.
   * @param dx        The change in x position that should be applied in this IAnimation.
   * @param dy        The change in y position that should be applied in this IAnimation.
   * @param startTime The start time of this IAnimation.
   * @param endTime   The end time of this IAnimation.
   * @throws IllegalArgumentException if the given start or end times are invalid.
   */
  public IAnimationImplMove(IModel model, String fig, double dx, double dy, int startTime,
                            int endTime) throws IllegalArgumentException {
    super();
    super.model = model;
    super.fig = model.getFigure(fig);
    this.dx = dx;
    this.dy = dy;
    super.type = type;
    super.startTime = startTime;
    super.endTime = endTime;
    checkTimes();
  }

  @Override
  public double[] getNewColor() {
    return new double[0];
  }

  @Override
  public double getDX() {
    double copy = this.dx;
    return copy;
  }

  @Override
  public double getDY() {
    double copy = this.dy;
    return copy;
  }

  @Override
  public double getXFactor() {
    return 0;
  }

  @Override
  public double getYFactor() {
    return 0;
  }

  @Override
  public IFigure accept(IFigure f) {
    return f.updatePosn(this.dx, this.dy);
  }
}

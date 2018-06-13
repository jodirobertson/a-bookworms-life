package cs3500.animator.model;

/**
 * Represents an IAnimation that changes the color of an IFigure.
 */
public class IAnimationImplColorChange extends IAnimationAbstract implements IAnimation {
  private String type = "changes color";
  private double[] newColor;

  /**
   * Constructor for an IAnimation that changes the color of the IFigure.
   *
   * @param model     The current IModel.
   * @param fig       The name of the IFigure to which this IAnimation will be applied.
   * @param newColor  The new color desired in this IAnimation.
   * @param startTime The start time of this IAnimation.
   * @param endTime   The end time of this IAnimation.
   * @throws IllegalArgumentException if the doubles for the given color input are not between 0 and
   *                                  1, inclusive or if the start and end times are invalid.
   */
  public IAnimationImplColorChange(IModel model, String fig, double[] newColor, int startTime,
                                   int endTime) throws IllegalArgumentException {
    super();
    super.model = model;
    super.startTime = startTime;
    super.endTime = endTime;
    super.fig = model.getFigure(fig);
    super.type = type;
    checkTimes();

    for (double d : newColor) {
      if (d < 0 || d > 1) {
        throw new IllegalArgumentException("Not a valid color input.");
      }
      this.newColor = newColor;
    }
  }

  @Override
  public double[] getNewColor() {
    double[] copy = this.newColor;
    return copy;
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
    return 0;
  }

  @Override
  public double getYFactor() {
    return 0;
  }

  @Override
  public IFigure accept(IFigure f) {
    return f.updateColor(this.newColor);
  }
}

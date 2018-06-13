package cs3500.animator.model;

/**
 * Represents common information and methods shared by all IAnimations.
 */
abstract class IAnimationAbstract implements IAnimation {
  int startTime;
  int endTime;
  String type;
  IFigure fig;
  IModel model;

  /**
   * Default constructor for an IAnimationAbstract.
   */
  IAnimationAbstract() {
    //default constuctor
  }

  /**
   * Ensures start and end times are greater than 0, and that the end time is greater than the start
   * time.
   *
   * @throws IllegalArgumentException if the times given are invalid.
   */
  void checkTimes() throws IllegalArgumentException {
    if (startTime >= endTime || startTime < 0) {
      throw new IllegalArgumentException("Start and end times don't make sense.");
    }
  }

  @Override
  public IFigure getFigure() {
    IFigure copy = this.fig;
    return copy;
  }

  @Override
  public String getAnimType() {
    String copy = this.type;
    return copy;
  }

  @Override
  public int getStartTime() {
    return startTime;
  }

  @Override
  public int getEndTime() {
    return endTime;
  }

}

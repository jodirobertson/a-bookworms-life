package cs3500.animator.model;

/**
 * Represents any animation and the methods that may be called on them.
 */
public interface IAnimation {

  /**
   * Gets the starting time of the IAnimation.
   *
   * @return Returns the start time of this IAnimation.
   */
  int getStartTime();

  /**
   * Gets the ending time of the IAnimation.
   *
   * @return Returns the end time of this IAnimation.
   */
  int getEndTime();

  /**
   * Gets the new color being applied to an IFigure in a color change animation.
   *
   * @return the new color to be applied to an IFigure.
   */
  double[] getNewColor();

  /**
   * Gets the new dx being applied to an IFigure in a scaling animation.
   *
   * @return the dx to be applied to an IFigure.
   */
  double getDX();

  /**
   * Gets the new dy being applied to an IFigure in a scaling animation.
   *
   * @return the dy to be applied to an IFigure.
   */
  double getDY();

  /**
   * Gets the new x-factor being applied to an IFigure in a scaling animation.
   *
   * @return the x-factor to be applied to an IFigure.
   */
  double getXFactor();

  /**
   * Gets the new y-factor being applied to an IFigure in a scaling animation.
   *
   * @return the y-factor to be applied to an IFigure.
   */
  double getYFactor();

  /**
   * Gets the type of IAnimation.
   *
   * @return the type of IAnimation.
   */
  String getAnimType();

  /**
   * Gets the figure to which an IAnimation is being applied.
   *
   * @return The IFigure of the IAnimation.
   */
  IFigure getFigure();

  /**
   * Processes a figure that is having an animation applied to it, and uses the information from the
   * original figure to return a figure that is updated accroding to the animation.
   *
   * @param f The IFigure that is having an animation applied to it.
   * @return The updated IFigure that has had the changes of an IAnimation applied to it.
   */
  IFigure accept(IFigure f);
}

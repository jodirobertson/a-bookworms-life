package cs3500.animator.model;

/**
 * Represents actions that can be enacted on an IFigure.
 */
public interface IFigure {

  /**
   * Gets the desired name of the IFigure.
   *
   * @return A String representing the name of the IFigure.
   */
  String getName();

  /**
   * Gets the appearance time of the IFigure.
   *
   * @return Returns the time the IFigure appears.
   */
  int getAppearTime();

  /**
   * Gets the disappearance time of the IFigure.
   *
   * @return Returns the time the IFigure disappears.
   */
  int getDisappearTime();

  /**
   * Gets the color of the IFigure.
   *
   * @return Returns the color of the IFigure.
   */
  double[] getColor();

  /**
   * Gets the x-position of the IFigure.
   *
   * @return Returns the xPos of the IFigure.
   */
  double getXPos();

  /**
   * Gets the y-position of the IFigure.
   *
   * @return Returns the yPos of the IFigure.
   */
  double getYPos();

  /**
   * Gets the x-dimension of the IFigure.
   *
   * @return Returns the xDim of the IFigure.
   */
  double getXDim();

  /**
   * Gets the y-dimension of the IFigure.
   *
   * @return Returns the yDim of the IFigure.
   */
  double getYDim();

  /**
   * Returns the type of shape of the IFigure.
   *
   * @return The String representing the type of shape of the IFigure.
   */
  String getShape();

  /**
   * Updates the positioning of an IFigure after a IAnimationImplMove IAnimation has been called.
   *
   * @param dx The change in x of the new position.
   * @param dy The change in y of the new position.
   * @return Returns an IFigure with the new desired position.
   */
  IFigure updatePosn(double dx, double dy);

  /**
   * Updates the positioning of an IFigure after a IAnimationImplColorChange IAnimation has been
   * called.
   *
   * @param newColor The desired new color of the IFigure.
   * @return Returns an IFigure with the new desired color.
   */
  IFigure updateColor(double[] newColor);

  /**
   * Updates the dimensions of an IFigure after a IAnimationImplScale IAnimation has been called.
   *
   * @param xFactor The factor of scaling to be applied in the x dimension.
   * @param yFactor The factor of scaling to be applied in the y dimension.
   * @return Returns an IFigure with the new scaled dimensions.
   */
  IFigure updateDims(double xFactor, double yFactor);

  /**
   * Applies the given IAnimation to this IFigure, updated in the returned IFigure.
   *
   * @param a The IAnimation to be applied to this IFigure.
   * @return The updated IFigure according to the IAnimation applied.
   */
  IFigure apply(IAnimation a);
}

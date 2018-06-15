package cs3500.animator.model;

// Update: combined IFigureImplOval and IFigureImplDummy to become IFigureImplDummy from class
// discussion suggesting that all shapes can be represented in terms of common data. The
// distinction is then the added field String shapeName.

/**
 * Represents the shared information among figures.
 */
public class IFigureImplDummy implements IFigure {
  private IModel model;
  private double[] color;
  private String name;
  private String shapeName;
  private double xPos;
  private double yPos;
  private int appearsAt;
  private int disappearsAt;
  private double xDim;
  private double yDim;

  /**
   * Represents an instance of an IFigure IFigureImplOval.
   *
   * @param model        The current IModel.
   * @param color        The color of the IFigure.
   * @param name         The name given to the IFigure.
   * @param xPos         The center x-coordinate of the IFigure.
   * @param yPos         The center y-coordinate of the IFigure.
   * @param appearsAt    The time at which the IFigure appears on the scene.
   * @param disappearsAt The time at which the IFigure disappears from the scene.
   * @param xDim         The x-dimension of the oval.
   * @param yDim         The y-dimension of the oval.
   * @throws IllegalArgumentException if any of the specified parameters are invalid.
   */
  public IFigureImplDummy(IModel model, String shapeName, double[] color, String name, double xPos,
                          double yPos, int appearsAt, int disappearsAt, double xDim, double yDim)
          throws IllegalArgumentException {
    this.model = model;
    this.shapeName = shapeName;
    this.color = color;
    this.name = name;
    this.xPos = xPos;
    this.yPos = yPos;
    this.appearsAt = appearsAt;
    this.disappearsAt = disappearsAt;
    this.xDim = xDim;
    this.yDim = yDim;
    checkConstructor();
  }

  /**
   * Ensures that the appear and disappear times are greater than zero, and that the disappear time
   * is greater than the appear time. It also ensures that the doubles given as the color are in the
   * range of 0 to 1, inclusive. The dimensions are also required to be greater than zero.
   *
   * @throws IllegalArgumentException if either the given input to build an IFigure is invalid.
   */
  void checkConstructor() throws IllegalArgumentException {
    // checks appear/disappear
    if (appearsAt >= disappearsAt || appearsAt < 0) {
      throw new IllegalArgumentException("Appear and disappears values don't make sense.");
    }
    // checks dimensions -> allowing 0 as input makes it impossible to change size
    if (xDim <= 0 || yDim <= 0) {
      throw new IllegalArgumentException("X-dimension and Y-dimension must be " +
              "non-negative and non-zero.");
    }
    // checks color
    for (double d : color) {
      if (d < 0 || d > 1) {
        throw new IllegalArgumentException("Not a valid color input.");
      }
    }
  }

  @Override
  public int getAppearTime() {
    int copy = this.appearsAt;
    return copy;
  }

  @Override
  public int getDisappearTime() {
    int copy = this.disappearsAt;
    return copy;
  }

  @Override
  public String getName() {
    String copy = this.name;
    return copy;
  }

  @Override
  public double[] getColor() {
    double[] copy = this.color;
    return copy;
  }

  @Override
  public double getXPos() {
    double copy = this.xPos;
    return copy;
  }

  @Override
  public double getYPos() {
    double copy = this.yPos;
    return copy;
  }

  @Override
  public double getXDim() {
    double copy = this.xDim;
    return copy;
  }

  @Override
  public double getYDim() {
    double copy = this.yDim;
    return copy;
  }

  @Override
  public String getShape() {
    String copy = this.shapeName;
    return copy;
  }

  @Override
  public IFigure updatePosn(double dx, double dy) {
    double oldX = this.xPos;
    double oldY = this.yPos;
    double newX = oldX + dx;
    double newY = oldY + dy;
    return new IFigureImplDummy(model, shapeName, color, name, newX, newY, appearsAt, disappearsAt,
            xDim, yDim);
  }

  @Override
  public IFigure updateColor(double[] newColor) {
    return new IFigureImplDummy(model, shapeName, newColor, name, xPos, yPos, appearsAt, disappearsAt,
            xDim, yDim);
  }

  @Override
  public IFigure updateDims(double xFactor, double yFactor) {
    double oldXDim = this.xDim;
    double oldYDim = this.yDim;
    double newXDim = oldXDim * xFactor;
    double newYDim = oldYDim * yFactor;
    return new IFigureImplDummy(model, shapeName, color, name, xPos, yPos, appearsAt, disappearsAt,
            newXDim, newYDim);
  }

  @Override
  public IFigure apply(IAnimation a) {
    return a.accept(this);
  }
}

package cs3500.animator.view;

import java.util.List;

import cs3500.animator.model.IAnimation;
import cs3500.animator.model.IFigure;
import cs3500.animator.model.IModel;

/**
 * Represents an IView that produces an output of the composition in an SVG file format.
 */
public class IViewImplSVGAnimation extends IViewAbstract implements IView {
  private Appendable output;
  private int screenWidth;
  private int screenHeight;

  /**
   * Constructs an IView using SVG file formatting, given an IModel, tick, dimensions for the
   * composition window, and an output.
   */
  public IViewImplSVGAnimation(IModel model, Appendable output, double tick,
                               int screenWidth, int screenHeight) {
    super();
    super.model = model;
    super.tick = tick;
    this.output = output;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }

  /**
   * Converts colors represented in values 0-1 to a value 0-255.
   *
   * @param color the current color that should be updated.
   * @return Returns a formatted String with the colors in 0-255 rgb format.
   */
  private String convertColor(double[] color) {
    String rgb = "rgb(";
    rgb = rgb + color[0] * 255 + "," + color[1] * 255 + "," + color[2] * 255 + ")";
    return rgb;
  }

  /**
   * Writes the first line of code in the SVG output according to desired given width and height of
   * the window.
   */
  private void firstLine() {
    String firstL = "<svg width=\"" + screenWidth + "\" height=\"" + screenHeight
            + "\" version=\"1.1\"\n" + "     xmlns=\"http://www.w3.org/2000/svg\">\n";
    appendHelper(this.output, firstL);
  }

  /**
   * Writes the last line to close out the SVG file.
   */
  private void lastLine() {
    String lastL = "</svg>";
    appendHelper(this.output, lastL);
  }

  /**
   * Writes the first line that adds a rectangle to the SVG output.
   *
   * @param fig The given IFigure that is to be added to the SVG output.
   */
  private void openRectangle(IFigure fig) {
    String addRect = "<rect id=\"" + fig.getName() + "\" x=\"" + fig.getXPos() + "\" y=\"" + fig.getYPos()
            + "\" width=\"" + fig.getXDim() + "\" height=\"" + fig.getYDim() + "\" \n" +
            "fill=\"" + convertColor(fig.getColor()) + "\" visibility=\"visible\" >\n";

    appendHelper(this.output, addRect);
  }

  /**
   * Writes the last line of the rectangle representation in SVG format.
   */
  private void closeRectangle() {
    appendHelper(this.output, "</rect>\n");
  }

  /**
   * Writes the first line of adding an oval to the SVG output.
   */
  private void openOval(IFigure fig) {
    String addOval = "<ellipse id=\"" + fig.getName() + "\" cx=\"" + fig.getXPos() + "\" cy=\"" + fig.getYPos()
            + "\" rx=\"" + fig.getXDim() + "\" ry=\"" + fig.getYDim() + "\" \n" +
            "fill=\"" + convertColor(fig.getColor()) + "\" visibility=\"visible\" >\n";
    appendHelper(this.output, addOval);
  }

  /**
   * Writes the last line of the oval shape addition to the SVG output.
   */
  private void closeOval() {
    appendHelper(this.output, "</ellipse>\n");
  }

  /**
   * Formats the start time of the animation according to SVG format.
   *
   * @param a The IAnimation that is having its start and end times converted.
   * @return Returns a String of the start time in SVG format.
   */
  private String formatStartTime(IAnimation a) {
    return super.f.format(a.getStartTime() * 1000 / tick);
  }

  /**
   * Formats the end time of the animation according to SVG format.
   *
   * @param a The IAnimation that is having its start and end times converted.
   * @return Returns a String of the end time in SVG format.
   */
  private String formatDuration(IAnimation a) {
    return super.f.format((a.getEndTime() - a.getStartTime()) * 1000 / tick);
  }

  /**
   * Writes the animation of changing color into the SVG output.
   *
   * @param a The IAnimationImplColorChange to be converted to SVG format.
   */
  private void animateColorChange(IAnimation a) {

    String addAnim = "<animate attributeType=\"xml\" begin=\"" + formatStartTime(a) + "ms\" " +
            "dur=\"" + formatDuration(a) + "ms\" \nattributeName=\"color\" from=\""
            + convertColor(a.getFigure().getColor()) + "\" to=\"" + convertColor(a.getNewColor())
            + "\" fill=\"remove\" />\n";

    appendHelper(this.output, addAnim);
  }

  /**
   * Writes the animation of scaling into the SVG output.
   *
   * @param a The IAnimation being translated to SVG format.
   * @param xy The dimension that is being scaled.
   */
  private void animateScale(IAnimation a, char xy) {
    String DimName;
    String sub1;
    String sub2;

    // assigns name inputs for animation
    switch (a.getFigure().getShape()) {
      case "oval":
        if (xy == 'x') {
          DimName = "rx";
          sub1 = "" + a.getFigure().getXDim();
          sub2 = "" + a.getFigure().updateDims(a.getXFactor(), a.getYFactor()).getXDim();
        } else {
          DimName = "ry";
          sub1 = "" + a.getFigure().getYDim();
          sub2 = "" + a.getFigure().updateDims(a.getXFactor(), a.getYFactor()).getYDim();
        }
        break;
      case "rectangle":
        if (xy == 'x') {
          DimName = "width";
          sub1 = "" + a.getFigure().getXDim();
          sub2 = "" + a.getFigure().updateDims(a.getXFactor(), a.getYFactor()).getXDim();
        } else {
          DimName = "height";
          sub1 = "" + a.getFigure().getYDim();
          sub2 = "" + a.getFigure().updateDims(a.getXFactor(), a.getYFactor()).getYDim();
        }
        break;
      default:
        DimName = "error";
        sub1 = "";
        sub2 = "";
    }

    String addAnim = "<animate attributeType=\"xml\" begin=\"" + formatStartTime(a) + "ms\" " +
            "dur=\"" + formatDuration(a) + "ms\" \nattributeName=\"" + DimName + "\" from=\""
            + sub1 + "\" to=\"" + sub2 + "\" fill=\"remove\" />\n";

    appendHelper(this.output, addAnim);
  }

  /**
   * Writes the animation of moving an object into the SVG output.
   *
   * @param a The IAnimation to be transcribed in SVG format.
   * @param xy The dimension upon which it is acting.
   */
  private void animateMove(IAnimation a, char xy) {
    String attName;
    String sub1;
    String sub2;

    if (xy == 'x') {
      attName = "cx";
      sub1 = "" + a.getFigure().getXPos();
      sub2 = "" + a.getFigure().updatePosn(a.getDX(), a.getDY()).getXPos();
    } else {
      attName = "cy";
      sub1 = "" + a.getFigure().getYPos();
      sub2 = "" + a.getFigure().updatePosn(a.getDX(), a.getDY()).getYPos();
    }
    String addAnim = "<animate attributeType=\"xml\" begin=\"" + formatStartTime(a) + "ms\" " +
            "dur=\"" + formatDuration(a) + "ms\" \nattributeName=\"" + attName + "\" from=\""
            + sub1 + "\" to=\"" + sub2 + "\" fill=\"remove\" />\n";

    appendHelper(this.output, addAnim);
  }

  /**
   * Checks whether an apparent animation is occuring, and then delegates to animateMove to write
   * it to the SVG output.
   *
   * @param a The IAnimation to be written into SVG format.
   */
  private void checkMoveAndExecute(IAnimation a) {
    if (!(a.getDX() == 0)) {
      animateMove(a, 'x');
    }
    if (!(a.getDY() == 0)) {
      animateMove(a, 'y');
    }
  }

  /**
   * Checks whether an apparent animation is occuring, and then delegates to animateScale to write
   * it to the SVG output.
   *
   * @param a The IAnimation to be written into SVG format.
   */
  private void checkScaleAndExecute(IAnimation a) {
    if (!(a.getXFactor() == 1)) {
      animateScale(a, 'x');
    }
    if (!(a.getYFactor() == 1)) {
      animateScale(a, 'y');
    }
  }

  /**
   * Delegates the transcribing of each IAnimation to the appropriate helpers.
   *
   * @param a The IAnimation to be transcribed.
   */
  private void delegateAnimPrint(IAnimation a) {
    switch (a.getAnimType()) {
      case "moves":
        checkMoveAndExecute(a);
        break;
      case "color changes":
        animateColorChange(a);
        break;
      case "scales":
        checkScaleAndExecute(a);
        break;
      default:
    }
  }

  @Override
  public void play() {
    // a list of animations that occur to a particular IFigure (as referenced by its unique name)
    List<IAnimation> animByShape;

    firstLine();

    // writes all figures into SVG format
    for (IFigure fig : super.model.getFiguresByStartTime()) {
      switch (fig.getShape()) {
        case "oval":
          openOval(fig);
          // writes all animations on figure in between open and close shape code
          animByShape = super.model.getAnimationsByShape().get(fig.getName());
          for (IAnimation a : animByShape) {
            delegateAnimPrint(a);
          }
          closeOval();
          break;
        case "rectangle":
          openRectangle(fig);
          // writes all animations on figure in between open and close shape code
          animByShape = super.model.getAnimationsByShape().get(fig.getName());
          for (IAnimation a : animByShape) {
            delegateAnimPrint(a);
          }
          closeRectangle();
          break;
        default:
          return;
      }
    }
    lastLine();
  }
}

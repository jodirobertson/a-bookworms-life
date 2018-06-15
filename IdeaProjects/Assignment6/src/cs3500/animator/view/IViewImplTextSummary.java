package cs3500.animator.view;

import cs3500.animator.model.IAnimation;
import cs3500.animator.model.IFigure;
import cs3500.animator.model.IModel;

/**
 * Represents a view that outputs a text summary of the animation created.
 */
public class IViewImplTextSummary extends IViewAbstract implements IView {
  private Appendable output;

  /**
   * Constructs a view that outputs a summary of text that describes all of the shapes in the
   * composition and what animations are applied to them, in the order they occur. The tick must be
   * greater than zero.
   *
   * @param output The specified Appendable that will display the view.
   */
  public IViewImplTextSummary(IModel model, Appendable output, double tick) {
    super();
    super.model = model;
    this.output = output;
    super.tick = tick;
    checkTick();
    ensureNotNull(model);
    ensureNotNull(output);
  }

  /**
   * Prints a description of the IFigure's color.
   *
   * @param fig Takes in the IFigure to be printed.
   * @return Returns a String detailing the original given color of the IFigure.
   */
  private String printColor(IFigure fig) {
    return "(" + f.format(fig.getColor()[0])
            + "," + f.format(fig.getColor()[1]) + ","
            + f.format(fig.getColor()[2]) + ")";
  }

  /**
   * Prints a description of the IFigure's dimensions.
   *
   * @param fig Takes in the IFigure to be printed.
   * @return Returns a String detailing the dimensions of the IFigure.
   */
  private String printDims(IFigure fig) {
    if (fig.getShape().equals("oval")) {
      return "X radius: " + f.format(fig.getXDim())
              + ", Y radius: " + f.format(fig.getYDim());
    } else {
      return "Width: " + f.format(fig.getXDim())
              + ", Height: " + f.format(fig.getYDim());
    }
  }

  /**
   * Prints a description of the IFigure's position.
   *
   * @param fig Takes in the IFigure to be printed.
   * @return Returns a String detailing the position of the IFigure.
   */
  private String printPosn(IFigure fig) {
    return "(" + fig.getXPos() + "," + fig.getYPos() + ") ";
  }

  /**
   * Prints a summary of when the IFigure appears and disappears.
   *
   * @param fig Takes in the IFigure to be printed.
   * @return Returns a String detailing when the IFigure appears and disappears.
   */
  private String printFigTimes(IFigure fig) {
    return "\nAppears at t=" + f.format(fig.getAppearTime() / super.tick)
            + "s\nDisappears at t=" + f.format(fig.getDisappearTime() / super.tick) + "s";
  }

  /**
   * Prints a summary of the name and type of the IFigure.
   *
   * @param fig Takes in the IFigure to be printed.
   * @return Returns a String detailing the name and type of the IFigure.
   */
  private String printNameAndType(IFigure fig) {
    return "Name: " + fig.getName() + "\nType: " + fig.getShape();
  }

  /**
   * Prints a summary of the IFigure.
   *
   * @param fig Takes in the IFigure to be printed.
   * @return A String detailing all information about the IFigure.
   */
  private String printFigure(IFigure fig) {
    if (fig.getShape().equals("oval")) {
      return printNameAndType(fig)
              + "\nCenter: (" + f.format(fig.getXPos()) + "," + f.format(fig.getYPos())
              + "), " + printDims(fig) + ", "
              + "Color: " + printColor(fig) + printFigTimes(fig);
    } else { // rectangle (update if more shapes are added)
      return printNameAndType(fig)
              + "\nCorner: (" + f.format(fig.getXPos()) + "," + f.format(fig.getYPos())
              + "), " + printDims(fig) + ", "
              + "Color: " + printColor(fig) + printFigTimes(fig);
    }
  }

  /**
   * Prints a summary of all the original figures in the scene, ordered by when they appear first.
   *
   * @return A String detailing the figures in the scene as they first appear.
   */
  private String printFigures() {
    String sum = "";
    for (IFigure f : super.model.getFiguresByStartTime()) {
      sum = sum + "\n" + printFigure(f) + "\n";
    }
    return sum;
  }

  /**
   * Formats a summary of the starting and ending times of an animation.
   *
   * @return A String detailing the start and end times of the IAnimation.
   */
  private String printAnimTimes(IAnimation a) {
    return "from t=" + f.format(a.getStartTime() / super.tick) + "s to t="
            + f.format(a.getEndTime() / super.tick) + "s";
  }


  /**
   * Prints a summary of the animation for color changes.
   *
   * @param a Takes in the IAnimation to be printed.
   * @return A String detailing the information about the given IAnimation.
   */
  private String printColorChange(IAnimation a) {
    return a.getAnimType() + " from " + printColor(a.getFigure()) + " to "
            + printColor(a.getFigure().updateColor(a.getNewColor())) + " " + printAnimTimes(a);
  }


  /**
   * Prints a summary of the animation for moves.
   *
   * @param a Takes in the IAnimation to be printed.
   * @return A String detailing the information about the given IAnimation.
   */
  private String printMove(IAnimation a) {
    return a.getAnimType() + " from " + printPosn(a.getFigure()) + "to "
            + printPosn(a.getFigure().updatePosn(a.getDX(), a.getDY())) + printAnimTimes(a);
  }

  /**
   * Prints a summary of the animation for scaling.
   *
   * @param a Takes in the IAnimation to be printed.
   * @return A String detailing the information about the given IAnimation.
   */
  private String printScale(IAnimation a) {
    return a.getAnimType() + " from " + printDims(a.getFigure()) + " to "
            + printDims(a.getFigure().updateDims(a.getXFactor(), a.getYFactor()))
            + " " + printAnimTimes(a);
  }

  /**
   * Prints a summary detailing the IAnimation according to which type of IAnimation it is.
   *
   * @param a The IAnimation to be printed.
   * @return A String detailing the entire information about an IAnimation.
   */
  private String printAnimation(IAnimation a) {
    switch (a.getAnimType()) {
      case "moves":
        return printMove(a);
      case "changes color":
        return printColorChange(a);
      case "scales":
        return printScale(a);
      default:
        return "";
    }
  }

  /**
   * Creates a summary block of the series of animations which occur in this model, ordered by the
   * start time of the animation.
   *
   * @return Returns a String of the animations occurring in the model.
   */
  private String printAnimations() {
    String sum = "";
    for (IAnimation a : super.model.getAnimationsByStartTime()) {
      sum = sum + "\n" + "Shape " + super.model.getMasterList().get(a) + " "
              + printAnimation(a);
    }
    return sum;
  }

  @Override
  public void play() throws IllegalArgumentException {
    appendHelper(this.output, "Shapes:" + printFigures() + printAnimations());
  }
}

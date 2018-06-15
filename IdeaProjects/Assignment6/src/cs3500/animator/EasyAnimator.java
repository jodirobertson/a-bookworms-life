package cs3500.animator;

import java.io.FileNotFoundException;

import cs3500.animator.model.IModel;
import cs3500.animator.model.IModelImpl;
import cs3500.animator.util.AnimationFileReader;
import cs3500.animator.util.TweenModelBuilder;
import cs3500.animator.util.TweenModelBuilderImpl;
import cs3500.animator.view.IView;
import cs3500.animator.view.IViewImplTextSummary;
import cs3500.animator.view.IViewImplSVGAnimation;
import cs3500.animator.view.IViewImplVisualAnimation;

/**
 * Runs the main program according to given arguments.
 */
public final class EasyAnimator {
  /**
   *Constructs a compostion of animated iFigures in the desired view format according to
   * user-provided input, output, and tempo.
   *
   * @param args The input file name, type of view desired, output type desired, and tempo.
   * @throws Exception if any of the given arguments are invalid.
   */
  public static void main(String[] args) throws Exception {
    String fileName = "test/buildings.txt";
    String viewType = "visual";
    String outputType = "out.svg";
    double tempo = 0.1;

    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "-if":
          fileName = args[i + 1];
          i++;
          break;
        case "-iv":
          viewType = args[i + 1];
          i++;
          break;
        case "-o":
          outputType = args[i + 1];
          i++;
          break;
        case "-speed":
          tempo = Double.valueOf(args[i + 1]);
          i++;
          break;
        default:
          throw new IllegalArgumentException("Invalid input");
      }
    }

    // checks valid tempo
    if (tempo <= 0) {
      throw new IllegalArgumentException("Tempo must be greater than zero.");
    }

    IView view;
    IModel model = new IModelImpl();
    Appendable output = new StringBuffer();

    try {
      TweenModelBuilder<IModel> tween = new TweenModelBuilderImpl();
      model = new AnimationFileReader().readFile(fileName, tween);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given input file was not found");
    }

    switch (viewType) {
      case "text":
        view = new IViewImplTextSummary(model, output, tempo);
        view.play();
        break;
      case "visual":
        view = new IViewImplVisualAnimation(model, tempo);
        view.play();
        break;
      case "svg":
        // catches output exceptions
        try {
          view = new IViewImplSVGAnimation(model, output, tempo, 1200, 900);
          view.play();
        } catch (Exception e) {
          throw new IllegalArgumentException(e.getMessage());
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid view type");
    }
  }
}

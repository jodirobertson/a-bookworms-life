import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.StringWriter;

import cs3500.animator.model.IAnimation;
import cs3500.animator.model.IAnimationImplColorChange;
import cs3500.animator.model.IAnimationImplMove;
import cs3500.animator.model.IAnimationImplScale;
import cs3500.animator.model.IFigure;
import cs3500.animator.model.IFigureImplDummy;
import cs3500.animator.model.IModel;
import cs3500.animator.model.IModelImpl;
import cs3500.animator.util.AnimationFileReader;
import cs3500.animator.util.TweenModelBuilder;
import cs3500.animator.util.TweenModelBuilderImpl;
import cs3500.animator.view.IView;
import cs3500.animator.view.IViewImplTextSummary;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the text output IView.
 */
public class IViewImplTextSummaryTest {

  // tests invalid given zero tick
  @Test(expected = IllegalArgumentException.class)
  public void testZeroTick() {
    IModel model = new IModelImpl();
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 0);
  }

  // tests invalid given negative tick
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeTick() {
    IModel model = new IModelImpl();
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, -10);
  }

  // tests exceptions if output is closed
  @Test(expected = IllegalArgumentException.class)
  public void testClosedOutput() throws Exception {
    TweenModelBuilder<IModel> tween = new TweenModelBuilderImpl();
    IModel model = new AnimationFileReader().readFile("test/small-demo.txt", tween);
    Appendable out = new BufferedWriter(new StringWriter());
    ((BufferedWriter) out).close();
    IView view = new IViewImplTextSummary(model, out, 20);
    view.play();
  }

  // tests exceptions if input file not found
  @Test(expected = FileNotFoundException.class)
  public void testInputNotFound() throws Exception {
    Appendable out = new StringBuffer();
    TweenModelBuilder<IModel> tween = new TweenModelBuilderImpl();
    IModel model = new AnimationFileReader().readFile("small-demo.txt", tween);
    IView view = new IViewImplTextSummary(model, out, 3);
    view.play();
  }

  // tests exception if both given model and given output is null
  @Test(expected = IllegalArgumentException.class)
  public void testNulls() {
    IView view = new IViewImplTextSummary(null, null, 2);
  }

  // tests exception if given output is null
  @Test(expected = IllegalArgumentException.class)
  public void testNullOutput() {
    IModel model = new IModelImpl();
    IView view = new IViewImplTextSummary(model, null, 2);
  }

  // tests exception if given model is null
  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(null, out, 2);
  }

  // tests alternate input source
  @Test
  public void testUsingFile() throws Exception {
    Appendable out = new StringBuffer();
    TweenModelBuilder<IModel> tween = new TweenModelBuilderImpl();
    IModel model = new AnimationFileReader().readFile("test/small-demo.txt", tween);
    IView view = new IViewImplTextSummary(model, out, 3);
    view.play();
    assertEquals("Shapes:\n" +
            "Name: R\n" +
            "Type: rectangle\n" +
            "Corner: (200.0,200.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
            "Appears at t=0.3s\n" +
            "Disappears at t=33.3s\n" +
            "\n" +
            "Name: C\n" +
            "Type: oval\n" +
            "Center: (500.0,100.0), X radius: 60.0, Y radius: 30.0, Color: (0.0,0.0,1.0)\n" +
            "Appears at t=2.0s\n" +
            "Disappears at t=33.3s\n" +
            "\n" +
            "Shape R moves from (200.0,200.0) to (300.0,300.0) from t=3.3s to t=16.7s\n" +
            "Shape C moves from (500.0,100.0) to (500.0,400.0) from t=6.7s to t=23.3s\n" +
            "Shape C changes color from (0.0,0.0,1.0) to (0.0,1.0,0.0) from t=16" +
            ".7s to t=26.7s\n" +

            "Shape R scales from Width: 50.0, Height: 100.0 to Width: 25.0, Height: 100" +
            ".0 from t=17.0s to t=23.3s\n" +
            "Shape R moves from (300.0,300.0) to (200.0,200.0) from t=23.3s to t=33.3" +
            "s", out.toString());
  }

  // tests given example, adding the animations out of startTime order
  @Test
  public void testAnimationOrder() {
    IModel model = new IModelImpl();
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 2);

    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0},
            "R", 200,
            200, 1, 100, 50, 100);
    IFigure c = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1},
            "C", 500, 100, 6,
            100, 60, 30);
    model.add(r);
    model.add(c);
    IAnimation one = new IAnimationImplMove(model, "R", 100, 100, 10, 50);
    model.animate(one, "R");
    IAnimation two = new IAnimationImplMove(model, "C", 0, 300, 20, 70);
    model.animate(two, "C");
    IAnimation five = new IAnimationImplMove(model, "R", -100, -100, 70, 100);
    model.animate(five, "R");
    IAnimation four = new IAnimationImplScale(model, "R", 0.5, 1, 51, 70);
    model.animate(four, "R");
    IAnimation three = new IAnimationImplColorChange(model, "C", new double[]{0, 1, 0}, 50, 80);
    model.animate(three, "C");
    view.play();
    assertEquals("Shapes:\n" +
            "Name: R\n" +
            "Type: rectangle\n" +
            "Corner: (200.0,200.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=50.0s\n" +
            "\n" +
            "Name: C\n" +
            "Type: oval\n" +
            "Center: (500.0,100.0), X radius: 60.0, Y radius: 30.0, Color: (0.0,0.0,1.0)\n" +
            "Appears at t=3.0s\n" +
            "Disappears at t=50.0s\n" +
            "\n" +
            "Shape R moves from (200.0,200.0) to (300.0,300.0) from t=5.0s to t=25.0s\n" +
            "Shape C moves from (500.0,100.0) to (500.0,400.0) from t=10.0s to t=35.0s\n" +
            "Shape C changes color from (0.0,0.0,1.0) to (0.0,1.0,0.0) from t=25" +
            ".0s to t=40.0s\n" +
            "Shape R scales from Width: 50.0, Height: 100.0 to Width: 25.0, Height: 100" +
            ".0 from t=25.5s to t=35.0s\n" +
            "Shape R moves from (300.0,300.0) to (200.0,200.0) from t=35.0s to" +
            " t=50.0s", out.toString());
  }

  // tests given example, adding the oval first
  @Test
  public void testAddingFiguresOrder() {
    IModel model = new IModelImpl();
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 2);

    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0},
            "R", 200,
            200, 1, 100, 50, 100);
    IFigure c = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1},
            "C", 500, 100, 6,
            100, 60, 30);
    model.add(c);
    model.add(r);
    IAnimation one = new IAnimationImplMove(model, "R", 100, 100, 10, 50);
    model.animate(one, "R");
    IAnimation two = new IAnimationImplMove(model, "C", 0, 300, 20, 70);
    model.animate(two, "C");
    IAnimation three = new IAnimationImplColorChange(model, "C", new double[]{0, 1, 0}, 50, 80);
    model.animate(three, "C");
    IAnimation four = new IAnimationImplScale(model, "R", 0.5, 1, 51, 70);
    model.animate(four, "R");
    IAnimation five = new IAnimationImplMove(model, "R", -100, -100, 70, 100);
    model.animate(five, "R");
    view.play();
    assertEquals("Shapes:\n" +
            "Name: R\n" +
            "Type: rectangle\n" +
            "Corner: (200.0,200.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=50.0s\n" +
            "\n" +
            "Name: C\n" +
            "Type: oval\n" +
            "Center: (500.0,100.0), X radius: 60.0, Y radius: 30.0, Color: (0.0,0.0,1.0)\n" +
            "Appears at t=3.0s\n" +
            "Disappears at t=50.0s\n" +
            "\n" +
            "Shape R moves from (200.0,200.0) to (300.0,300.0) from t=5.0s to t=25.0s\n" +
            "Shape C moves from (500.0,100.0) to (500.0,400.0) from t=10.0s to t=35.0s\n" +
            "Shape C changes color from (0.0,0.0,1.0) to (0.0,1.0,0.0) from t=25.0s to t=" +
            "40.0s\n" +
            "Shape R scales from Width: 50.0, Height: 100.0 to Width: 25.0, Height: 100.0 fr" +
            "om t=25.5s to t=35.0s\n" +
            "Shape R moves from (300.0,300.0) to (200.0,200.0) from t=35.0s to t=5" +
            "0.0s", out.toString());
  }

  // tests given example from assignment
  @Test
  public void testPrintGivenExample() {
    IModel model = new IModelImpl();
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 2);
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0},
            "R", 200,
            200, 1, 100, 50, 100);
    IFigure c = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1},
            "C", 500, 100, 6,
            100, 60, 30);
    model.add(c);
    model.add(r);
    IAnimation one = new IAnimationImplMove(model, "R", 100, 100, 10, 50);
    model.animate(one, "R");
    IAnimation two = new IAnimationImplMove(model, "C", 0, 300, 20, 70);
    model.animate(two, "C");
    IAnimation three = new IAnimationImplColorChange(model, "C", new double[]{0, 1, 0}, 50, 80);
    model.animate(three, "C");
    IAnimation four = new IAnimationImplScale(model, "R", 0.5, 1, 51, 70);
    model.animate(four, "R");
    IAnimation five = new IAnimationImplMove(model, "R", -100, -100, 70, 100);
    model.animate(five, "R");
    view.play();
    assertEquals("Shapes:\n" +
            "Name: R\n" +
            "Type: rectangle\n" +
            "Corner: (200.0,200.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=50.0s\n" +
            "\n" +
            "Name: C\n" +
            "Type: oval\n" +
            "Center: (500.0,100.0), X radius: 60.0, Y radius: 30.0, Color: (0.0,0.0,1.0)\n" +
            "Appears at t=3.0s\n" +
            "Disappears at t=50.0s\n" +
            "\n" +
            "Shape R moves from (200.0,200.0) to (300.0,300.0) from t=5.0s to t=25.0s\n" +
            "Shape C moves from (500.0,100.0) to (500.0,400.0) from t=10.0s to t=35.0s\n" +
            "Shape C changes color from (0.0,0.0,1.0) to (0.0,1.0,0.0) from t=25.0s to t=40.0s\n" +
            "Shape R scales from Width: 50.0, Height: 100.0 to Width: 25.0, Height: 100.0" +
            " from t=25.5s to t=35.0s\n" +
            "Shape R moves from (300.0,300.0) to (200.0,200.0) from t=35.0s " +
            "to t=50.0s", out.toString());
  }

  // tests for general usage as expected, with several animations happening at once
  @Test
  public void testPlay() {
    IModel model = new IModelImpl();
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 1);
    IFigure a = new IFigureImplDummy(model, "rectangle", new double[]{1, 0.5, 1}, "ToolBox",
            500, 100, 1,
            100, 60, 30);
    model.add(a);
    IFigure b = new IFigureImplDummy(model, "rectangle", new double[]{1, 0.5, 1}, "Handle",
            500, 120, 1,
            100, 10, 8);
    model.add(b);
    IAnimation shrink = new IAnimationImplScale(model, "ToolBox", 0.5, 0.5, 3, 5);
    model.animate(shrink, "ToolBox");
    IAnimation shrinkToo = new IAnimationImplScale(model, "Handle", 0.5, 0.5, 3, 5);
    model.animate(shrinkToo, "Handle");
    IAnimation changeColor = new IAnimationImplColorChange(model, "ToolBox",
            new double[]{0.5, 0.5, 1}, 3, 5);
    model.animate(changeColor, "ToolBox");
    IAnimation drop = new IAnimationImplMove(model, "Handle", 0, 7, 4, 8);
    model.animate(drop, "Handle");
    view.play();
    assertEquals("Shapes:\n" +
                    "Name: ToolBox\n" +
                    "Type: rectangle\n" +
                    "Corner: (500.0,100.0), Width: 60.0, Height: 30.0, Color: (1.0,0.5,1.0)\n" +
                    "Appears at t=1.0s\n" +
                    "Disappears at t=100.0s\n" +
                    "\n" +
                    "Name: Handle\n" +
                    "Type: rectangle\n" +
                    "Corner: (500.0,120.0), Width: 10.0, Height: 8.0, Color: (1.0,0.5,1.0)\n" +
                    "Appears at t=1.0s\n" +
                    "Disappears at t=100.0s\n" +
                    "\n" +
                    "Shape ToolBox scales from Width: 60.0, Height: 30.0 to Width: 30.0, Height:" +
                    " 15.0 from t=3.0s to t=5.0s\n" +
                    "Shape Handle scales from Width: 10.0, Height: 8.0 to Width: 5.0, Height:" +
                    " 4.0 from t=3.0s to t=5.0s\n" +
                    "Shape ToolBox changes color from (1.0,0.5,1.0) to (0.5,0.5,1.0) from t=3.0s" +
                    " to t=5.0s\n" +
                    "Shape Handle moves from (500.0,120.0) to (500.0,127.0) from t=4.0s to t=8.0s",
            out.toString());
  }

  // tests with given building file
  @Test
  public void testBuildings() throws FileNotFoundException {
    Appendable out = new StringBuffer();
    TweenModelBuilder<IModel> tween = new TweenModelBuilderImpl();
    IModel model = new AnimationFileReader().readFile("test/buildings.txt", tween);
    IView view = new IViewImplTextSummary(model, out, 2);
    view.play();
    assertEquals("Shapes:\n" +
            "Name: background\n" +
            "Type: rectangle\n" +
            "Corner: (0.0,0.0), Width: 800.0, Height: 800.0, Color: (0.1,0.4,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: B0\n" +
            "Type: rectangle\n" +
            "Corner: (80.0,424.0), Width: 100.0, Height: 326.0, Color: (0.0,0.0,0.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: B1\n" +
            "Type: rectangle\n" +
            "Corner: (260.0,365.0), Width: 100.0, Height: 385.0, Color: (0.0,0.0,0.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: B2\n" +
            "Type: rectangle\n" +
            "Corner: (440.0,375.0), Width: 100.0, Height: 375.0, Color: (0.0,0.0,0.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: B3\n" +
            "Type: rectangle\n" +
            "Corner: (620.0,445.0), Width: 100.0, Height: 305.0, Color: (0.0,0.0,0.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window000\n" +
            "Type: rectangle\n" +
            "Corner: (100.0,850.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window001\n" +
            "Type: rectangle\n" +
            "Corner: (140.0,850.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window010\n" +
            "Type: rectangle\n" +
            "Corner: (100.0,890.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window011\n" +
            "Type: rectangle\n" +
            "Corner: (140.0,890.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window020\n" +
            "Type: rectangle\n" +
            "Corner: (100.0,930.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window021\n" +
            "Type: rectangle\n" +
            "Corner: (140.0,930.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window030\n" +
            "Type: rectangle\n" +
            "Corner: (100.0,970.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window031\n" +
            "Type: rectangle\n" +
            "Corner: (140.0,970.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window040\n" +
            "Type: rectangle\n" +
            "Corner: (100.0,1010.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window041\n" +
            "Type: rectangle\n" +
            "Corner: (140.0,1010.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window050\n" +
            "Type: rectangle\n" +
            "Corner: (100.0,1050.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window051\n" +
            "Type: rectangle\n" +
            "Corner: (140.0,1050.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window060\n" +
            "Type: rectangle\n" +
            "Corner: (100.0,1090.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window061\n" +
            "Type: rectangle\n" +
            "Corner: (140.0,1090.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window100\n" +
            "Type: rectangle\n" +
            "Corner: (280.0,-750.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window101\n" +
            "Type: rectangle\n" +
            "Corner: (320.0,-750.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window110\n" +
            "Type: rectangle\n" +
            "Corner: (280.0,-710.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window111\n" +
            "Type: rectangle\n" +
            "Corner: (320.0,-710.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window120\n" +
            "Type: rectangle\n" +
            "Corner: (280.0,-670.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window121\n" +
            "Type: rectangle\n" +
            "Corner: (320.0,-670.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window130\n" +
            "Type: rectangle\n" +
            "Corner: (280.0,-630.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window131\n" +
            "Type: rectangle\n" +
            "Corner: (320.0,-630.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window140\n" +
            "Type: rectangle\n" +
            "Corner: (280.0,-590.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window141\n" +
            "Type: rectangle\n" +
            "Corner: (320.0,-590.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window150\n" +
            "Type: rectangle\n" +
            "Corner: (280.0,-550.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window151\n" +
            "Type: rectangle\n" +
            "Corner: (320.0,-550.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window160\n" +
            "Type: rectangle\n" +
            "Corner: (280.0,-510.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window161\n" +
            "Type: rectangle\n" +
            "Corner: (320.0,-510.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window170\n" +
            "Type: rectangle\n" +
            "Corner: (280.0,-470.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window171\n" +
            "Type: rectangle\n" +
            "Corner: (320.0,-470.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window180\n" +
            "Type: rectangle\n" +
            "Corner: (280.0,-430.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window181\n" +
            "Type: rectangle\n" +
            "Corner: (320.0,-430.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window200\n" +
            "Type: rectangle\n" +
            "Corner: (460.0,850.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window201\n" +
            "Type: rectangle\n" +
            "Corner: (500.0,850.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window210\n" +
            "Type: rectangle\n" +
            "Corner: (460.0,890.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window211\n" +
            "Type: rectangle\n" +
            "Corner: (500.0,890.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window220\n" +
            "Type: rectangle\n" +
            "Corner: (460.0,930.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window221\n" +
            "Type: rectangle\n" +
            "Corner: (500.0,930.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window230\n" +
            "Type: rectangle\n" +
            "Corner: (460.0,970.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window231\n" +
            "Type: rectangle\n" +
            "Corner: (500.0,970.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window240\n" +
            "Type: rectangle\n" +
            "Corner: (460.0,1010.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window241\n" +
            "Type: rectangle\n" +
            "Corner: (500.0,1010.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window250\n" +
            "Type: rectangle\n" +
            "Corner: (460.0,1050.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window251\n" +
            "Type: rectangle\n" +
            "Corner: (500.0,1050.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window260\n" +
            "Type: rectangle\n" +
            "Corner: (460.0,1090.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window261\n" +
            "Type: rectangle\n" +
            "Corner: (500.0,1090.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window270\n" +
            "Type: rectangle\n" +
            "Corner: (460.0,1130.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window271\n" +
            "Type: rectangle\n" +
            "Corner: (500.0,1130.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window300\n" +
            "Type: rectangle\n" +
            "Corner: (640.0,-750.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window301\n" +
            "Type: rectangle\n" +
            "Corner: (680.0,-750.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window310\n" +
            "Type: rectangle\n" +
            "Corner: (640.0,-710.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window311\n" +
            "Type: rectangle\n" +
            "Corner: (680.0,-710.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window320\n" +
            "Type: rectangle\n" +
            "Corner: (640.0,-670.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window321\n" +
            "Type: rectangle\n" +
            "Corner: (680.0,-670.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window330\n" +
            "Type: rectangle\n" +
            "Corner: (640.0,-630.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window331\n" +
            "Type: rectangle\n" +
            "Corner: (680.0,-630.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window340\n" +
            "Type: rectangle\n" +
            "Corner: (640.0,-590.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window341\n" +
            "Type: rectangle\n" +
            "Corner: (680.0,-590.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window350\n" +
            "Type: rectangle\n" +
            "Corner: (640.0,-550.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window351\n" +
            "Type: rectangle\n" +
            "Corner: (680.0,-550.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window360\n" +
            "Type: rectangle\n" +
            "Corner: (640.0,-510.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: window361\n" +
            "Type: rectangle\n" +
            "Corner: (680.0,-510.0), Width: 20.0, Height: 20.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: moon\n" +
            "Type: oval\n" +
            "Center: (250.0,250.0), X radius: 50.0, Y radius: 50.0, Color: (0.9,0.9,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: eclipse\n" +
            "Type: oval\n" +
            "Center: (450.0,50.0), X radius: 50.0, Y radius: 50.0, Color: (0.1,0.4,1.0)\n" +
            "Appears at t=0.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star4\n" +
            "Type: oval\n" +
            "Center: (711.0,284.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=41.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star34\n" +
            "Type: oval\n" +
            "Center: (286.0,114.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=42.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star8\n" +
            "Type: oval\n" +
            "Center: (314.0,150.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=43.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star12\n" +
            "Type: oval\n" +
            "Center: (771.0,197.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=43.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star9\n" +
            "Type: oval\n" +
            "Center: (173.0,284.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=44.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star17\n" +
            "Type: oval\n" +
            "Center: (408.0,66.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=44.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star22\n" +
            "Type: oval\n" +
            "Center: (187.0,128.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=44.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star10\n" +
            "Type: oval\n" +
            "Center: (722.0,105.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=45.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star6\n" +
            "Type: oval\n" +
            "Center: (532.0,73.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=46.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star35\n" +
            "Type: oval\n" +
            "Center: (652.0,329.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=46.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star3\n" +
            "Type: oval\n" +
            "Center: (377.0,289.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=46.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star19\n" +
            "Type: oval\n" +
            "Center: (111.0,200.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=47.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star33\n" +
            "Type: oval\n" +
            "Center: (659.0,104.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=47.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star23\n" +
            "Type: oval\n" +
            "Center: (137.0,233.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=48.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star29\n" +
            "Type: oval\n" +
            "Center: (49.0,249.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=48.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star5\n" +
            "Type: oval\n" +
            "Center: (511.0,263.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=48.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star14\n" +
            "Type: oval\n" +
            "Center: (513.0,81.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=49.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star37\n" +
            "Type: oval\n" +
            "Center: (116.0,279.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=49.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star38\n" +
            "Type: oval\n" +
            "Center: (607.0,305.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=49.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star20\n" +
            "Type: oval\n" +
            "Center: (740.0,81.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=51.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star16\n" +
            "Type: oval\n" +
            "Center: (494.0,255.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=53.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star26\n" +
            "Type: oval\n" +
            "Center: (325.0,272.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=53.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star0\n" +
            "Type: oval\n" +
            "Center: (226.0,69.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=54.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star32\n" +
            "Type: oval\n" +
            "Center: (553.0,235.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=54.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star2\n" +
            "Type: oval\n" +
            "Center: (492.0,80.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=55.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star25\n" +
            "Type: oval\n" +
            "Center: (262.0,122.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=56.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star13\n" +
            "Type: oval\n" +
            "Center: (769.0,182.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=56.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star18\n" +
            "Type: oval\n" +
            "Center: (553.0,270.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=56.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star15\n" +
            "Type: oval\n" +
            "Center: (624.0,152.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=57.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star36\n" +
            "Type: oval\n" +
            "Center: (694.0,270.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=57.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star7\n" +
            "Type: oval\n" +
            "Center: (335.0,68.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=58.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star27\n" +
            "Type: oval\n" +
            "Center: (415.0,185.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=58.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star28\n" +
            "Type: oval\n" +
            "Center: (677.0,140.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=59.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star1\n" +
            "Type: oval\n" +
            "Center: (588.0,214.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=60.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star24\n" +
            "Type: oval\n" +
            "Center: (247.0,156.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=60.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star31\n" +
            "Type: oval\n" +
            "Center: (188.0,239.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=60.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star30\n" +
            "Type: oval\n" +
            "Center: (391.0,318.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=61.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star21\n" +
            "Type: oval\n" +
            "Center: (798.0,140.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=63.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star39\n" +
            "Type: oval\n" +
            "Center: (465.0,165.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=63.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: star11\n" +
            "Type: oval\n" +
            "Center: (527.0,267.0), X radius: 3.0, Y radius: 3.0, Color: (1.0,1.0,1.0)\n" +
            "Appears at t=64.5s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Shape window000 moves from (100.0,850.0) to (100.0,710.0) from t=10.0s to t=30.0s\n" +
            "Shape window001 moves from (140.0,850.0) to (140.0,710.0) from t=10.0s to t=30.0s\n" +
            "Shape window010 moves from (100.0,890.0) to (100.0,670.0) from t=10.0s to t=30.0s\n" +
            "Shape window011 moves from (140.0,890.0) to (140.0,670.0) from t=10.0s to t=30.0s\n" +
            "Shape window020 moves from (100.0,930.0) to (100.0,630.0) from t=10.0s to t=30.0s\n" +
            "Shape window021 moves from (140.0,930.0) to (140.0,630.0) from t=10.0s to t=30.0s\n" +
            "Shape window030 moves from (100.0,970.0) to (100.0,590.0) from t=10.0s to t=30.0s\n" +
            "Shape window031 moves from (140.0,970.0) to (140.0,590.0) from t=10.0s to t=30.0s\n" +
            "Shape window040 moves from (100.0,1010.0) to (100.0,550.0) from t=10.0s to t=30.0s\n" +
            "Shape window041 moves from (140.0,1010.0) to (140.0,550.0) from t=10.0s to t=30.0s\n" +
            "Shape window050 moves from (100.0,1050.0) to (100.0,510.0) from t=10.0s to t=30.0s\n" +
            "Shape window051 moves from (140.0,1050.0) to (140.0,510.0) from t=10.0s to t=30.0s\n" +
            "Shape window060 moves from (100.0,1090.0) to (100.0,470.0) from t=10.0s to t=30.0s\n" +
            "Shape window061 moves from (140.0,1090.0) to (140.0,470.0) from t=10.0s to t=30.0s\n" +
            "Shape window100 moves from (280.0,-750.0) to (280.0,710.0) from t=10.0s to t=30.0s\n" +
            "Shape window101 moves from (320.0,-750.0) to (320.0,710.0) from t=10.0s to t=30.0s\n" +
            "Shape window110 moves from (280.0,-710.0) to (280.0,670.0) from t=10.0s to t=30.0s\n" +
            "Shape window111 moves from (320.0,-710.0) to (320.0,670.0) from t=10.0s to t=30.0s\n" +
            "Shape window120 moves from (280.0,-670.0) to (280.0,630.0) from t=10.0s to t=30.0s\n" +
            "Shape window121 moves from (320.0,-670.0) to (320.0,630.0) from t=10.0s to t=30.0s\n" +
            "Shape window130 moves from (280.0,-630.0) to (280.0,590.0) from t=10.0s to t=30.0s\n" +
            "Shape window131 moves from (320.0,-630.0) to (320.0,590.0) from t=10.0s to t=30.0s\n" +
            "Shape window140 moves from (280.0,-590.0) to (280.0,550.0) from t=10.0s to t=30.0s\n" +
            "Shape window141 moves from (320.0,-590.0) to (320.0,550.0) from t=10.0s to t=30.0s\n" +
            "Shape window150 moves from (280.0,-550.0) to (280.0,510.0) from t=10.0s to t=30.0s\n" +
            "Shape window151 moves from (320.0,-550.0) to (320.0,510.0) from t=10.0s to t=30.0s\n" +
            "Shape window160 moves from (280.0,-510.0) to (280.0,470.0) from t=10.0s to t=30.0s\n" +
            "Shape window161 moves from (320.0,-510.0) to (320.0,470.0) from t=10.0s to t=30.0s\n" +
            "Shape window170 moves from (280.0,-470.0) to (280.0,430.0) from t=10.0s to t=30.0s\n" +
            "Shape window171 moves from (320.0,-470.0) to (320.0,430.0) from t=10.0s to t=30.0s\n" +
            "Shape window180 moves from (280.0,-430.0) to (280.0,390.0) from t=10.0s to t=30.0s\n" +
            "Shape window181 moves from (320.0,-430.0) to (320.0,390.0) from t=10.0s to t=30.0s\n" +
            "Shape window200 moves from (460.0,850.0) to (460.0,710.0) from t=10.0s to t=30.0s\n" +
            "Shape window201 moves from (500.0,850.0) to (500.0,710.0) from t=10.0s to t=30.0s\n" +
            "Shape window210 moves from (460.0,890.0) to (460.0,670.0) from t=10.0s to t=30.0s\n" +
            "Shape window211 moves from (500.0,890.0) to (500.0,670.0) from t=10.0s to t=30.0s\n" +
            "Shape window220 moves from (460.0,930.0) to (460.0,630.0) from t=10.0s to t=30.0s\n" +
            "Shape window221 moves from (500.0,930.0) to (500.0,630.0) from t=10.0s to t=30.0s\n" +
            "Shape window230 moves from (460.0,970.0) to (460.0,590.0) from t=10.0s to t=30.0s\n" +
            "Shape window231 moves from (500.0,970.0) to (500.0,590.0) from t=10.0s to t=30.0s\n" +
            "Shape window240 moves from (460.0,1010.0) to (460.0,550.0) from t=10.0s to t=30.0s\n" +
            "Shape window241 moves from (500.0,1010.0) to (500.0,550.0) from t=10.0s to t=30.0s\n" +
            "Shape window250 moves from (460.0,1050.0) to (460.0,510.0) from t=10.0s to t=30.0s\n" +
            "Shape window251 moves from (500.0,1050.0) to (500.0,510.0) from t=10.0s to t=30.0s\n" +
            "Shape window260 moves from (460.0,1090.0) to (460.0,470.0) from t=10.0s to t=30.0s\n" +
            "Shape window261 moves from (500.0,1090.0) to (500.0,470.0) from t=10.0s to t=30.0s\n" +
            "Shape window270 moves from (460.0,1130.0) to (460.0,430.0) from t=10.0s to t=30.0s\n" +
            "Shape window271 moves from (500.0,1130.0) to (500.0,430.0) from t=10.0s to t=30.0s\n" +
            "Shape window300 moves from (640.0,-750.0) to (640.0,710.0) from t=10.0s to t=30.0s\n" +
            "Shape window301 moves from (680.0,-750.0) to (680.0,710.0) from t=10.0s to t=30.0s\n" +
            "Shape window310 moves from (640.0,-710.0) to (640.0,670.0) from t=10.0s to t=30.0s\n" +
            "Shape window311 moves from (680.0,-710.0) to (680.0,670.0) from t=10.0s to t=30.0s\n" +
            "Shape window320 moves from (640.0,-670.0) to (640.0,630.0) from t=10.0s to t=30.0s\n" +
            "Shape window321 moves from (680.0,-670.0) to (680.0,630.0) from t=10.0s to t=30.0s\n" +
            "Shape window330 moves from (640.0,-630.0) to (640.0,590.0) from t=10.0s to t=30.0s\n" +
            "Shape window331 moves from (680.0,-630.0) to (680.0,590.0) from t=10.0s to t=30.0s\n" +
            "Shape window340 moves from (640.0,-590.0) to (640.0,550.0) from t=10.0s to t=30.0s\n" +
            "Shape window341 moves from (680.0,-590.0) to (680.0,550.0) from t=10.0s to t=30.0s\n" +
            "Shape window350 moves from (640.0,-550.0) to (640.0,510.0) from t=10.0s to t=30.0s\n" +
            "Shape window351 moves from (680.0,-550.0) to (680.0,510.0) from t=10.0s to t=30.0s\n" +
            "Shape window360 moves from (640.0,-510.0) to (640.0,470.0) from t=10.0s to t=30.0s\n" +
            "Shape window361 moves from (680.0,-510.0) to (680.0,470.0) from t=10.0s to t=30.0s\n" +
            "Shape eclipse moves from (450.0,50.0) to (280.0,230.0) from t=25.0s to t=45.0s\n" +
            "Shape background changes color from (0.1,0.4,1.0) to (0.1,0.2,1.0) from t=25.0s to " +
            "t=45.0s\n" +
            "Shape eclipse changes color from (0.1,0.4,1.0) to (0.1,0.2,1.0) from t=25.0s to " +
            "t=45.0s\n" +
            "Shape window330 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=40.5s t" +
            "o t=43.0s\n" +
            "Shape window021 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=41." +
            "0s to t=43.5s\n" +
            "Shape window210 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=41" +
            ".5s to t=44.0s\n" +
            "Shape window061 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=46" +
            ".5s to t=49.0s\n" +
            "Shape window110 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=47.5" +
            "s to t=50.0s\n" +
            "Shape window261 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=48.0s t" +
            "o t=50.5s\n" +
            "Shape window151 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=49.5s " +
            "to t=52.0s\n" +
            "Shape window360 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=49." +
            "5s to t=52.0s\n" +
            "Shape window361 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=49" +
            ".5s to t=52.0s\n" +
            "Shape window351 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=50." +
            "0s to t=52.5s\n" +
            "Shape window310 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=51.5s" +
            " to t=54.0s\n" +
            "Shape window101 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=5" +
            "4.0s to t=56.5s\n" +
            "Shape window241 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=" +
            "56.0s to t=58.5s\n" +
            "Shape window331 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=5" +
            "9.5s to t=62.0s\n" +
            "Shape window200 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=" +
            "63.5s to t=66.0s\n" +
            "Shape window250 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=" +
            "64.0s to t=66.5s\n" +
            "Shape window301 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=" +
            "67.5s to t=70.0s\n" +
            "Shape window041 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=" +
            "73.5s to t=76.0s\n" +
            "Shape window060 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from t=" +
            "73.5s to t=76.0s\n" +
            "Shape window131 changes color from (1.0,1.0,1.0) to (1.0,1.0,0.0) from " +
            "t=74.0s to t=76.5s", out.toString());
  }
}

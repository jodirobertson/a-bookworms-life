import org.junit.Test;

import static org.junit.Assert.assertEquals;

import cs3500.animator.model.IAnimation;
import cs3500.animator.model.IAnimationImplColorChange;
import cs3500.animator.model.IAnimationImplMove;
import cs3500.animator.model.IAnimationImplScale;
import cs3500.animator.model.IFigure;
import cs3500.animator.model.IFigureImplDummy;
import cs3500.animator.model.IModel;
import cs3500.animator.model.IModelImpl;
import cs3500.animator.view.IView;
import cs3500.animator.view.IViewImplTextSummary;


/**
 * Represents class for testing.
 */
public class IModelImplTest {

  // tests for general usage as expected, with several animations happening at once
  @Test
  public void testPlay() {
    IModel model = new IModelImpl();
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
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 1);
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
                    "Shape ToolBox scales from Width: 60.0, Height: 30.0 to Width: 30." +
                    "0, Height: 15.0 from t=3.0s to t=5.0s\n" +
                    "Shape Handle scales from Width: 10.0, Height: 8.0 to Width: 5.0, Heig" +
                    "ht: 4.0 from t=3.0s to t=5.0s\n" +
                    "Shape ToolBox changes color from (1.0,0.5,1.0) to (0.5,0.5,1.0) from" +
                    " t=3.0s to t=5.0s\n" +
                    "Shape Handle moves from (500.0,120.0) to (500.0,127.0) from t=4.0s to t=8.0s",
            out.toString());
  }

  // tests for negative start time of a color change
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartTimes() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 1}, "Rebel",
            500, 100, 1,
            100, 60, 30);
    IAnimation x = new IAnimationImplColorChange(model, "Rebel", new double[]{1, 0, 1}, -1, 5);
  }

  // tests for negative end time of a color change
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartTimes2() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 1}, "Rebel",
            500, 100, 1,
            100, 60, 30);
    IAnimation x = new IAnimationImplColorChange(model, "Rebel", new double[]{1, 0, 1}, 1, -5);
  }

  // tests for invalid end time of a color change
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartTimes3() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 1}, "Rebel",
            500, 100, 1,
            100, 60, 30);
    IAnimation x = new IAnimationImplColorChange(model, "Rebel", new double[]{1, 0, 1}, 50, 5);
  }

  // tests for invalid end time of a color change
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidStartTimes4() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 1}, "Rebel",
            500, 100, 1,
            100, 60, 30);
    IAnimation x = new IAnimationImplColorChange(model, "Rebel", new double[]{1, 0, 1}, 9, 5);
  }

  // tests for negative disappear time of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimes5() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 1}, "Rebel",
            500, 100, 1,
            -100, 60, 30);
  }

  // tests for negative disappear time of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimes6() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "oval", new double[]{1, 0, 1}, "Rebel",
            500, 100, 1,
            -100, 60, 30);
  }

  // tests for negative appear time of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimes() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 1}, "Rebel",
            500, 100, -1,
            100, 60, 30);
  }

  // tests for negative appear time of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimes2() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "oval", new double[]{1, 0, 1}, "Rebel",
            500, 100, -1,
            100, 60, 30);
  }

  // tests for invalid appear time of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimes3() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 1}, "Rebel",
            500, 100, 100,
            100, 60, 30);
  }

  // tests for invalid appear time of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTimes4() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "oval", new double[]{1, 0, 1}, "Rebel",
            500, 100, 101,
            100, 60, 30);
  }

  // tests for invalid colors of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColor1() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{2, 0, 1}, "Reggie",
            500, 100, 1,
            100, 60, 30);
  }

  // tests for invalid colors of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColor2() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{0, 0, -1},
            "Reg", 500, 100, 1,
            100, 60, 30);
  }

  // tests for invalid colors of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColor3() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{0, 9, -1},
            "Roxy", 500, 100, 1,
            100, 60, 30);
  }

  // tests for invalid colors of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColorOval1() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "oval", new double[]{0, 0, -1}, "Opal", 500, 100, 1,
            100, 60, 30);
  }

  // tests for invalid colors of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColorOval2() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "oval", new double[]{0, -90, 0}, "Olivia", 500, 100, 1,
            100, 60, 30);
  }

  // tests for invalid dimensions of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimRect1() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "oval", new double[]{100, 90, -1}, "Oops", 500, 100, 1,
            100, 60, 30);
  }

  // tests for invalid dimensions of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimRect2() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{0, 0, 1},
            "Rectangle", 500, 100, 1,
            100, 60, -30);
  }

  // tests for invalid dimensions of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimRect3() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{0, 0, 1},
            "Rectangle", 500, 100, 1,
            100, -60, -30);
  }

  // tests for invalid 0-dimensions of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimRect4() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{0, 0, 1},
            "Rectangle", 500, 100, 1,
            100, 0, 30);
  }

  // tests for invalid 0-dimensions of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimRect5() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{0, 0, 1},
            "Rectangle", 500, 100, 1,
            100, 60, 0);
  }

  // tests for invalid 0-dimensions of a rectangle
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimRect6() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{0, 0, 1},
            "Rectangle", 500, 100, 1,
            100, 0, 0);
  }

  // tests for invalid dimensions of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimOval1() {
    IModel model = new IModelImpl();
    IFigure oval = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1},
            "Opal", 500, 100, 1,
            100, -60, 30);
  }

  // tests for invalid dimensions of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimOval2() {
    IModel model = new IModelImpl();
    IFigure oval = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1},
            "Opal", 500, 100, 1,
            100, 60, -30);
  }

  // tests for invalid dimensions of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidDimOval3() {
    IModel model = new IModelImpl();
    IFigure oval = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1},
            "Opal", 500, 100, 1,
            100, -60, -30);
  }

  // checks for invalid 0-dimensions of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testValidDimOval00() {
    IModel model = new IModelImpl();
    IFigure oval = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1},
            "Opal", 500, 100, 1,
            100, 0, 9);
  }

  // checks for invalid 0-dimensions of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testValidDimOval01() {
    IModel model = new IModelImpl();
    IFigure oval = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1},
            "Opal", 500, 100, 1,
            100, 10, 0);
  }

  // checks for invalid 0-dimensions of an oval
  @Test(expected = IllegalArgumentException.class)
  public void testValidDimOval02() {
    IModel model = new IModelImpl();
    IFigure oval = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1},
            "Opal", 500, 100, 1,
            100, 0, 0);
  }

  // tests adding a an IFigure whose name already exists
  @Test(expected = IllegalArgumentException.class)
  public void testDuplicateName() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 200,
            200, 6, 100, 50, 100);
    IFigure r2 = new IFigureImplDummy(model, "rectangle", new double[]{0, 0, 1}, "R",
            500, 100, 1,
            100, 60, 30);
    model.add(r);
    model.add(r2);
  }

  // tests that getState updates with out-of order figures and animations, as well as
  // multiple moves on both an oval and a rectangle
  @Test
  public void testOOOGetState() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 200,
            200, 6, 100, 50, 100);
    IFigure c = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1}, "C", 500, 100, 1,
            100, 60, 30);
    model.add(c);
    model.add(r);
    IAnimation one = new IAnimationImplMove(model, "R", 100, 100, 10, 50);
    model.animate(one, "R");
    IAnimation two = new IAnimationImplMove(model, "C", 0, 300, 20, 70);
    model.animate(two, "C");
    IAnimation three = new IAnimationImplColorChange(model, "C", new double[]{0, 1, 0}, 12, 40);
    model.animate(three, "C");
    IAnimation four = new IAnimationImplScale(model, "R", 0.5, 1, 3, 80);
    model.animate(four, "R");
    IAnimation five = new IAnimationImplMove(model, "R", -100, -100, 70, 100);
    model.animate(five, "R");
    IAnimation six = new IAnimationImplMove(model, "C", 0, 300, 71, 80);
    model.animate(six, "C");
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 1);
    view.play();
    assertEquals("Shapes:\n" +
                    "Name: C\n" +
                    "Type: oval\n" +
                    "Center: (500.0,100.0), X radius: 60.0, Y radius: 30.0," +
                    " Color: (0.0,0.0,1.0)\n" +
                    "Appears at t=1.0s\n" +
                    "Disappears at t=100.0s\n" +
                    "\n" +
                    "Name: R\n" +
                    "Type: rectangle\n" +
                    "Corner: (200.0,200.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
                    "Appears at t=6.0s\n" +
                    "Disappears at t=100.0s\n" +
                    "\n" +
                    "Shape R scales from Width: 50.0, Height: 100.0 to Width: 25.0, " +
                    "Height: 100.0 from t=3.0s to t=80.0s\n" +
                    "Shape R moves from (200.0,200.0) to (300.0,300.0) from t=10.0s to t=50.0s\n" +
                    "Shape C changes color from (0.0,0.0,1.0) to (0.0,1.0,0.0) from t=12" +
                    ".0s to t=40.0s\n" +
                    "Shape C moves from (500.0,100.0) to (500.0,400.0) from t=20.0s to t=70.0s\n" +
                    "Shape R moves from (300.0,300.0) to (200.0,200.0) from t=70.0s to t=100.0s\n" +
                    "Shape C moves from (500.0,400.0) to (500.0,700.0) from t=71.0s to t=80.0s",
            out.toString());
  }

  // tests scaling an oval and a rectangle multiple times
  @Test
  public void testScaleGetState() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 0,
            0, 1, 100, 50, 100);
    IFigure c = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1}, "C", 500, 100, 6,
            100, 60, 30);
    model.add(r);
    model.add(c);
    IAnimation oneR = new IAnimationImplScale(model, "R", 0.5, 1, 0, 10);
    model.animate(oneR, "R");
    IAnimation oneC = new IAnimationImplScale(model, "C", 0.5, 1, 0, 10);
    model.animate(oneC, "C");
    IAnimation twoR = new IAnimationImplScale(model, "R", 3, 4, 11, 20);
    model.animate(twoR, "R");
    IAnimation twoC = new IAnimationImplScale(model, "C", 1.5, 0.5, 20, 50);
    model.animate(twoC, "C");
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 1);
    view.play();
    assertEquals("Shapes:\n" +
            "Name: R\n" +
            "Type: rectangle\n" +
            "Corner: (0.0,0.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
            "Appears at t=1.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: C\n" +
            "Type: oval\n" +
            "Center: (500.0,100.0), X radius: 60.0, Y radius: 30.0, Color: (0.0,0.0,1.0)\n" +
            "Appears at t=6.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Shape R scales from Width: 50.0, Height: 100.0 to Width: 25.0, Height: 100.0 " +
            "from t=0.0s to t=10.0s\n" +
            "Shape C scales from X radius: 60.0, Y radius: 30.0 to X radius: 30.0, Y ra" +
            "dius: 30.0 from t=0.0s to t=10.0s\n" +
            "Shape R scales from Width: 25.0, Height: 100.0 to Width: 75.0, Height: 400" +
            ".0 from t=11.0s to t=20.0s\n" +
            "Shape C scales from X radius: 30.0, Y radius: 30.0 to X radius: 45.0, Y rad" +
            "ius: 15.0 from t=20.0s to t=50.0s", out.toString());
  }

  //tests changing color of an oval and rectangle multiple times
  @Test
  public void testColorChangeGetState() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 0,
            0, 1, 100, 50, 100);
    IFigure c = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1}, "C", 500, 100, 6,
            100, 60, 30);
    model.add(r);
    model.add(c);
    IAnimation oneR = new IAnimationImplColorChange(model, "R", new double[]{1, 1, 1}, 0, 10);
    model.animate(oneR, "R");
    IAnimation oneC = new IAnimationImplColorChange(model, "C", new double[]{1, 1, 1}, 0, 10);
    model.animate(oneC, "C");
    IAnimation twoR = new IAnimationImplColorChange(model, "R", new double[]{0, 0, 0}, 20, 30);
    model.animate(twoR, "R");
    IAnimation twoC = new IAnimationImplColorChange(model, "C", new double[]{0, 0, 0}, 23, 40);
    model.animate(twoC, "C");
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 1);
    view.play();
    assertEquals("Shapes:\n" +
                    "Name: R\n" +
                    "Type: rectangle\n" +
                    "Corner: (0.0,0.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
                    "Appears at t=1.0s\n" +
                    "Disappears at t=100.0s\n" +
                    "\n" +
                    "Name: C\n" +
                    "Type: oval\n" +
                    "Center: (500.0,100.0), X radius: 60.0, Y radius: 30.0" +
                    ", Color: (0.0,0.0,1.0)\n" +
                    "Appears at t=6.0s\n" +
                    "Disappears at t=100.0s\n" +
                    "\n" +
                    "Shape R changes color from (1.0,0.0,0.0) to (1.0,1.0,1.0) from t=0.0s" +
                    " to t=10.0s\n" +
                    "Shape C changes color from (0.0,0.0,1.0) to (1.0,1.0,1.0) from t=0.0s to" +
                    " t=10.0s\n" +
                    "Shape R changes color from (1.0,1.0,1.0) to (0.0,0.0,0.0) from t=20.0s " +
                    "to t=30.0s\n" +
                    "Shape C changes color from (1.0,1.0,1.0) to (0.0,0.0,0.0) from t=23.0s " +
                    "to t=40.0s",
            out.toString());

  }

  // tests scaling with an invalid factor
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidScale() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 0,
            0, 1, 100, 50, 100);
    model.add(r);
    IAnimation one = new IAnimationImplScale(model, "R", 0, -70, 10, 11);
    model.animate(one, "R");
  }

  // tests scaling with an invalid factor
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidScale2() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "oval", new double[]{1, 0, 0}, "R", 0,
            0, 1, 100, 50, 100);
    model.add(r);
    IAnimation one = new IAnimationImplScale(model, "R", -10, 0, 10, 11);
    model.animate(one, "R");
  }

  // tests scaling with two invalid factors
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidScale3() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "oval", new double[]{1, 0, 0}, "R", 0,
            0, 1, 100, 50, 100);
    model.add(r);
    IAnimation one = new IAnimationImplScale(model, "R", -80, -70, 10, 11);
    model.animate(one, "R");
  }

  // tests making a color change with an invalid color
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColorChange() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 0,
            0, 1, 100, 50, 100);
    model.add(r);
    IAnimation one = new IAnimationImplColorChange(model, "R", new double[]{10, 1, 0}, 50, 80);
    model.animate(one, "R");
  }

  // tests making a color change with an invalid color
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColorChange2() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "oval", new double[]{1, 0, 0}, "R", 0,
            0, 1, 100, 50, 100);
    model.add(r);
    IAnimation one = new IAnimationImplColorChange(model, "R", new double[]{-10, 1, 0}, 50, 80);
    model.animate(one, "R");
  }

  //tests adding the same animation to different shapes (move to rectangle and oval)
  @Test
  public void testSameAnimationsGetState() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 0,
            0, 1, 100, 50, 100);
    IFigure c = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1}, "C", 500, 100, 6,
            100, 60, 30);
    model.add(r);
    model.add(c);
    IAnimation oneR = new IAnimationImplMove(model, "R", 100, 100, 10, 50);
    model.animate(oneR, "R");
    IAnimation oneC = new IAnimationImplMove(model, "C", 100, 100, 10, 50);
    model.animate(oneC, "C");
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 1);
    view.play();
    assertEquals("Shapes:\n" +
            "Name: R\n" +
            "Type: rectangle\n" +
            "Corner: (0.0,0.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
            "Appears at t=1.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: C\n" +
            "Type: oval\n" +
            "Center: (500.0,100.0), X radius: 60.0, Y radius: 30.0, Color: (0.0,0.0,1.0)\n" +
            "Appears at t=6.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Shape R moves from (0.0,0.0) to (100.0,100.0) from t=10.0s to t=50.0s\n" +
            "Shape C moves from (500.0,100.0) to (600.0,200.0) from t=10.0s to t=50.0s" +
            "", out.toString());

  }

  // tests adding 2 of the same shape
  @Test(expected = IllegalArgumentException.class)
  public void testSameShapesGetState() {
    IModel model = new IModelImpl();
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 1);
    view.play();
    assertEquals("Shapes:", out.toString());
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 0,
            0, 1, 100, 50, 100);
    model.add(r);
    assertEquals("Shapes:", out.toString());
    model.add(r);
    assertEquals("Shapes:\n" +
            "Name: R\n" +
            "Type: rectangle\n" +
            "Corner: (0.0,0.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
            "Appears at t=1\n" +
            "Disappears at t=100\n" +
            "\n" +
            "Name: R\n" +
            "Type: rectangle\n" +
            "Corner: (0.0,0.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
            "Appears at t=1\n" +
            "Disappears at t=100\n", out.toString());
  }

  // tests an empty canvas
  @Test
  public void testEmptyGetState() {
    IModel model = new IModelImpl();
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 1);
    view.play();
    assertEquals("Shapes:", out.toString());
  }

  //tests retrieving an IFigure that does not exist
  @Test(expected = IllegalArgumentException.class)
  public void testDNEGetFigure() {
    IModel model = new IModelImpl();
    model.getFigure("X");
  }

  //tests empty string input for getFigure
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyGetFigure() {
    IModel model = new IModelImpl();
    model.getFigure("");
  }

  // tests for empty string to animate
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyAnimate() {
    IModel model = new IModelImpl();
    IAnimation one = new IAnimationImplMove(model, "R", 100, 100, 10, 50);
    model.animate(one, "");
  }

  // tests for animation to IFigure that does not exist
  @Test(expected = IllegalArgumentException.class)
  public void testDNEAnimate() {
    IModel model = new IModelImpl();
    IAnimation one = new IAnimationImplMove(model, "R", 100, 100, 10, 50);
    model.animate(one, "R");
  }

  // tests for one null input exception for animate method
  @Test(expected = IllegalArgumentException.class)
  public void testNullAnimate1() {
    IModel model = new IModelImpl();
    IAnimation one = new IAnimationImplMove(model, "R", 100, 100, 10, 50);
    model.animate(one, null);
  }

  // tests for one null input exception for animate method
  @Test(expected = IllegalArgumentException.class)
  public void testNullAnimate2() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 200,
            200, 1, 100, 50, 100);
    model.animate(null, "R");
  }

  // tests for double null input exception for animate method
  @Test(expected = IllegalArgumentException.class)
  public void testNullAnimate3() {
    IModel model = new IModelImpl();
    model.animate(null, null);
  }


  // tests for null input exception for getFigure method
  @Test(expected = IllegalArgumentException.class)
  public void testNullGetFigure() {
    IModel model = new IModelImpl();
    model.getFigure(null);
  }

  // tests for null input exception for add method
  @Test(expected = IllegalArgumentException.class)
  public void testNullAdd() {
    IModel model = new IModelImpl();
    model.add(null);
  }

  // tests exception for competing move animations on the same object
  @Test(expected = IllegalArgumentException.class)
  public void testCompetingAnimationsMove() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 200,
            200, 1, 100, 50, 100);
    model.add(r);
    IAnimation one = new IAnimationImplMove(model, "R", 100, 100, 10, 50);
    model.animate(one, "R");
    IAnimation two = new IAnimationImplMove(model, "R", 50, 50, 20, 50);
    model.animate(two, "R");
  }

  // tests exception for competing color animations on the same object starting the second one ends
  @Test(expected = IllegalArgumentException.class)
  public void testCompetingAnimationsColor() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 200,
            200, 1, 100, 50, 100);
    model.add(r);
    IAnimation one = new IAnimationImplColorChange(model, "R", new double[]{0, 1, 1}, 10, 50);
    model.animate(one, "R");
    IAnimation two = new IAnimationImplColorChange(model, "R", new double[]{0, 1, 1}, 50, 50);
    model.animate(two, "R");
  }

  // tests exception for competing color animations on the same object ending the second one starts
  @Test(expected = IllegalArgumentException.class)
  public void testCompetingAnimationsScale() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 200,
            200, 1, 100, 50, 100);
    model.add(r);
    IAnimation one = new IAnimationImplScale(model, "R", 0.5, 1, 51, 70);
    model.animate(one, "R");
    IAnimation two = new IAnimationImplScale(model, "R", 0.5, 1, 20, 51);
    model.animate(two, "R");
  }

  // tests example given in assignment description
  @Test
  public void testPrintingState() {
    IModel model = new IModelImpl();
    IFigure r = new IFigureImplDummy(model, "rectangle", new double[]{1, 0, 0}, "R", 200,
            200, 1, 100, 50, 100);
    IFigure c = new IFigureImplDummy(model, "oval", new double[]{0, 0, 1}, "C", 500, 100, 6,
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
    Appendable out = new StringBuffer();
    IView view = new IViewImplTextSummary(model, out, 1);
    view.play();
    assertEquals("Shapes:\n" +
            "Name: R\n" +
            "Type: rectangle\n" +
            "Corner: (200.0,200.0), Width: 50.0, Height: 100.0, Color: (1.0,0.0,0.0)\n" +
            "Appears at t=1.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Name: C\n" +
            "Type: oval\n" +
            "Center: (500.0,100.0), X radius: 60.0, Y radius: 30.0, Color: (0.0,0.0,1.0)\n" +
            "Appears at t=6.0s\n" +
            "Disappears at t=100.0s\n" +
            "\n" +
            "Shape R moves from (200.0,200.0) to (300.0,300.0) from t=10.0s to t=50.0s\n" +
            "Shape C moves from (500.0,100.0) to (500.0,400.0) from t=20.0s to t=70.0s\n" +
            "Shape C changes color from (0.0,0.0,1.0) to (0.0,1.0,0.0) from t=50." +
            "0s to t=80.0s\n" +
            "Shape R scales from Width: 50.0, Height: 100.0 to Width: 25.0, Height: 100." +
            "0 from t=51.0s to t=70.0s\n" +
            "Shape R moves from (300.0,300.0) to (200.0,200.0) from t=70.0s to t=1" +
            "00.0s", out.toString());
  }
}



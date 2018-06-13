import org.junit.Test;

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

import static org.junit.Assert.assertEquals;

public class IViewImplTextSummaryTest {

  @Test
  public void testPrintSummary() {
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
}

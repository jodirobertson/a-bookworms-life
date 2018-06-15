package cs3500.animator.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;

import cs3500.animator.model.IAnimation;
import cs3500.animator.model.IAnimationImplColorChange;
import cs3500.animator.model.IAnimationImplMove;
import cs3500.animator.model.IAnimationImplScale;
import cs3500.animator.model.IModel;
import cs3500.animator.model.IFigure;

/**
 * Represents an IView that draws using JavaSwing.
 */
public class IViewImplVisualAnimation extends JFrame implements IView {
  private MyPanel drawingPanel;
  private IModel model;
  private Timer timer;
  private int time = 0;
  private ArrayList<ArrayList<IFigure>> animationShapes = new ArrayList<>();
  // keeps a hashmap of the figure to its most up-to-date version during the animation process
  // INVARIANT: maintains updated throughout animation composition process
  private HashMap<String, IFigure> upToDateFigs = new HashMap<>();

  /**
   * Consutructs an IView that uses JavaSwing to draw its composition.
   *
   * @param model The given IModel to be drawn.
   * @param tempo The given tempo at which to play the animation.
   */
  public IViewImplVisualAnimation(IModel model, double tempo) {
    this.model = model;
    this.timer = new Timer((int) (10 / tempo), null);

    timer.addActionListener(new ActionListener() {

                              @Override
                              public void actionPerformed(ActionEvent e) {
                                drawingPanel.drawShapes(animationShapes.get(time));
                                drawingPanel.repaint();
                                if (time == model.maxTime()) {
                                  timer.stop();
                                }
                                time++;
                              }
                            }
    );

    for (int i = 0; i <= model.maxTime(); i++) {
      this.animationShapes.add(this.draw(i));
    }

    setTitle("Visual Animation");
    setSize(800, 800);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    drawingPanel = new MyPanel();
    drawingPanel.setPreferredSize(this.getSize());
    this.add(drawingPanel);

    JScrollPane scrollPane = new JScrollPane(drawingPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.add(scrollPane);
  }

  /**
   * Returns a list of IFigures to be drawn at a given time.
   *
   * @param time The given time to be queried for figures to draw.
   * @return Returns an ArrayList of IFigures to be drawn at a given time, according to when they
   * were first created.
   */
  private ArrayList<IFigure> draw(int time) {
    ArrayList<IFigure> result = new ArrayList<>();

    // check all figures based on when they were added
    for (IFigure f : model.getFiguresByAdd()) {
      IFigure figInProgress = f;
      this.upToDateFigs.put(f.getName(), f);
      // if the figure is visible
      if (f.getAppearTime() <= time && f.getDisappearTime() >= time) {
        // go through all animations to see which apply to this figure
        for (IAnimation a : model.getAnimationsByStartTime()) {
          // if this animation is applied to the given figure
          if (a.getFigure().getName().equals(f.getName())
                  && (a.getStartTime() <= time) && (a.getEndTime() > time)) {
            // update accordingly
            figInProgress = tween(a, this.upToDateFigs.get(f.getName()), time);
            this.upToDateFigs.put(figInProgress.getName(), figInProgress);
          }
          // if the figure has already had that animation completed, update the data to hold newest
          else if (a.getFigure().getName().equals(f.getName()) && a.getEndTime() <= time) {
            this.upToDateFigs.put(f.getName(), a.accept(this.upToDateFigs.get(f.getName())));
          }
        }
        // add the appearing figure to the list to be drawn and update data accordingly
        result.add(this.upToDateFigs.get(figInProgress.getName()));
      }
    }
    return result;
  }

  /**
   * Finds the intermediate step in two given values, a and b, based on their respective time of
   * start, ta and tb, and when the given time to be queried.
   *
   * @param a  The initial value.
   * @param b  The final value.
   * @param ta The time at which the initial value starts.
   * @param tb The time at which the final value is reached.
   * @param t  The given time to be queried.
   * @return The value at the given time, between ta and tb, and values a and b.
   */
  private double interpolate(double a, double b, int ta, int tb, int t) {
    return ((a * (tb - t) / (tb - ta)) + (b * (t - ta) / (tb - ta)));
  }

  /**
   * Returns the interpolated, animated IFigure at a given time.
   *
   * @param a    The animation to be applied.
   * @param f    The figure to which a should be applied.
   * @param time The time at which the animations is occuring.
   * @return Returns an IFigure at a given time during an IAnimation.
   */
  private IFigure updateMove(IAnimation a, IFigure f, int time) {
    double oldOldX = a.getFigure().getXPos();
    double oldOldY = a.getFigure().getYPos();
    double oldNewX = a.getDX() + oldOldX;
    double oldNewY = a.getDY() + oldOldY;
    double updatedDX = interpolate(oldOldX, oldNewX,
            a.getStartTime(), a.getEndTime(), time) - oldOldX;
    double updatedDY = interpolate(oldOldY, oldNewY,
            a.getStartTime(), a.getEndTime(), time) - oldOldY;
    return new IAnimationImplMove(model, f.getName(),
            updatedDX, updatedDY, a.getStartTime(), a.getEndTime()).accept(f);
  }

  /**
   * Returns the interpolated, animated IFigure at a given time.
   *
   * @param a    The animation to be applied.
   * @param f    The figure to which a should be applied.
   * @param time The time at which the animations is occuring.
   * @return Returns an IFigure at a given time during an IAnimation.
   */
  private IFigure updateScale(IAnimation a, IFigure f, int time) {
    double oldOldXDim = f.getXDim();
    double oldOldYDim = f.getYDim();
    double oldNewXDim = a.getXFactor() * oldOldXDim;
    double oldNewYDim = a.getYFactor() * oldOldYDim;
    double updatedXFactor = interpolate(oldOldXDim,
            oldNewXDim, a.getStartTime(), a.getEndTime(), time) / oldOldXDim;
    double updatedYFactor = interpolate(oldOldYDim,
            oldNewYDim, a.getStartTime(), a.getEndTime(), time) / oldOldYDim;
    return new IAnimationImplScale(model, f.getName(),
            updatedXFactor, updatedYFactor, a.getStartTime(), a.getEndTime()).accept(f);
  }

  /**
   * Returns the interpolated, animated IFigure at a given time.
   *
   * @param a    The animation to be applied.
   * @param f    The figure to which a should be applied.
   * @param time The time at which the animations is occuring.
   * @return Returns an IFigure at a given time during an IAnimation.
   */
  private IFigure updateColor(IAnimation a, IFigure f, int time) {
    double oldR = f.getColor()[0];
    double newR = a.getNewColor()[0];
    double oldG = f.getColor()[1];
    double newG = a.getNewColor()[1];
    double oldB = f.getColor()[2];
    double newB = a.getNewColor()[2];
    double[] updatedColor = new double[]
            {interpolate(oldR, newR, a.getStartTime(), a.getEndTime(), time),
                    interpolate(oldG, newG, a.getStartTime(), a.getEndTime(), time),
                    interpolate(oldB, newB, a.getStartTime(), a.getEndTime(), time)};
    return new IAnimationImplColorChange(model, f.getName(), updatedColor,
            a.getStartTime(), a.getEndTime()).accept(f);
  }

  /**
   * Returns the interpolated, animated IFigure at a given time.
   *
   * @param a The animation to be applied.
   * @param f The figure to which a should be applied.
   * @param t The time at which the animations is occuring.
   * @return Returns an IFigure at a given time during an IAnimation.
   */
  private IFigure tween(IAnimation a, IFigure f, int t) {
    switch (a.getAnimType()) {
      case "moves":
        return updateMove(a, f, t);
      case "scales":
        return updateScale(a, f, t);
      case "changes color":
        return updateColor(a, f, t);
      default:
        throw new IllegalArgumentException("No such IAnimation.");
    }
  }

  @Override
  public void play() {
    setVisible(true);
    timer.restart();
  }
}

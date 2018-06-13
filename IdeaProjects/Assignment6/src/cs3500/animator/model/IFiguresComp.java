package cs3500.animator.model;

import java.util.Comparator;

/**
 * Represent the comparison of two figures based on when they appear in the scene.
 */
public class IFiguresComp implements Comparator<IFigure> {

  @Override
  public int compare(IFigure first, IFigure second) {
    return first.getAppearTime() - second.getAppearTime();
  }
}

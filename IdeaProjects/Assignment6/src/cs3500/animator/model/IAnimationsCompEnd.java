package cs3500.animator.model;

import java.util.Comparator;

/**
 * Represents the comparison of two animations based on when they end in the scene.
 */
public class IAnimationsCompEnd implements Comparator<IAnimation> {

  @Override
  public int compare(IAnimation first, IAnimation second) {
    return first.getEndTime() - second.getEndTime();
  }
}

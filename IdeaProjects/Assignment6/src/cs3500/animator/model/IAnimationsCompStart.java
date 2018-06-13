package cs3500.animator.model;

import java.util.Comparator;

/**
 * Represents the comparison of two animations based on when they start in the scene.
 */
public class IAnimationsCompStart implements Comparator<IAnimation> {

  @Override
  public int compare(IAnimation first, IAnimation second) {
    return first.getStartTime() - second.getStartTime();
  }
}
package cs3500.animator.view;

import cs3500.animator.model.IModel;

/**
 * Represents the shared data and methods among the IViewImpl's.
 */
abstract class IViewAbstract implements IView {
  IModel model;
  double tick;

  /**
   * Default constructor for IViewAbstract.
   */
  IViewAbstract() {
  }

  /**
   * Ensures that the given tick rate is greater than zero.
   *
   * @throws IllegalArgumentException if the tick is less than or equal to zero.
   */
  void checkTick() throws IllegalArgumentException {
    if (this.tick <= 0) {
      throw new IllegalArgumentException("Tick must be greater than zero.");
    }
  }
}

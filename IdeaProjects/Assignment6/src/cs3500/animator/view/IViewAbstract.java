package cs3500.animator.view;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import cs3500.animator.model.IModel;

/**
 * Represents the shared data and methods among the IViewImpl's.
 */
abstract class IViewAbstract implements IView {
  IModel model;
  double tick;
  NumberFormat f = new DecimalFormat("#0.0");

  /**
   * Default constructor for IViewAbstract.
   */
  IViewAbstract() {
    // default constructor
  }

  /**
   * Requires that the given input is not null.
   *
   * @throws IllegalArgumentException if the given object is null.
   */
  void ensureNotNull(Object o) {
    if (o == null) {
      throw new IllegalArgumentException("Given argument is null.");
    }
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

  /**
   * Appends given string argument to the specified output. Catches an IOException if the output
   * cannot be appended to.
   *
   * @param out The specified Appendable output for the SVG view.
   * @param s   The desired String to be appended.
   * @throws IllegalArgumentException if the output cannot be appended to.
   */
  void appendHelper(Appendable out, String s) throws IllegalArgumentException {
    try {
      out.append(s);
    } catch (IOException e) {
      throw new IllegalArgumentException("Could not be appended to output");
    }
  }
}

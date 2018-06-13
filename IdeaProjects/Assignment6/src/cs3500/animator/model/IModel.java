package cs3500.animator.model;

import java.util.HashMap;
import java.util.List;

/**
 * Represents the model for constructing a composition of IFigures and IAnimations.
 */
public interface IModel {

  /**
   * Updates the scene to include the given IFigure.
   *
   * @param f The IFigure to be added to the scene.
   * @throws IllegalArgumentException If a null input is given, if an IFigure with the given name
   *                                  already exists, or .
   */
  void add(IFigure f) throws IllegalArgumentException;

  /**
   * Updates the scene to include an IAnimation on the IFigure given by name.
   *
   * @param a     The IAnimation to be added.
   * @param fName The name of the IFigure to be animated.
   * @throws IllegalArgumentException If the IAnimation competes with an existing one applied to the
   *                                  IFigure, if either input is null, or if the given IFigure does
   *                                  not exist.
   */
  void animate(IAnimation a, String fName) throws IllegalArgumentException;

  /**
   * Returns the current IFigure with the given name.
   *
   * @param fName The name of the IFigure to be retrieved.
   * @return The IFigure to be retrieved.
   * @throws IllegalArgumentException If given input is null or does not exist.
   */
  IFigure getFigure(String fName) throws IllegalArgumentException;

  //TODO: JAvadocs!
  List<IFigure> getFiguresByStartTime();

  List<IAnimation> getAnimationsByStartTime();

  HashMap<IAnimation, String> getMasterList();

  HashMap<String, IFigure> getUpdatedFigures();
}

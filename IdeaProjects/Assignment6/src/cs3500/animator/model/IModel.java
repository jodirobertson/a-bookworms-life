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

  /**
   * Returns a the Hashmap that stores which animations are occuring on each shape at a time.
   *
   * @return Returns a Hashmap of the String (name of IFigure) to a list of all IAnimations which
   * are applied to that shape
   */
  HashMap<String, List<IAnimation>> getAnimationsByShape();

  /**
   * Returns a list of all IFigures in the composition, ordered by their appear times.
   *
   * @return Returns a list of all IFigures in the composition, ordered by their appear times.
   */
  List<IFigure> getFiguresByStartTime();

  /**
   * Returns a list of all IAnimations in the composition, ordered by their appear times.
   *
   * @return Returns a list of all IAnimations in the composition, ordered by their appear times.
   */
  List<IAnimation> getAnimationsByStartTime();

  /**
   * Returns a HashMap that couples the IAnimations to the IFigures.
   *
   * @return Returns a HashMap that couples every IAnimation to the name of an IFigure.
   */
  HashMap<IAnimation, String> getMasterList();

  /**
   * Returns a Hashmap of the name of an IFigure in the composition to its current, animated version
   * of the IFigure of the same unique name.
   *
   * @return Returns a Hashmap of the IFigure that is most updated with IAnimations, retrieved by
   * its name.
   */
  HashMap<String, IFigure> getUpdatedFigures();

  /**
   * Returns a List of all IFigures added to the composition, based on the order they are added to
   * the composition.
   *
   * @return Returns a LinkedList of IFigures in ascending order of when they are added to the
   * composition.
   */
  List<IFigure> getFiguresByAdd();
}

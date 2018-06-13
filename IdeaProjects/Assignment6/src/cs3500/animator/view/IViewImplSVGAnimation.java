package cs3500.animator.view;

import cs3500.animator.model.IModel;

/**
 * Represents an IView that produces an output of the composition in an SVG file format.
 */
public class IViewImplSVGAnimation extends IViewAbstract implements IView {
  private Appendable output;


  public IViewImplSVGAnimation(IModel model, Appendable output, double tick) {
    super();
    super.model = model;
    super.tick = tick;
    this.output = output;
  }


  @Override
  public void play() {

  }
}

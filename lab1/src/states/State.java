package states;

import shapes.Shape;

import java.awt.event.MouseEvent;
import java.util.List;

public interface State {
    default void onClick(MouseEvent event) {
    }

    default void onClick(List<Shape> shapes, MouseEvent event) {
    }
}
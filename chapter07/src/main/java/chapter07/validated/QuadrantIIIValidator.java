package chapter07.validated;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * ConstraintValidator: Defines the logic to validate a given constraint A for a given object type T.
 * Implementations must comply to the following restriction:
 */
public class QuadrantIIIValidator implements ConstraintValidator<NoQuadrantIII, Coordinate> {
    @Override
    public void initialize(NoQuadrantIII constraintAnnotation) {
    }

    /**
     *
     * @param value Coordinate
     * @param context Provides contextual data and operation when applying a given constraint validator.
     * @return
     */
    @Override
    public boolean isValid(Coordinate value, ConstraintValidatorContext context) {
        return !(value.getX() < 0 && value.getY() < 0);
    }
}

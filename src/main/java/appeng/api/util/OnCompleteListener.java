package appeng.api.util;

import java.io.Serializable;

@FunctionalInterface
public interface OnCompleteListener<A1, A2, A3> extends Serializable {

    long serialVersionUID = 734594276097234589L;

    /**
     * Applies this function to the given arguments.
     *
     * @param a1 the first function argument
     * @param a2 the second function argument
     * @param a3 the third function argument
     * @return the function result
     */
    void apply(A1 a1, A2 a2, A3 a3);
}

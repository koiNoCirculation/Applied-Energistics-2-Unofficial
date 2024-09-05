package appeng.api.util;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * consumes an integer, if it's larger than 0, it means the crafting is not stucked.
 */
@FunctionalInterface
public interface CraftingStatusListener<T> extends Consumer<T>, Serializable {

    long serialVersionUID = 83482599346L;
}

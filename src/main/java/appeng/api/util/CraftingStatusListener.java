package appeng.api.util;

import java.io.Serializable;
import java.util.function.Consumer;

@FunctionalInterface
public interface CraftingStatusListener<T> extends Consumer<T>, Serializable {

    long serialVersionUID = 83482599346L;
}

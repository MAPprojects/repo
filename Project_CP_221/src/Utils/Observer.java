package Utils;

import java.util.ArrayList;

public interface Observer<E>  {
    void notify(E el);
}

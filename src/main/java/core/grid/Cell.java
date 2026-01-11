package core.grid;

import core.value.EmptyValue;
import core.value.Value;
import lombok.*;

@Data
@AllArgsConstructor
public class Cell {
    private Value value;

    public void clear(){
        value = new EmptyValue();
    }
}

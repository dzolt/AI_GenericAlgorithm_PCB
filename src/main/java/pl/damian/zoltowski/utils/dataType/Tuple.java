package pl.damian.zoltowski.utils.dataType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tuple<X, Y> {
    public X first;
    public Y second;

    @Override
    public String toString() {
        return "<" + this.first + ", " + this.second + ">";
    }
}

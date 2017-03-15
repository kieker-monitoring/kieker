package kieker.analysisteetime.util;

import com.google.common.base.Objects;

public final class ComposedKey<F,S> {

	private final F first;
	private final S second;

    private ComposedKey(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ComposedKey)) {
            return false;
        }
        ComposedKey<?, ?> p = (ComposedKey<?, ?>) o;
        return Objects.equal(p.first, first) && Objects.equal(p.second, second);
    }

    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    public static <F, S> ComposedKey<F, S> of(F first, S second) {
        return new ComposedKey<F, S>(first, second);
    }
	
}

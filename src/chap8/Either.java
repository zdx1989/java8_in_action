package chap8;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Either<L, R> {

    private final L left;

    private final R right;

    private Either(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public static<R> Either<?, R> right(R r) {
        Objects.requireNonNull(r);
        return new Either<>(null, r);
    }

    public static<L> Either<L, ?> left(L l) {
        Objects.requireNonNull(l);
        return new Either<>(l, null);
    }

    public boolean isLeft() {
        return left != null;
    }

    public boolean isRight() {
        return right != null;
    }

    public R getRight() {
        return right;
    }

    public L getLeft() {
        return left;
    }

    public Either<?, R> filter(Predicate<? super R> predicate) {
        Objects.requireNonNull(predicate);
        if (isLeft()) return this;
        else return predicate.test(right) ? this: null;
    }

    public <A> Either<?, A> map(Function<? super R, A> function) {
        Objects.requireNonNull(function);
        if (isLeft()) return new Either<>(left, null);
        else return new Either<>(null, function.apply(right));
    }

    public <A> Either<?, A> flatMap(Function<? super R, Either<?, A>> function) {
        Objects.requireNonNull(function);
        if (isLeft()) return new Either<>(left, null);
        else return function.apply(right);
    }

}

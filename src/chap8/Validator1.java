package chap8;

public interface Validator1<T, R> {

    Either<Boolean, T> validate(T t);
}

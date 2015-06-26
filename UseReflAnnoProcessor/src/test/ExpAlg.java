package test;

import java.util.ArrayList;
import java.util.List;

import library.ReflAlg;
import annotation.Refl;

@Refl
public interface ExpAlg<E> {
    E Lit(int x);
    E Add(E e1, E e2);
    E Var(String s);
}
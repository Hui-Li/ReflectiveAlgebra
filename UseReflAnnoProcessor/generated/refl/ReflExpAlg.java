package refl;

import java.util.List;
import java.util.ArrayList;
import library.ReflAlg;
import test.ExpAlg;

public abstract class ReflExpAlg<E> implements ExpAlg<E> {

	abstract ReflAlg<E> alg();

	public E Add(E p0, E p1) {
		List<E> l = new ArrayList<E>();
		l.add(p0);
		l.add(p1);
		return alg().Cons("Add", l);
	}

	public E Lit(int p0) {
		List<E> l = new ArrayList<E>();
		l.add(alg().KInt(p0));
		return alg().Cons("Lit", l);
	}

	public E Var(java.lang.String p0) {
		List<E> l = new ArrayList<E>();
		l.add(alg().KString(p0));
		return alg().Cons("Var", l);
	}

}
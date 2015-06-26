package refl;

import test.ExpAlg;

public final class ExpAlgGen implements ExpAlg<ExpAlgGen.Type> {
	public interface Type {
		<E> E accept(ExpAlg<E> alg);
	}

	public Type Add(Type p0, Type p1) {
		return new Type() {
			public <E> E accept(ExpAlg<E> alg) { 
				return alg.Add(p0.accept(alg), p1.accept(alg));
			}
		};
	}

	public Type Lit(int p0) {
		return new Type() {
			public <E> E accept(ExpAlg<E> alg) { 
				return alg.Lit(p0);
			}
		};
	}

	public Type Var(java.lang.String p0) {
		return new Type() {
			public <E> E accept(ExpAlg<E> alg) { 
				return alg.Var(p0);
			}
		};
	}

}
package com.renpe.intellij.sagemath.lang

/**
 * Sage category/type names exposed at the top level of `sage.all`.
 *
 * This is a curated baseline list that ships with the plugin. To get a
 * complete, version-accurate list for your local Sage install, run
 * `sage -python scripts/generate_builtins.py` — that regenerates this
 * file from `dir(sage.all)`.
 */
internal object SagemathTypes {

    val NAMES: Set<String> = setOf(
        // ---- Number systems / rings / fields ----------------------------
        "Integer", "Rational", "RealNumber", "ComplexNumber", "RealDoubleElement",
        "ComplexDoubleElement", "RealIntervalFieldElement", "ComplexIntervalFieldElement",
        "RealBallField", "ComplexBallField", "AlgebraicNumber", "AlgebraicReal",
        "AlgebraicField", "AlgebraicRealField", "QQbar", "AA",
        "IntegerRing", "RationalField", "RealField", "ComplexField",
        "RealDoubleField", "ComplexDoubleField", "RealIntervalField", "ComplexIntervalField",
        "FiniteField", "GaloisField", "PrimeField", "IntegerModRing",
        "NumberField", "QuadraticField", "CyclotomicField", "PadicField",
        "Qp", "Zp", "Qq", "Zq", "PowerSeriesRing", "LaurentSeriesRing",
        "LaurentPolynomialRing", "PolynomialRing", "MultivariatePolynomialRing",
        "FractionField", "QuotientRing", "ResidueField",

        // ---- Linear algebra ---------------------------------------------
        "Matrix", "MatrixSpace", "Vector", "VectorSpace", "FreeModule",
        "FreeModuleElement", "Matrix_dense", "Matrix_sparse",
        "CombinatorialFreeModule", "ModulesWithBasis",

        // ---- Polynomials / symbolic -------------------------------------
        "Polynomial", "MPolynomial", "LaurentPolynomial", "PowerSeries",
        "SymbolicRing", "SymbolicExpression", "SymbolicVariable",
        "SR", "Expression",

        // ---- Groups -----------------------------------------------------
        "AbelianGroup", "AdditiveAbelianGroup", "AlternatingGroup", "BraidGroup",
        "CyclicPermutationGroup", "DihedralGroup", "DiCyclicGroup",
        "FreeGroup", "GeneralLinearGroup", "GL", "SpecialLinearGroup", "SL",
        "OrthogonalGroup", "GO", "SO", "SymplecticGroup", "Sp",
        "UnitaryGroup", "GU", "SU", "PermutationGroup", "MathieuGroup",
        "SymmetricGroup", "TransitiveGroup", "WeylGroup", "CoxeterGroup",
        "AffineGroup", "ProjectiveLinearGroup", "PGL", "ProjectiveSpecialLinearGroup", "PSL",
        "MatrixGroup", "FinitelyPresentedGroup",

        // ---- Combinatorics ----------------------------------------------
        "Permutation", "Permutations", "Combinations", "Arrangements",
        "Partition", "Partitions", "OrderedPartition", "OrderedPartitions",
        "Composition", "Compositions", "SetPartition", "SetPartitions",
        "Tableau", "Tableaux", "StandardTableau", "StandardTableaux",
        "SemistandardTableau", "SemistandardTableaux", "SkewTableau", "SkewTableaux",
        "Word", "Words", "FiniteWord", "InfiniteWord",
        "RootSystem", "CartanType", "WeylCharacterRing", "DynkinDiagram",
        "CrystalOfTableaux", "Subsets", "Tuples", "UnorderedTuples",

        // ---- Graphs / combinatorial structures --------------------------
        "Graph", "DiGraph", "BipartiteGraph", "Hypergraph",
        "Poset", "FinitePoset", "LatticePoset", "MeetSemilattice", "JoinSemilattice",
        "SimplicialComplex", "DeltaComplex", "CubicalComplex",
        "Matroid", "ChainComplex", "CombinatorialPolyhedron",

        // ---- Geometry / schemes / varieties -----------------------------
        "AffineSpace", "ProjectiveSpace", "ProductProjectiveSpaces", "ToricVariety",
        "EllipticCurve", "EllipticCurveIsogeny", "EllipticCurveFromPlaneCubic",
        "HyperellipticCurve", "Curve", "Surface", "Scheme", "AffineScheme",
        "ProjectiveScheme", "AlgebraicScheme", "Variety", "Cone", "Fan",
        "Polyhedron", "LatticePolytope", "LatticePolytope3d",

        // ---- Modular forms / number theory ------------------------------
        "ModularForms", "ModularSymbols", "CuspForms", "EisensteinForms",
        "Newforms", "ModularAbelianVariety", "Gamma0", "Gamma1", "GammaH",
        "DirichletGroup", "DirichletCharacter", "Hecke",

        // ---- Categories -------------------------------------------------
        "Category", "Categories", "Modules", "Rings", "Fields", "Algebras",
        "Groups", "Monoids", "Semigroups", "Sets", "Posets", "Lattices",
        "VectorSpaces", "Hom", "End",

        // ---- Manifolds / differential geometry --------------------------
        "Manifold", "DifferentiableManifold", "TopologicalManifold",
        "RealLine", "EuclideanSpace", "Sphere",

        // ---- Crypto / coding theory -------------------------------------
        "LinearCode", "BinaryCode", "BCHCode", "HammingCode", "ReedMullerCode",
        "ReedSolomonCode", "CyclicCode",

        // ---- Misc -------------------------------------------------------
        "Set", "EnumeratedSet", "FiniteEnumeratedSet", "InfiniteEnumeratedSet",
        "RecursivelyEnumeratedSet", "Family", "Sequence",
        "Function", "PiecewiseFunction", "Sequence_generic",
    )
}

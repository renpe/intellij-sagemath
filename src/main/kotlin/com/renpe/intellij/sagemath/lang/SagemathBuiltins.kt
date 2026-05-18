package com.renpe.intellij.sagemath.lang

/**
 * Sage built-in function and value names exposed at the top level of
 * `sage.all`.
 *
 * This is a curated baseline list that ships with the plugin so syntax
 * highlighting is useful out of the box. The Sage standard library is
 * vastly larger than this — for a complete, version-accurate list, run
 *
 *     sage -python scripts/generate_builtins.py
 *
 * which walks `dir(sage.all)` on your local Sage install and rewrites
 * this file.
 */
internal object SagemathBuiltins {

    val NAMES: Set<String> = setOf(
        // ---- Ring/field shortcuts (lowercased aliases) ------------------
        "ZZ", "QQ", "RR", "CC", "RDF", "CDF", "RIF", "CIF", "RBF", "CBF",
        "GF", "Zmod", "Integers", "NN", "Primes", "SR",

        // ---- Constructors / factories -----------------------------------
        "matrix", "vector", "identity_matrix", "zero_matrix", "ones_matrix",
        "diagonal_matrix", "block_matrix", "block_diagonal_matrix",
        "random_matrix", "random_vector", "elementary_matrix",
        "column_matrix", "companion_matrix", "jordan_block",
        "polygen", "polygens", "var", "function", "piecewise",

        // ---- Number theory ----------------------------------------------
        "factor", "factorial", "binomial", "multinomial", "falling_factorial",
        "rising_factorial", "fibonacci", "lucas_number1", "lucas_number2",
        "bell_number", "bernoulli", "euler_number",
        "gcd", "lcm", "xgcd", "inverse_mod", "power_mod", "crt", "CRT",
        "is_prime", "is_prime_power", "is_pseudoprime", "is_square",
        "is_squarefree", "is_power_of_two", "is_perfect_power",
        "next_prime", "previous_prime", "next_probable_prime",
        "primes", "prime_range", "primes_first_n", "prime_pi", "prime_powers",
        "divisors", "number_of_divisors", "sigma", "euler_phi", "moebius",
        "kronecker_symbol", "kronecker", "jacobi_symbol", "legendre_symbol",
        "hilbert_symbol", "discrete_log", "discrete_log_lambda",
        "continued_fraction", "convergents",
        "valuation", "ord", "radical", "squarefree_part",

        // ---- Elementary functions ---------------------------------------
        "abs", "sign", "sqrt", "exp", "log", "ln", "logb", "log2",
        "sin", "cos", "tan", "cot", "sec", "csc",
        "asin", "acos", "atan", "acot", "asec", "acsc", "atan2",
        "sinh", "cosh", "tanh", "coth", "sech", "csch",
        "asinh", "acosh", "atanh", "acoth", "asech", "acsch",
        "real", "imag", "conjugate", "norm", "arg", "real_part", "imag_part",
        "ceil", "floor", "round", "trunc", "frac",
        "min", "max", "minmax",

        // ---- Special functions ------------------------------------------
        "gamma", "lngamma", "log_gamma", "digamma", "beta", "zeta",
        "dirichlet_eta", "hurwitz_zeta", "polylog", "dilog",
        "exp_integral_e", "exp_integral_ei", "log_integral", "log_integral_offset",
        "sinh_integral", "cosh_integral", "sin_integral", "cos_integral",
        "erf", "erfc", "erfi", "erfinv", "fresnel_sin", "fresnel_cos",
        "airy_ai", "airy_bi", "bessel_J", "bessel_Y", "bessel_I", "bessel_K",
        "hypergeometric", "hypergeometric_M", "hypergeometric_U",
        "elliptic_e", "elliptic_f", "elliptic_kc", "elliptic_pi",
        "jacobi", "jacobi_am", "jacobi_cn", "jacobi_dn", "jacobi_sn",
        "weierstrass_p",

        // ---- Symbolic calculus ------------------------------------------
        "diff", "derivative", "integral", "integrate", "limit", "lim",
        "sum", "prod", "product", "series", "taylor",
        "expand", "factor_list", "simplify", "simplify_full",
        "simplify_trig", "simplify_log", "simplify_exp", "simplify_radical",
        "collect", "collect_common_factors", "combine", "expand_log",
        "expand_trig", "trig_simplify", "trig_expand", "trig_reduce",
        "solve", "solve_mod", "solve_diophantine", "find_root", "find_fit",
        "find_local_maximum", "find_local_minimum", "find_maximum", "find_minimum",
        "desolve", "desolve_system", "desolve_laplace", "desolve_rk4", "desolve_odeint",
        "laplace", "inverse_laplace", "fourier", "inverse_fourier",
        "assume", "forget", "assumptions",
        "maxima", "maxima_calculus", "maxima_console", "mathematica", "mathematica_console",
        "sympy", "sympify",

        // ---- Linear algebra helpers -------------------------------------
        "det", "trace", "rank", "nullity", "kernel", "image", "transpose",
        "adjoint", "minors", "characteristic_polynomial", "minimal_polynomial",
        "eigenvalues", "eigenvectors_left", "eigenvectors_right",
        "left_eigenvectors", "right_eigenvectors", "jordan_form",
        "echelon_form", "rref", "smith_form", "hermite_form",
        "qr", "lu", "cholesky", "svd", "pivots",

        // ---- Polynomial helpers -----------------------------------------
        "resultant", "discriminant", "subresultants",
        "groebner_basis", "lift", "reduce",
        "interpolate", "lagrange_polynomial", "chebyshev_T", "chebyshev_U",
        "hermite", "legendre_P", "gen_legendre_P", "laguerre", "gen_laguerre",
        "jacobi_P", "ultraspherical", "gegenbauer", "krawtchouk",

        // ---- Combinatorics ----------------------------------------------
        "permutations", "combinations", "subsets", "tuples", "unordered_tuples",
        "cartesian_product", "product_pp",
        "stirling_number1", "stirling_number2", "catalan_number",
        "bell_polynomial", "bernoulli_polynomial", "euler_polynomial",
        "number_of_partitions", "number_of_compositions",
        "OEIS", "oeis",

        // ---- Graph helpers ----------------------------------------------
        "graphs", "digraphs", "posets", "matroids", "knots", "designs",
        "codes", "crystals", "polytopes", "lattices",

        // ---- Symbolic helpers / IO --------------------------------------
        "show", "view", "pretty_print", "latex", "html", "ascii_art", "unicode_art",
        "print_latex", "print", "load", "attach", "detach", "save", "loads", "dumps",
        "preparse", "preparser", "implicit_multiplication",
        "set_random_seed", "current_randstate", "randint", "random", "random_prime",
        "uniform", "gauss",

        // ---- Plotting ---------------------------------------------------
        "plot", "plot3d", "list_plot", "list_plot3d", "scatter_plot",
        "histogram", "bar_chart", "matrix_plot", "density_plot", "contour_plot",
        "implicit_plot", "implicit_plot3d", "parametric_plot", "parametric_plot3d",
        "polar_plot", "region_plot", "streamline_plot", "vector_field",
        "complex_plot", "polygon", "polygon2d", "polygon3d", "point",
        "point2d", "point3d", "points", "line", "line2d", "line3d",
        "lines", "arrow", "arrow2d", "arrow3d", "circle", "disk", "ellipse",
        "arc", "text", "text3d", "graphics_array", "multi_graphics",
        "sphere", "cylinder", "cone", "cube", "tetrahedron", "octahedron",
        "icosahedron", "dodecahedron", "Tachyon", "animate",

        // ---- Cryptography -----------------------------------------------
        "AES", "DES", "ECDSA", "RSA", "DiffieHellman",
        "hex_str_to_bin", "bin_to_ascii", "ascii_to_bin",

        // ---- Numerical --------------------------------------------------
        "numerical_approx", "n", "N", "nintegral", "numerical_integral",
        "minimize", "minimize_constrained", "linear_program", "MixedIntegerLinearProgram",
        "fft", "ifft",

        // ---- Vector calculus --------------------------------------------
        "gradient", "divergence", "curl", "laplacian", "hessian", "wronskian",
        "jacobian",

        // ---- Misc Python-like helpers -----------------------------------
        "range", "xrange", "srange", "sxrange", "ellipsis_range", "ellipsis_iter",
        "len", "type", "isinstance", "id", "hash", "repr", "ascii",
        "all", "any", "enumerate", "filter", "map", "zip", "reversed", "sorted",
        "sum_of_squares", "sumall", "prod_all",
        "open", "input", "compile",
        "list", "dict", "set", "tuple", "frozenset", "bytes", "bytearray",
        "str", "bool", "int", "float", "complex", "object",
        "iter", "next",
        "getattr", "setattr", "hasattr", "delattr", "vars", "dir",
        "globals", "locals", "callable",
        "staticmethod", "classmethod", "property", "super",

        // ---- Version / help --------------------------------------------
        "version", "banner", "license", "help", "search_doc", "search_def", "search_src",
        "browse_sage_doc", "tutorial", "reference", "quickref",
    )
}

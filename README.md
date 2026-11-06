# String Reconstruction

Academic project for the **Functional and Concurrent Programming** course at Universidad del Valle. It studies how to reconstruct a hidden string by querying an oracle that reports whether a candidate occurs as a contiguous substring.

## Problem

Let `S` be an unknown string of length `N` over the alphabet `{a, c, g, t}`. The oracle receives a candidate `w` and returns `true` if `w` occurs in `S`, and `false` otherwise. The goal is to reconstruct `S` while minimizing oracle queries, which may be costly.

The oracle interface is a Scala function:

```scala
type Oraculo = Seq[Char] => Boolean
```

`crearOraculo(delay)(string)` creates a test oracle that waits for `delay` milliseconds per query and checks whether the candidate occurs in the string.

## Algorithms

The project implements five sequential strategies and parallel counterparts:

| Approach | Main idea |
| --- | --- |
| Naive | Enumerate possible strings of length `N` and query the oracle. |
| Improved | Extend candidates character by character, retaining only fragments accepted by the oracle. |
| Turbo | Combine validated fragments into progressively larger blocks. |
| Improved Turbo | Filter candidates using intermediate fragments before querying the oracle. |
| Accelerated Turbo | Use a trie to store validated fragments and check substring membership. |

The parallel variants use Fork/Join tasks and parallel collections. Their `umbral` parameter selects a sequential fallback for inputs at or below the threshold, avoiding parallel overhead on smaller inputs.

## Benchmark results

The accompanying report compares the sequential and parallel approaches across different input sizes. It identifies **Improved Turbo** as the most consistent overall approach in its experiments. It also reports that the trie in Accelerated Turbo did not provide a clear improvement in that implementation. Benchmark measurements depend on hardware, JVM settings, and runtime load; CSV result files are included for reference.

## Requirements

- JDK compatible with Scala 3
- sbt **1.10.11** or compatible

The project uses Scala **3.3.6**, Scala Parallel Collections, ScalaMeter, and MUnit. The sbt configuration sets a 5 GB initial heap and an 8 GB maximum heap for Java processes.

## Build and run

Compile from the repository root:

```bash
sbt compile
```

Run the benchmark/test runner:

```bash
sbt "runMain TestRunner"
```

`TestRunner` performs repeated oracle queries and benchmarks; runtime can be substantial. It writes a timestamped CSV results file in the working directory. Use a Scala script runner compatible with the `.sc` extension to run `src/test/scala/pruebas.sc` examples.

## Project structure

```text
src/
├── main/scala/
│   ├── ArbolSufijos/       # Trie structure and operations
│   ├── Oraculo/            # Alphabet and substring oracle
│   ├── ReconstCadenas/     # Sequential reconstruction algorithms
│   ├── ReconstCadenasPar/  # Parallel reconstruction algorithms
│   ├── Benchmark/          # ScalaMeter helpers
│   ├── TestRunner/         # Benchmark runner and CSV output
│   └── common/             # Fork/Join task scheduler
└── test/scala/
    └── pruebas.sc         # Script-based examples and experiments
```

## Academic reference

The report **“El problema de reconstrucción de cadenas - Proyecto”** (Universidad del Valle, July 24, 2025) is included in the repository as [`El_problema_de_reconstrucción_de_cadenas.pdf`](./El_problema_de_reconstrucción_de_cadenas.pdf). It describes the problem, algorithmic approaches, experimental results, and conclusions about parallelization and trie performance.

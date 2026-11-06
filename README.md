# String Reconstruction

An academic project for the **Functional and Concurrent Programming** course at Universidad del Valle. The project studies how to recover a hidden string when the only available information comes from an oracle that answers substring-membership queries.

## The problem

Assume there is an unknown string `S` of known length `N`, built from the alphabet:

```text
Σ = {a, c, g, t}
```

For any candidate string `w`, an oracle answers whether `w` occurs contiguously somewhere inside `S`. It does not reveal the location of the match. The task is to identify the entire hidden string while minimizing the number of oracle calls. This models settings such as DNA sequencing, where each query can represent an expensive measurement.

The project models the oracle as a Scala function:

```scala
type Oraculo = Seq[Char] => Boolean
```

`crearOraculo(delay)(secret)` creates a local test oracle. It waits for `delay` milliseconds for every query and then checks whether the candidate occurs in `secret`.

The alphabet has four symbols, so there are `4^N` possible strings of length `N`. Trying them all is guaranteed to find the answer, but quickly becomes impractical. The central algorithmic idea is that every contiguous fragment of a string that occurs in `S` also occurs in `S`. A valid fragment can therefore be extended or combined with other valid fragments to build longer candidates.

## Approaches explored

The project implements five sequential strategies and parallel counterparts:

1. **Naive exhaustive search** generates candidates of length `N` and asks the oracle whether each one is the hidden string. It provides a simple baseline, but its search space grows exponentially.
2. **Improved incremental search** builds candidates one character at a time. At each stage, it keeps only prefixes accepted by the oracle, pruning branches that cannot lead to a solution.
3. **Turbo** starts with valid one-character fragments and repeatedly concatenates pairs of valid fragments, doubling the candidate block length at each round.
4. **Improved Turbo** adds an internal filter: before asking the oracle about a longer concatenation, it checks that its intermediate fragments are already known to be valid. This avoids some redundant oracle calls.
5. **Accelerated Turbo** uses a trie to store validated fragments and check fragment membership during filtering. The report investigates whether this data structure improves on Improved Turbo in practice.

The parallel variants distribute independent candidate checks or combinations across Fork/Join tasks and parallel collections. Their `umbral` parameter selects a sequential implementation for inputs at or below a threshold. This matters because creating and scheduling parallel work has a fixed cost that can outweigh its benefits for small inputs.

## What the project achieved

The work implements the reconstruction strategies, a test oracle with configurable query delay, a trie-based fragment index, parallel variants, and benchmark tooling that compares sequential and parallel execution. The report documents the algorithms' reasoning and measurements at multiple input sizes; CSV result files from benchmark runs are also included in the repository.

The experiments highlight two practical lessons:

- Reducing oracle calls through progressively stronger filtering can matter more than adding a data structure by itself.
- Parallel execution helps once there is enough independent work to offset scheduling overhead. The useful threshold depends on the algorithm and machine.

## Reported results

The report identifies **Improved Turbo** as the strongest overall choice in its experiments: it combines low absolute runtime with comparatively stable parallel speedup. The report's summary gives maximum speedups of 10.14× for Naive, 8.31× for Improved, 4.20× for Turbo, 5.56× for Accelerated Turbo, and 5.94× for Improved Turbo. These are each algorithm's reported peak, not a same-input comparison; in particular, Naive was tested on a shorter input range than the Turbo approaches.

For the report's `k = 10` comparison, the reported runtimes are:

| Algorithm | Sequential (ms) | Parallel (ms) | Speedup (parallel comparison) |
| --- | ---: | ---: | ---: |
| Naive | 1,283,371 | 182,598 | 7.03× |
| Improved | 2,566,823 | 308,742 | 8.31× |
| Turbo | 520,498 | 321,903 | 1.62× |
| Accelerated Turbo | 334,020 | 268,239 | 1.25× |
| Improved Turbo | 128,028 | 26,944 | 4.75× |

The last column is calculated from the sequential and parallel times in the report's `k = 10` table. At this input size, Improved Turbo has the shortest sequential and parallel runtimes. The trie-based Accelerated Turbo is slower than Improved Turbo in both columns, supporting the report's conclusion that the trie did not provide a clear benefit in this implementation.

The report also describes scaling problems and irregular results for some methods and input sizes. Its measurements are specific to the tested machine, JVM and memory settings, thread count, garbage-collection behavior, and background system load. The reported peak speedups should therefore be read as experimental observations, not guaranteed performance on other systems.

## Build and run

Requirements:

- JDK compatible with Scala 3
- sbt 1.10.11 or a compatible version

The project uses Scala 3.3.6, Scala Parallel Collections, ScalaMeter, and MUnit. Compile from the repository root:

```bash
sbt compile
```

Run the benchmark runner with:

```bash
sbt "runMain TestRunner"
```

`TestRunner` performs repeated oracle queries and benchmarks, so it can take a long time. It writes a timestamped CSV file in the working directory. The Scala script examples are in `src/test/scala/pruebas.sc` and require a Scala script runner compatible with `.sc` files.

## Project structure

```text
src/
├── main/scala/
│   ├── ArbolSufijos/       # Trie structure and operations
│   ├── Oraculo/            # Alphabet and substring oracle
│   ├── ReconstCadenas/     # Sequential reconstruction algorithms
│   ├── ReconstCadenasPar/  # Parallel reconstruction algorithms
│   ├── Benchmark/          # ScalaMeter benchmark helpers
│   ├── TestRunner/         # Benchmark runner and CSV output
│   └── common/             # Fork/Join task scheduler
└── test/scala/
    └── pruebas.sc          # Script-based examples and experiments
```

## Academic report

The report **“El problema de reconstrucción de cadenas - Proyecto”** (Universidad del Valle, July 24, 2025) is included as [`El_problema_de_reconstrucción_de_cadenas.pdf`](./El_problema_de_reconstrucción_de_cadenas.pdf). It contains the formal discussion, algorithm analyses, benchmark tables, and conclusions summarized above.

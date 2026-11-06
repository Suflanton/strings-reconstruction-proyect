package object Benchmark {

  import org.scalameter._

  import Oraculo._
  import ReconstCadenas._

  val costoOraculo = 1

  val configBase = config(
    //KeyValue(Key.exec.minWarmupRuns -> 1),
    //KeyValue(Key.exec.maxWarmupRuns -> 1),
    KeyValue(Key.verbose -> false)
  ) withWarmer new Warmer.Default

  // Benchmark para un solo algoritmo
  def benchmarkSimple(alg: (Int, Oraculo) => Seq[Char], secuencias: Seq[Seq[Char]]): Seq[Double] = {
    for (s <- secuencias) yield {
      val oraculo = crearOraculo(costoOraculo)(s)
      val tiempo = configBase measure {
        alg(s.length, oraculo)
      }
      tiempo.value
    }
  }

  // Benchmark para comparar dos algoritmos
  def benchmarkComparacionSecuencialParalelo(
                            alg1: (Int, Oraculo) => Seq[Char],
                            alg2: Int => (Int, Oraculo) => Seq[Char],
                            secuencias: Seq[Seq[Char]]
                          ): Seq[(Double, Double, Double)] = {
    for (s <- secuencias) yield {
      val oraculo = crearOraculo(costoOraculo)(s)

      val tiempo1 = configBase measure {
        alg1(s.length, oraculo)
      }

      val tiempo2 = configBase measure {
        alg2(0)(s.length, oraculo)
      }

      val speedUp = tiempo1.value / tiempo2.value
      (tiempo1.value, tiempo2.value, speedUp)
    }
  }

}

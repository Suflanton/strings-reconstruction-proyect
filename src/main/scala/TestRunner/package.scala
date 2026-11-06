import ReconstCadenas.*
import ReconstCadenasPar.*
import Oraculo.{alfabeto, *}
import org.scalameter.*

import java.io.{File, PrintWriter}
import java.text.SimpleDateFormat
import java.util.Date

object TestRunner extends App {

  def comparar(k: Int): (Double, Double, Double) = {
    val n = k
    val s = generarSecuenciaAleatoria(n, alfabeto)
    val or = crearOraculo(1)(s)
    println(s"\n--- Prueba para k=$k (n=$n) ---")
    println(s"Cadena aleatoria generada: ${s.mkString}")

    // Tiempo secuencial
    val tiempoSecuencial = config(
      KeyValue(Key.exec.minWarmupRuns -> 1),//20
      KeyValue(Key.exec.maxWarmupRuns -> 1),//60
      KeyValue(Key.verbose -> false)
    ) withWarmer(Warmer.Zero) measure {
      val resultado = reconstruirCadenaMejorado(s.length, or)
      // Añadido: Mostrar resultado secuencial
      println(s"Resultado secuencial: ${resultado.mkString}")
      resultado
    }

    // Tiempo paralelo (única versión paralela incluida)
    val tiempoPar = config(
      KeyValue(Key.exec.minWarmupRuns -> 1),
      KeyValue(Key.exec.maxWarmupRuns -> 1),
      KeyValue(Key.verbose -> false)
    ) withWarmer(Warmer.Zero) measure {
      val resultado = reconstruirCadenaMejoradoPar(1)(s.length, or)
      // Añadido: Mostrar resultado paralelo
      println(s"Resultado paralelo:   ${resultado.mkString}")
      resultado
    }

    val speedup = tiempoSecuencial.value / tiempoPar.value

    (tiempoSecuencial.value, tiempoPar.value, speedup)
  }

  def generarSecuenciaAleatoria(n: Int, alfabeto: Seq[Char]): Seq[Char] = {
    val r = new scala.util.Random()
    Seq.fill(n)(alfabeto(r.nextInt(alfabeto.length)))
  }

  def imprimirTablaResultados(resultados: Map[Int, (Double, Double, Double)]): Unit = {
    println("| k | Tam | T.Secuencial (ms) | T.Paralelo (ms) | Aceleración |")
    println("|---|-----|-------------------|-----------------|--------------|")
    for ((k, (ts, tp, sp)) <- resultados.toSeq.sortBy(_._1)) {
      val tamano = math.pow(2,k).toInt
      println(f"| $k%2d | $tamano%4d | $ts%17.2f | $tp%16.2f | $sp%12.2f |")
    }
  }

  def guardarResultadosCSV(resultados: Map[Int, (Double, Double, Double)], nombreArchivo: String): Unit = {
    val writer = new PrintWriter(new File(nombreArchivo))
    try {
      writer.println("k,Tamaño,Tiempo_Secuencial,Tiempo_Paralelo,Aceleracion")
      for ((k, (ts, tp, sp)) <- resultados.toSeq.sortBy(_._1)) {
        val tamano =k
        writer.println(f"$k,$tamano,$ts%.2f,$tp%.2f,$sp%.2f")
      }
      println(s"Resultados guardados en el archivo: $nombreArchivo")
    } finally {
      writer.close()
    }
  }

  val dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss")
  val timestamp = dateFormat.format(new Date())
  val nombreArchivo = s"resultados_Turbomejorada_final_12-12_$timestamp.csv"

  println("Ejecutando pruebas para k de 1 a 10...")

  val resultados = (1 to 12).map { k =>
    println(s"Procesando k = $k (tamaño = ${math.pow(2,k).toInt})...")
    val resultado = comparar(k)
    (k, resultado)
  }.toMap

  println("\nRESULTADOS DE PRUEBAS:")
  imprimirTablaResultados(resultados)
  guardarResultadosCSV(resultados, nombreArchivo)
}
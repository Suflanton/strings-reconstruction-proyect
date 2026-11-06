import ArbolSufijos._
import Oraculo._

package object ReconstCadenas {

  def reconstruirCadenaIngenuo(n: Int, o: Oraculo): Seq[Char] = {
    def generarCombinaciones(n: Int): LazyList[Seq[Char]] = {
      if (n == 0) LazyList(Seq.empty)
      else for {
        suf <- generarCombinaciones(n - 1)
        c <- LazyList.from(alfabeto)
      } yield c +: suf
    }

    val combinaciones = generarCombinaciones(n)

    val total = math.pow(alfabeto.length, n).toInt
    val octavo = total / 8

    val ((res1, res2, res3, res4), (res5, res6, res7, res8)) = (
      (
        combinaciones.slice(0 * octavo, 1 * octavo).find(o),
        combinaciones.slice(1 * octavo, 2 * octavo).find(o),
        combinaciones.slice(2 * octavo, 3 * octavo).find(o),
        combinaciones.slice(3 * octavo, 4 * octavo).find(o)
      ),
      (
        combinaciones.slice(4 * octavo, 5 * octavo).find(o),
        combinaciones.slice(5 * octavo, 6 * octavo).find(o),
        combinaciones.slice(6 * octavo, 7 * octavo).find(o),
        combinaciones.slice(7 * octavo, total).find(o)
      )
    )

    res1.orElse(res2).orElse(res3).orElse(res4)
      .orElse(res5).orElse(res6).orElse(res7).orElse(res8)
      .getOrElse(Seq.empty)
  }

  def reconstruirCadenaMejorado(n: Int, o: Oraculo): Seq[Char] = {
    def generarCombinaciones(n: Int): LazyList[Seq[Char]] = {
      if (n == 0) LazyList(Seq.empty)
      else for {
        suf <- generarCombinaciones(n - 1)
        c <- alfabeto
        s = c +: suf
        if(o(s))
      } yield s
    }

    generarCombinaciones(n).headOption.getOrElse(Seq.empty)

   
  }
  

  def reconstruirCadenaTurbo(n: Int, o: Oraculo): Seq[Char] = {
    // recibe la longitud de la secuencia que hay que reconstruir (n, potencia de 2), y un oraculo para esa secuencia
    // y devuelve la secuencia reconstruida
    // Usa la propiedad de que si s=s1++s2 entonces s1 y s2 tambien son subsecuencias de s
    def generarSigmaK(sc: Seq[Seq[Char]]): Seq[Seq[Char]] = {
      for{
        w <- sc
        v <- sc
      }yield w ++ v
    }
    def aux(scInicial: Seq[Seq[Char]], k: Int): Seq[Char] = {
      val sigmaK = generarSigmaK(scInicial).filter(o)
      if(k == n) sigmaK.head else aux(sigmaK, k * 2)
    }
    val initialSc = alfabeto.map(c=> Seq(c)).filter(o)
    aux(initialSc, 2)
  }

  def reconstruirCadenaTurboMejorada(n: Int, o: Oraculo): Seq[Char] = {
    // recibe la longitud de la secuencia que hay que reconstruir (n, potencia de 2), y un oraculo para esa secuencia
    // y devuelve la secuencia reconstruida
    // Usa la propiedad de que si s=s1++s2 entonces s1 y s2 tambien son subsecuencias de s
    // Usa el filtro para ir mas rapido
    def filtrar(sc: Seq[Seq[Char]], k: Int): Seq[Seq[Char]] = {
      for{
        w <- sc
        v <- sc
        s = w ++ v
        if (1 until k).forall(i => sc.contains(s.slice(i, i + k)))
      } yield s
    }

    def aux(scInicial: Seq[Seq[Char]], k: Int): Seq[Char] = {
      val sigmaK = filtrar(scInicial, k/2).filter(o)
      if(k == n)  sigmaK.head else aux(sigmaK, k * 2)
    }

    val initialSc = alfabeto.map(c=> Seq(c)).filter(o)
    aux(initialSc, 2)
  }

  def reconstruirCadenaTurboAcelerada(n: Int, o: Oraculo): Seq[Char] = {
    // recibe la longitud de la secuencia que hay que reconstruir (n, potencia de 2), y un oraculo para esa secuencia
    // y devuelve la secuencia reconstruida
    // Usa la propiedad de que si s=s1++s2 entonces s1 y s2 tambien son subsecuencias de s
    // Usa el filtro para ir mas rapido
    // Usa arboles de sufijos para guardar Seq[Seq[Char]]
    def filtrar(sc1: Seq[Seq[Char]], k:Int): Seq[Seq[Char]] = {
      val scTrie = arbolDeSufijos(sc1);
      for {
        w <- sc1
        v <- sc1
        s = w ++ v
        if (1 until k).forall(c => pertenece(s.slice(c, c + k), scTrie))
      } yield s
    }

    def aux(scInicial: Seq[Seq[Char]], k:Int): Seq[Char] = {
      val sigmaK = filtrar(scInicial, k/2).filter(o)
      if (k == n) sigmaK.head else aux(sigmaK, k * 2)
    }

    val initialSc = alfabeto.map(c=> Seq(c)).filter(o)
    aux(initialSc, 2)
  }


}

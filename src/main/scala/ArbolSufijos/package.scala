package object ArbolSufijos {
  // Definiendo otra estructura para manipular Seq[Seq[Char]]
  abstract class Trie
  case class Nodo(car: Char, marcada: Boolean, hijos: List[Trie]) extends Trie
  case class Hoja(car: Char, marcada: Boolean) extends Trie

  def raiz(t: Trie): Char = {
    t match {
      case Nodo(c, _, _) => c
      case Hoja(c, _)    => c
    }
  }

  def cabezas(t: Trie): Seq[Char] = {
    t match {
      case Nodo(_, _, lt) => lt.map(t => raiz(t))
      case Hoja(c, _)      => Seq[Char](c)
    }
  }

  def pertenece(s: Seq[Char], t: Trie): Boolean = {
    s match {
    
      case s1 +: ss => t match {
        case Nodo(_, _, hijos) =>
          hijos.exists(node => raiz(node) == s1 && pertenece(ss, node))
        case Hoja(_, _) => false
      }

      case Seq() => t match {
        case Nodo(_, marcada, _) => marcada
        case Hoja(_, marcada)    => marcada
      }
    }
  }


  def adicionar(s: Seq[Char], t: Trie): Trie = {

    def construirRama(s: Seq[Char]): Trie={
      s match{
        case s1+:Seq() => Hoja(s1,true)
        case s1+:ss => Nodo(s1, false, List(construirRama(ss)))
      }
    }

    if(pertenece(s,t)){t}else{

      (s,t) match{

        case(s1+:ss, Hoja(c,m)) => Nodo(c, m, List(construirRama(s1+:ss)))

        case(s1+:ss, Nodo(c,m,hijos)) => if(cabezas(t).contains(s1)){
          val nuevosHijos = hijos.map(hijo => if(raiz(hijo) == s1) adicionar(ss,hijo) else hijo)
          Nodo(c,m,nuevosHijos)
        }else{
          Nodo(c,m, hijos ++ List(construirRama(s1+:ss)))
        }

        case (Seq(), Nodo(c, m, hijos)) => Nodo(c, true, hijos)

        case(Seq(), t) => t

      }
    }
  }

  def arbolDeSufijos(ss: Seq[Seq[Char]]): Trie = {
    // dada una secuencia no vacia de secuencias devuelve el arbol de sufijos asociado a esas secuencia
    def sufijosDeSec(ss:Seq[Seq[Char]]): Seq[Seq[Char]]={
      ss.foldLeft(Seq.empty[Seq[Char]])((suf,cad)=>
        suf ++ (for (n <- 0 until cad.length) yield cad.drop(n)))
    }
    val trie: Trie = Nodo(car = ' ', marcada=false, List())
    val sufijos = sufijosDeSec(ss)

    sufijos.foldLeft(trie)((t,s) => adicionar(s,t))
  }
}

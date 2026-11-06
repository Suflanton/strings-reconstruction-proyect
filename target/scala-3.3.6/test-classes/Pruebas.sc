import Oraculo._
import ArbolSufijos._
import ReconstCadenas._
import ReconstCadenasPar._
import scala.util.Random
import Benchmark._
val random = new Random()


def secAlAzar(long:Int, s:Seq[Char]): Seq[Char] = {
  //Crea una secuencia de long caracteres del alfabeto,
  // escogidos de forma aleatoria, terminando en s
  if (s.length==long) s
  else {
    val indiceAzar=random.nextInt(4)
    secAlAzar(long,alfabeto(indiceAzar)+:s)
  }
}
val costoOraculo = 0

val sec1=Seq('a', 'c', 'c', 'a')
val sec2 = Seq('a', 'c', 'g', 'c', 'a')
val sec3="attctcataaaatcac".toList

val or_1=crearOraculo(costoOraculo)(sec1)
val or_2=crearOraculo(costoOraculo)(sec2)
val or_3=crearOraculo(costoOraculo)(sec3)

def secsCortasParaPruebas(n:Int):Seq[Seq[Char]] = for {
  i <- 1 to n
  s = secAlAzar(i,Seq())
} yield s

def secsLargasParaPruebas(n:Int):Seq[Seq[Char]] = for {
  i <- 1 to n
  s = secAlAzar(math.pow(2,i).toInt,Seq())
} yield s
val prueba= crearOraculo(0)(secAlAzar(4,Seq()))
val pruebalarga = crearOraculo(0)(secAlAzar(math.pow(2,5).toInt,Seq()))
reconstruirCadenaIngenuoPar(3)(4, prueba)
reconstruirCadenaMejoradoPar(16)(math.pow(2,5).toInt, pruebalarga)
reconstruirCadenaTurboPar(16)(math.pow(2,5).toInt, pruebalarga)
reconstruirCadenaTurboMejoradaPar(16)(math.pow(2,5).toInt, pruebalarga)
reconstruirCadenaTurboAceleradaPar(16)(math.pow(2,5).toInt, pruebalarga)

/*
def secsCortasParaPruebas(n:Int):Seq[Seq[Char]] = for {
  i <- 1 to n
  s = secAlAzar(i,Seq())
} yield s

def secsLargasParaPruebas(n:Int):Seq[Seq[Char]] = for {
  i <- 1 to n
  s = secAlAzar(math.pow(2,i).toInt,Seq())
} yield s

def pruebasIngenuo(ss:Seq[Seq[Char]]) = for {
  s <- ss
  o = crearOraculo(costoOraculo)(s)
} yield (s,reconstruirCadenaIngenuoConIterator(s.length,o))

def pruebasIngenuoPar(ss:Seq[Seq[Char]]) = for {
  s <- ss
  o = crearOraculo(costoOraculo)(s)
} yield (s,reconstruirCadenaIngenuoParConIterator(0)(s.length,o))

*/
//pruebasIngenuoPar(secsCortasParaPruebas(2))
// benchmarkComparacionSecuencialParalelo(reconstruirCadenaIngenuoConIterator,reconstruirCadenaIngenuoParConIterator,Seq(secAlAzar(12,Seq())))
/*

def pruebasMejorado (ss:Seq[Seq[Char]]) = for {
  s <- ss
  o = crearOraculo(costoOraculo)(s)
} yield (s.length, s,reconstruirCadenaMejorado(s.length,o))



def pruebasTurbo (ss:Seq[Seq[Char]]) = for {
  s <- ss
  o = crearOraculo(costoOraculo)(s)
} yield (s.length, s,reconstruirCadenaTurbo(s.length,o))

def pruebasTurboMejorada (ss:Seq[Seq[Char]]) = for {
  s <- ss
  o = crearOraculo(costoOraculo)(s)
} yield (s.length, s,reconstruirCadenaTurboMejorada(s.length,o))



def pruebasTurboAcelerada(ss:Seq[Seq[Char]]) = for {
  s <- ss
  o = crearOraculo(costoOraculo)(s)
} yield (s.length, s,reconstruirCadenaTurboAcelerada(s.length,o))


// Secuencias para pruebas
val ss1_10=secsCortasParaPruebas(10)
val ss1_16=secsCortasParaPruebas(16)
val ss2_1024 = secsLargasParaPruebas(10)
val ss2_2048 = secsLargasParaPruebas(11)
val ss2_4096 = secsLargasParaPruebas(12)
val s1_8 = ss1_10(7)
val s2_8 = ss1_16(7)
val s1_10 = ss1_10.reverse(0)
val s1_11=ss1_16(10)
val s1_12 = ss1_16(11)
val s1_13 = ss1_16(12)
val s1_14 = ss1_16(13)
val s1_15 = ss1_16(14)
val s1_16 = ss1_16(15)
val s1_32 = ss2_1024(4)
val s2_32 = ss2_2048(4)
val s3_32 = ss2_4096(4)
val s1_64 = ss2_1024(5)
val s2_64 = ss2_2048(5)
val s3_64 = ss2_4096(5)
val s1_128 = ss2_1024(6)
val s2_128 = ss2_2048(6)
val s3_128 = ss2_4096(6)
val s1_256 = ss2_1024(7)
val s2_256 = ss2_2048(7)
val s3_256 = ss2_4096(7)
val s1_512 = ss2_1024(8)
val s2_512 = ss2_2048(8)
val s3_512 = ss2_4096(8)
val s1_1024 = ss2_1024(9)
val s2_1024 = ss2_2048(9)
val s3_1024 = ss2_4096(9)
val s1_2048 = ss2_2048(10)
val s2_2048 = ss2_4096(10)
val s1_4096 = ss2_4096(11)


// Pruebas funcionales

// secuencias de longitud 8
reconstruirCadenaIngenuo(s1_8.length, crearOraculo(costoOraculo)(s1_8))
reconstruirCadenaIngenuo(s2_8.length, crearOraculo(costoOraculo)(s2_8))
reconstruirCadenaMejorado(s1_8.length, crearOraculo(costoOraculo)(s1_8))
reconstruirCadenaMejorado(s2_8.length, crearOraculo(costoOraculo)(s2_8))
reconstruirCadenaTurbo(s1_8.length, crearOraculo(costoOraculo)(s1_8))
reconstruirCadenaTurbo(s2_8.length, crearOraculo(costoOraculo)(s2_8))
reconstruirCadenaTurboMejorada(s1_8.length, crearOraculo(costoOraculo)(s1_8))
reconstruirCadenaTurboMejorada(s2_8.length, crearOraculo(costoOraculo)(s2_8))
reconstruirCadenaTurboAcelerada(s1_8.length, crearOraculo(costoOraculo)(s1_8))
reconstruirCadenaTurboAcelerada(s2_8.length, crearOraculo(costoOraculo)(s2_8))
// secuencias de longitud 16
reconstruirCadenaMejorado(s1_16.length, crearOraculo(costoOraculo)(s1_16))
reconstruirCadenaTurbo(s1_16.length, crearOraculo(costoOraculo)(s1_16))
reconstruirCadenaTurboMejorada(s1_16.length, crearOraculo(costoOraculo)(s1_16))
reconstruirCadenaTurboAcelerada(s1_16.length, crearOraculo(costoOraculo)(s1_16))

// secuencias de longitud 32
reconstruirCadenaMejorado(s1_32.length, crearOraculo(costoOraculo)(s1_32))
reconstruirCadenaMejorado(s2_32.length, crearOraculo(costoOraculo)(s2_32))
reconstruirCadenaMejorado(s3_32.length, crearOraculo(costoOraculo)(s3_32))
reconstruirCadenaTurbo(s1_32.length, crearOraculo(costoOraculo)(s1_32))
reconstruirCadenaTurbo(s2_32.length, crearOraculo(costoOraculo)(s2_32))
reconstruirCadenaTurbo(s3_32.length, crearOraculo(costoOraculo)(s3_32))
reconstruirCadenaTurboMejorada(s1_32.length, crearOraculo(costoOraculo)(s1_32))
reconstruirCadenaTurboMejorada(s2_32.length, crearOraculo(costoOraculo)(s2_32))
reconstruirCadenaTurboMejorada(s3_32.length, crearOraculo(costoOraculo)(s3_32))
reconstruirCadenaTurboAcelerada(s1_32.length, crearOraculo(costoOraculo)(s1_32))
reconstruirCadenaTurboAcelerada(s2_32.length, crearOraculo(costoOraculo)(s2_32))
reconstruirCadenaTurboAcelerada(s3_32.length, crearOraculo(costoOraculo)(s3_32))


// secuencias de longitud 64
reconstruirCadenaMejorado(s1_64.length, crearOraculo(costoOraculo)(s1_64))
reconstruirCadenaMejorado(s2_64.length, crearOraculo(costoOraculo)(s2_64))
reconstruirCadenaMejorado(s3_64.length, crearOraculo(costoOraculo)(s3_64))
reconstruirCadenaTurbo(s1_64.length, crearOraculo(costoOraculo)(s1_64))
reconstruirCadenaTurbo(s2_64.length, crearOraculo(costoOraculo)(s2_64))
reconstruirCadenaTurbo(s3_64.length, crearOraculo(costoOraculo)(s3_64))
reconstruirCadenaTurboMejorada(s1_64.length, crearOraculo(costoOraculo)(s1_64))
reconstruirCadenaTurboMejorada(s2_64.length, crearOraculo(costoOraculo)(s2_64))
reconstruirCadenaTurboMejorada(s3_64.length, crearOraculo(costoOraculo)(s3_64))
reconstruirCadenaTurboAcelerada(s1_64.length, crearOraculo(costoOraculo)(s1_64))
reconstruirCadenaTurboAcelerada(s2_64.length, crearOraculo(costoOraculo)(s2_64))
reconstruirCadenaTurboAcelerada(s3_64.length, crearOraculo(costoOraculo)(s3_64))

// secuencias de longitud 128
reconstruirCadenaMejorado(s1_128.length, crearOraculo(costoOraculo)(s1_128))
reconstruirCadenaMejorado(s2_128.length, crearOraculo(costoOraculo)(s2_128))
reconstruirCadenaMejorado(s3_128.length, crearOraculo(costoOraculo)(s3_128))
reconstruirCadenaTurbo(s1_128.length, crearOraculo(costoOraculo)(s1_128))
reconstruirCadenaTurbo(s2_128.length, crearOraculo(costoOraculo)(s2_128))
reconstruirCadenaTurbo(s3_128.length, crearOraculo(costoOraculo)(s3_128))
reconstruirCadenaTurboMejorada(s1_128.length, crearOraculo(costoOraculo)(s1_128))
reconstruirCadenaTurboMejorada(s2_128.length, crearOraculo(costoOraculo)(s2_128))
reconstruirCadenaTurboMejorada(s3_128.length, crearOraculo(costoOraculo)(s3_128))
reconstruirCadenaTurboAcelerada(s1_128.length, crearOraculo(costoOraculo)(s1_128))
reconstruirCadenaTurboAcelerada(s2_128.length, crearOraculo(costoOraculo)(s2_128))
reconstruirCadenaTurboAcelerada(s3_128.length, crearOraculo(costoOraculo)(s3_128))

// secuencias de longitud 256
reconstruirCadenaMejorado(s1_256.length, crearOraculo(costoOraculo)(s1_256))
reconstruirCadenaMejorado(s2_256.length, crearOraculo(costoOraculo)(s2_256))
reconstruirCadenaMejorado(s3_256.length, crearOraculo(costoOraculo)(s3_256))
reconstruirCadenaTurbo(s1_256.length, crearOraculo(costoOraculo)(s1_256))
reconstruirCadenaTurbo(s2_256.length, crearOraculo(costoOraculo)(s2_256))
reconstruirCadenaTurbo(s3_256.length, crearOraculo(costoOraculo)(s3_256))
reconstruirCadenaTurboMejorada(s1_256.length, crearOraculo(costoOraculo)(s1_256))
reconstruirCadenaTurboMejorada(s2_256.length, crearOraculo(costoOraculo)(s2_256))
reconstruirCadenaTurboMejorada(s3_256.length, crearOraculo(costoOraculo)(s3_256))
reconstruirCadenaTurboAcelerada(s1_256.length, crearOraculo(costoOraculo)(s1_256))
reconstruirCadenaTurboAcelerada(s2_256.length, crearOraculo(costoOraculo)(s2_256))
reconstruirCadenaTurboAcelerada(s3_256.length, crearOraculo(costoOraculo)(s3_256))

// secuencias de longitud 512
reconstruirCadenaMejorado(s1_512.length, crearOraculo(costoOraculo)(s1_512))
reconstruirCadenaMejorado(s2_512.length, crearOraculo(costoOraculo)(s2_512))
reconstruirCadenaMejorado(s3_512.length, crearOraculo(costoOraculo)(s3_512))
reconstruirCadenaTurbo(s1_512.length, crearOraculo(costoOraculo)(s1_512))
reconstruirCadenaTurbo(s2_512.length, crearOraculo(costoOraculo)(s2_512))
reconstruirCadenaTurbo(s3_512.length, crearOraculo(costoOraculo)(s3_512))
reconstruirCadenaTurboMejorada(s1_512.length, crearOraculo(costoOraculo)(s1_512))
reconstruirCadenaTurboMejorada(s2_512.length, crearOraculo(costoOraculo)(s2_512))
reconstruirCadenaTurboMejorada(s3_512.length, crearOraculo(costoOraculo)(s3_512))
reconstruirCadenaTurboAcelerada(s1_512.length, crearOraculo(costoOraculo)(s1_512))
reconstruirCadenaTurboAcelerada(s2_512.length, crearOraculo(costoOraculo)(s2_512))
reconstruirCadenaTurboAcelerada(s3_512.length, crearOraculo(costoOraculo)(s3_512))


// secuencias de longitud 1024
reconstruirCadenaMejorado(s1_1024.length, crearOraculo(costoOraculo)(s1_1024))
reconstruirCadenaMejorado(s2_1024.length, crearOraculo(costoOraculo)(s2_1024))
reconstruirCadenaMejorado(s3_1024.length, crearOraculo(costoOraculo)(s3_1024))
reconstruirCadenaTurbo(s1_1024.length, crearOraculo(costoOraculo)(s1_1024))
reconstruirCadenaTurbo(s2_1024.length, crearOraculo(costoOraculo)(s2_1024))
reconstruirCadenaTurbo(s3_1024.length, crearOraculo(costoOraculo)(s3_1024))
reconstruirCadenaTurboMejorada(s1_1024.length, crearOraculo(costoOraculo)(s1_1024))
reconstruirCadenaTurboMejorada(s2_1024.length, crearOraculo(costoOraculo)(s2_1024))
reconstruirCadenaTurboMejorada(s3_1024.length, crearOraculo(costoOraculo)(s3_1024))
reconstruirCadenaTurboAcelerada(s1_1024.length, crearOraculo(costoOraculo)(s1_1024))
reconstruirCadenaTurboAcelerada(s2_1024.length, crearOraculo(costoOraculo)(s2_1024))
reconstruirCadenaTurboAcelerada(s3_1024.length, crearOraculo(costoOraculo)(s3_1024))

// Pruebas por lotes

pruebasIngenuo(ss1_10)
pruebasIngenuo(ss1_16.slice(0,10))
pruebasIngenuo(ss1_16.slice(0,11))
pruebasIngenuo(ss1_16.slice(0,12))
pruebasIngenuo(ss1_16.slice(0,13))
pruebasIngenuo(ss1_16.slice(0,14))
pruebasIngenuo(ss1_16.slice(0,15))
pruebasIngenuo(ss1_16)

// con n=13 casi no puede, con n= 14, no pudo


pruebasMejorado(ss1_10)
pruebasMejorado(ss1_16.slice(0,10))
pruebasMejorado(ss1_16.slice(0,11))
pruebasMejorado(ss1_16.slice(0,12))
pruebasMejorado(ss1_16.slice(0,13))
pruebasMejorado(ss1_16.slice(0,14))
pruebasMejorado(ss1_16.slice(0,15))
pruebasMejorado(ss1_16)
pruebasMejorado(ss2_1024)
pruebasMejorado(ss2_2048)
pruebasMejorado(ss2_4096)
// con n=2^11  pudo, con n= 2^12, no pudo

pruebasTurbo(ss1_10)
pruebasTurbo(ss1_16.slice(0,10))
pruebasTurbo(ss1_16.slice(0,11))
pruebasTurbo(ss1_16.slice(0,12))
pruebasTurbo(ss1_16.slice(0,13))
pruebasTurbo(ss1_16.slice(0,14))
pruebasTurbo(ss1_16.slice(0,15))
pruebasTurbo(ss1_16)
pruebasTurbo(ss2_1024)
pruebasTurbo(ss2_2048)
pruebasTurbo(ss2_4096)

pruebasTurboMejorada(ss1_10)
pruebasTurboMejorada(ss1_16.slice(0,10))
pruebasTurboMejorada(ss1_16.slice(0,11))
pruebasTurboMejorada(ss1_16.slice(0,12))
pruebasTurboMejorada(ss1_16.slice(0,13))
pruebasTurboMejorada(ss1_16.slice(0,14))
pruebasTurboMejorada(ss1_16.slice(0,15))
pruebasTurboMejorada(ss1_16)
pruebasTurboMejorada(ss2_1024)
pruebasTurboMejorada(ss2_2048)
pruebasTurboMejorada(ss2_4096)

pruebasTurboAcelerada(ss1_10)
pruebasTurboAcelerada(ss1_16.slice(0,10))
pruebasTurboAcelerada(ss1_16.slice(0,11))
pruebasTurboAcelerada(ss1_16.slice(0,12))
pruebasTurboAcelerada(ss1_16.slice(0,13))
pruebasTurboAcelerada(ss1_16.slice(0,14))
pruebasTurboAcelerada(ss1_16.slice(0,15))
pruebasTurboAcelerada(ss1_16)
pruebasTurboAcelerada(ss2_1024)
pruebasTurboAcelerada(ss2_2048)
pruebasTurboAcelerada(ss2_4096)
*/



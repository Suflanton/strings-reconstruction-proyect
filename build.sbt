ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.6"

lazy val root = (project in file("."))
  .settings(
    name := "strings-reconstruction-proyect",

    javaOptions ++= Seq(
      "-Xms5G",                        // Memoria mínima del heap
      "-Xmx8G",                        // Memoria máxima del heap
      "-XX:+UseG1GC",                 // Usar el recolector de basura G1
      "-XX:-UseGCOverheadLimit",      // Desactivar el límite de overhead de GC
      "-Djava.util.concurrent.ForkJoinPool.common.parallelism=6",
        "-XX:+PrintGCDetails",
        "-XX:+PrintGCTimeStamps"
    )
  )

scalacOptions ++= Seq("-language:implicitConversions", "-deprecation")
libraryDependencies ++= Seq(
  ("com.storm-enroute" %% "scalameter-core" % "0.21").cross(CrossVersion.for3Use2_13),
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.2.0",
  "org.scalameta" %% "munit" % "1.1.0" % Test
)
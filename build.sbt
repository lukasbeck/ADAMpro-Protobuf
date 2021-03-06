//protobuf
import sbtassembly.AssemblyPlugin.autoImport._

name := "ADAMpro-grpc"

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

libraryDependencies ++= Seq(
  "io.grpc" % "grpc-protobuf" % "1.0.3",
  "io.grpc" % "grpc-stub" % "1.0.3",
  "io.grpc" % "grpc-netty" % "1.0.3",
  "io.netty" % "netty-all" % "4.1.6.Final",
  "com.google.protobuf" % "protobuf-java" % "3.1.0",
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % com.trueaccord.scalapb.compiler.Version.scalapbVersion
).map(
  _.excludeAll(
    ExclusionRule("org.scala-lang"),
    ExclusionRule("org.slf4j"),
    ExclusionRule("log4j")
  )
)

//assembly
assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("io.netty.**" -> "adampro.grpc.shaded.io.netty.@1").inAll,
  ShadeRule.rename("com.fasterxml.**" -> "adampro.grpc.shaded.com.fasterxml.@1").inAll,
  ShadeRule.rename("org.apache.**" -> "adampro.grpc.shaded.org.apache.@1").inAll
)

assemblyOption in assembly :=
  (assemblyOption in assembly).value.copy(includeScala = false)

val meta = """META.INF(.)*""".r
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case meta(_) => MergeStrategy.discard
  case x => MergeStrategy.first
}
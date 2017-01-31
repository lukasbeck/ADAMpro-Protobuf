//protobuf
import sbtassembly.AssemblyPlugin.autoImport._

name := "ADAMpro-grpc"

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

resolvers ++= Seq(
  "Twitter" at "http://maven.twttr.com/"
)

libraryDependencies ++= Seq(
  "com.google.protobuf" % "protobuf-java" % "3.1.0",
  "io.grpc" % "grpc-netty" % "1.0.3",
  "io.grpc" % "grpc-protobuf" % "1.0.3",
  "io.grpc" % "grpc-stub" % "1.0.3",
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % com.trueaccord.scalapb.compiler.Version.scalapbVersion
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
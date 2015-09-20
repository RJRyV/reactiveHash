name := "reactiveHash"

version := "v0.1"

scalaVersion := "2.11.6"

resolvers += "TypeSafe Repository" at "http://repo.typesafe.com/typesafe/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT"

//logLevel := Level.Debug

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + "." + artifact.extension
}

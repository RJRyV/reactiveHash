name := "reactiveHash"

version := "v0.1"

scalaVersion := "2.11.4"

resolvers += "TypeSafe Repository" at "http://repo.typesafe.com/typesafe/releases"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.2.5"

//logLevel := Level.Debug

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  artifact.name + "." + artifact.extension
}

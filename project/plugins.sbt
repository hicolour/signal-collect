resolvers += Resolver.url(
	"artifactory",
	url("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(
		Resolver.ivyStylePatterns)

resolvers += Resolver.url(
	"bintray-sbt-plugin-releases",
	url("http://dl.bintray.com/content/sbt/sbt-plugin-releases"))(
		Resolver.ivyStylePatterns)

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.9.2")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.8")

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-multi-jvm" % "0.3.8")

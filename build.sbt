name := "terratraveler"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
	"org.specs2" %% "specs2" % "2.3.7" % "test",
  	jdbc,
  	anorm,
  	cache
)     

testOptions in Test := Seq(Tests.Filter((s: String) => !s.startsWith("ttt")))

play.Project.playScalaSettings

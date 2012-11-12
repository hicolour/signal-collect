Signal/Collect
==============

Signal/Collect is a framework for computations on large graphs. The model allows to concisely express many iterated and data-flow algorithms, while the framework parallelizes and distributes the computation.

This repository conatains pre-release snapshots of the distributed 2.0 version.

# How to compile the project #
Install SBT: http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html
Go to the project folder and start SBT on the command line. The output should end with:
"[info] Set current project to signal-collect (in build file:XYZ/signal-collect/)"
Write "assembly" on the SBT prompt to generate a .jar file with dependencies.
Write "eclipse" on the SBT prompt to generate an eclipse project.

# How to develop in Eclipse #
Generate an Eclipse project as described above.
Install Eclipse IDE 3.7 (Indigo) for Java developers from: http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/indigosr2
Within Eclipse, install the Scala IDE plugins (for Scala 2.10-RC2): Help -> Install New Software ... -> Add -> http://download.scala-ide.org/sdk/e38/scala210/dev/site/
Select and install all plugins from that location.
Open the Scala project that was generated by SBT with: File -> Import... -> General -> Existing Projects into Workspace -> select signal-collect folder
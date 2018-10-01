# StreamGen

Distributed streaming applications, i.e. applications that process massive and potentially infinite streams of data in a distributed fashion, are becoming increasingly popular to tame the velocity and the volume of Big Data. Nevertheless, their widespread adoption is still limited by 1) the non-trivial design paradigms that deal with the unboundedness and vol ume of involved data streams and by 2) the many distributed streaming platforms, each with its own characteristics and APIs. StreamGen is a Model-Driven Engineering tool prototype aiming at simplifying the design of streaming applications and at automatically generating the corresponding executable code. StreamGen is built around the following two main components: (i) a UML profile and a modeling method to add streaming-specific concepts to standard UML Diagrams; (ii) a code generation module, which currently supports the Apache Spark and Apache Flink platforms. 

# Repo Structure

StreamGen is currently still a research prototype. Nevetheless, we are actively working on StreamGen adding new features and consolidating the current implementation towards an initial release (that we plan to publish during the upcoming weeks). Right now you can checkout this repository and try StreamGen directly into the Eclipse IDE. 
StreamGen is developed using [Eclipse Papyrus v3.1.0](https://www.eclipse.org/papyrus/) for implementing the UML profile (although more recent versions can be used as well to work with StreamGen) and [Eclipse Acceleo v3.7.2](https://www.eclipse.org/acceleo/) for the code generators. In order use StreamGen within Eclipse it is necessary to install both Papyrus and Acceleo.
The repository contains the following main folders:


**metamodel**: containes the Ecore metamodel for the modeling language provided in StreamGen

**profile**: contains the PPapyrus UML profile, called StreamUML, used in StreamGen for modeling streaming applications

**src**: contains the source code for the code generation modules provided in StreamGen

**wordcount**: contains the UML model and the generated code (both for Spark an Flink) for the WordCount application example

**temptrack**: contains the UML model and the generated code (both for Spark and Flink) for the TempTrack case study

**survey**: contains the results of a survey we conducted with a group of young software engineers in order to assess the usability and usefulness of StreamGen

# Requirements


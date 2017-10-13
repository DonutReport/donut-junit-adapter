![](http://donutreport.github.io/donut/img/Donut-05.png)

Donut JUnit adapter is an open source adapter written for the open-source framework [donut](https://github.com/DonutReport/donut) by the teams at [MagenTys](https://magentys.io) & [Mechanical Rock](https://mechanicalrock.io) and is designed to generate gherkin jsons from JUnit xmls.
These gherkin jsons can be processed by donut.

### Options

`-p` or `--junit-result-dir-path` is a mandatory parameter, and it should be the path of junit result xml directory.<br>
`-o` or `--outputdir` is an optional parameter, and it should be the directory for storing the JSON reports. 

## Use as a dependency

* Maven
```
<dependency>
  <groupId>report.donut</groupId>
  <artifactId>donut-junit-adapter</artifactId>
  <version>0.2.2</version>
</dependency>
```
For older versions:
```
<dependency>
  <groupId>io.magentys</groupId>
  <artifactId>donut-junit-adapter</artifactId>
  <version>0.2.1</version>
</dependency>
```

## Build from source

### prerequisites

* install java 8+
* install maven

- Checkout the project
- Execute the command `mvn clean package` to generate the jar file
- Execute the following command to generate the JSON files:

### Usage
```
java -jar target/donut-nunit-adapter-0.2.2-SNAPSHOT-jar-with-dependencies.jar
-p <path_of_junit_result_xml_dir>
-o <path_of_directory_to_store_JSON_files>
```
### Output Location
```
If the output directory is specified,
	 the JSON files are written in the folder `junit-reports` in the specified directory
else		 
	if you're executing the jar at the project level
		 the JSON files are written in the folder `junit-reports` in the target folder
	else
		 the JSON files are written in the folder `junit-reports` in the current folder
```

## License

This project is under an MIT license

Powered by: [MagenTys](https://magentys.io) & [Mechanical Rock](https://www.mechanicalrock.io)

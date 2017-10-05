## Quickstart

Run using docker:

```
docker run --rm -it -v /path/to/junit/xml/target:/source -v /path/to/write/adapted/output/:/output --entrypoint="bash"  donut/donut-junit-adapter
```

Combine with Donut:

NOTE: you must have at least one cucumber.json output, as well as your unit test results.

Ensure your cucumber json and JUnit reports are in the same directory
```
docker run -v /path/to/your/cucumber-and-junit-reports:/source -v /path/to/output-report:/output donutreport/donut-docker -n myProjectName
```

### Options

`-p` or `--junit-result-dir-path` is a mandatory parameter, and it should be the path of junit result xml directory.<br>
`-o` or `--outputdir` is an optional parameter, and it should be the directory for storing the JSON reports.

### Usage

Currently, the only option is to build the project yourself. It will be on maven soon.
- Checkout the project.
- Execute the command `mvn clean package` to generate the jar file
- Execute the following command to generate the JSON files:
```
java -jar target/donut-nunit-adapter-0.1-SNAPSHOT-jar-with-dependencies.jar
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

# Contribution

## Deploying Docker container

```
docker login
docker build . -t donutreport/donut-junit-adapter:<version>
docker push donutreport/donut-junit-adapter:<version>

```

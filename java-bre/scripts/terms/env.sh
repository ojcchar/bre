#!/bin/bash

CLASSPATH=/home/Oscar/Documents/Workspaces/bre/java-bre/target/java-bre-0.0.1.jar
CLASSPATH=$CLASSPATH:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.core.commands_3.6.100.v20140528-1422.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.core.contenttype_3.4.200.v20140207-1251.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.core.expressions_3.4.600.v20140128-0851.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.core.filesystem_1.4.100.v20140514-1614.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.core.jobs_3.6.0.v20140424-0053.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.core.resources_3.9.1.v20140825-1431.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.core.runtime_3.10.0.v20140318-2214.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.equinox.app_1.3.200.v20130910-1609.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.equinox.common_3.6.200.v20130402-1505.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.equinox.preferences_3.5.200.v20140224-1527.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.equinox.registry_3.5.400.v20140428-1507.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.jdt.core_3.10.2.v20150120-1634.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.osgi_3.10.1.v20140909-1633.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.eclipse.text_3.5.300.v20130515-1451.jar:/home/Oscar/Documents/Workspaces/bre/java-bre/lib/org.hamcrest.core_1.3.0.v201303031735.jar
export CLASSPATH
export MAIN_CLASS_TERMS=edu.utdallas.seers.bre.javabre.main.MainWords
export MAIN_CLASS_BTERMS=edu.utdallas.seers.bre.javabre.main.MainBTs
export MAIN_CLASS_IFST=edu.utdallas.seers.bre.javabre.main.MainIF
export OUT_FOLD=terms

mkdir $OUT_FOLD
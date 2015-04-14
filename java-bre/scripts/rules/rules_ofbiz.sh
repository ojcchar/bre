#!/bin/bash

echo Executing env.sh
source /home/Oscar/Documents/Workspaces/bre/java-bre/scripts/rules/env.sh

export TERMS=/home/Oscar/Documents/Workspaces/bre/java-bre/terms_ofbiz.txt
export CLASSPATH_TGT_SYSTEM=/home/Oscar/ofbiz/applications/content/lib/fontbox-1.7.1.jar,/home/Oscar/ofbiz/applications/content/lib/jempbox-1.7.1.jar,/home/Oscar/ofbiz/applications/content/lib/pdfbox-1.7.1.jar,/home/Oscar/ofbiz/applications/content/lib/dom4j-1.6.1.jar,/home/Oscar/ofbiz/applications/content/lib/poi-3.10.1-20140818.jar,/home/Oscar/ofbiz/applications/content/lib/tika-core-1.7.jar,/home/Oscar/ofbiz/applications/content/lib/tika-parsers-1.7.jar,/home/Oscar/ofbiz/applications/product/lib/watermarker-0.0.4.jar,/home/Oscar/ofbiz/applications/product/lib/dozer-4.2.1.jar,/home/Oscar/ofbiz/framework/base/lib/ant-1.9.0-ant.jar,/home/Oscar/ofbiz/framework/base/lib/ant-1.9.0-ant-junit.jar,/home/Oscar/ofbiz/framework/base/lib/ant-1.9.0-ant-launcher.jar,/home/Oscar/ofbiz/framework/base/lib/ant/ant-1.9.0-ant-apache-bsf.jar,/home/Oscar/ofbiz/framework/base/lib/avalon-framework-4.2.0.jar,/home/Oscar/ofbiz/framework/base/lib/barcode4j-2.1-barcode4j-fop-ext-complete.jar,/home/Oscar/ofbiz/framework/base/lib/batik-all-1.7.jar,/home/Oscar/ofbiz/framework/base/lib/clhm-release-1.0-lru.jar,/home/Oscar/ofbiz/framework/base/lib/hamcrest-all-1.2.jar,/home/Oscar/ofbiz/framework/base/lib/fop-1.1.jar,/home/Oscar/ofbiz/framework/base/lib/freemarker-2.3.22.jar,/home/Oscar/ofbiz/framework/base/lib/httpclient-4.3.6.jar,/home/Oscar/ofbiz/framework/base/lib/httpclient-cache-4.3.6.jar,/home/Oscar/ofbiz/framework/base/lib/httpcore-4.3.3.jar,/home/Oscar/ofbiz/framework/base/lib/httpunit-1.7.jar,/home/Oscar/ofbiz/framework/base/lib/ical4j-1.0-rc2.jar,/home/Oscar/ofbiz/framework/base/lib/icu4j-52_1.jar,/home/Oscar/ofbiz/framework/base/lib/ivy-2.2.0.jar,/home/Oscar/ofbiz/framework/base/lib/jackson-annotations-2.4.0.jar,/home/Oscar/ofbiz/framework/base/lib/jackson-core-2.4.2.jar,/home/Oscar/ofbiz/framework/base/lib/jackson-databind-2.4.2.jar,/home/Oscar/ofbiz/framework/base/lib/javolution-5.4.3.jar,/home/Oscar/ofbiz/framework/base/lib/jdbm-1.0-SNAPSHOT.jar,/home/Oscar/ofbiz/framework/base/lib/jdom-1.1.jar,/home/Oscar/ofbiz/framework/base/lib/jpim-0.1.jar,/home/Oscar/ofbiz/framework/base/lib/juel-impl-2.2.7.jar,/home/Oscar/ofbiz/framework/base/lib/juel-spi-2.2.7.jar,/home/Oscar/ofbiz/framework/base/lib/junit-dep-4.10.jar,/home/Oscar/ofbiz/framework/base/lib/log4j-api-2.2.jar,/home/Oscar/ofbiz/framework/base/lib/mail-1.5.1.jar,/home/Oscar/ofbiz/framework/base/lib/nekohtml-1.9.16.jar,/home/Oscar/ofbiz/framework/base/lib/esapi-2.1.0.jar,/home/Oscar/ofbiz/framework/base/lib/resolver-2.9.1.jar,/home/Oscar/ofbiz/framework/base/lib/serializer-2.9.1.jar,/home/Oscar/ofbiz/framework/base/lib/slf4j-api-1.6.4.jar,/home/Oscar/ofbiz/framework/base/lib/xercesImpl-2.9.1.jar,/home/Oscar/ofbiz/framework/base/lib/ws-commons-java5-1.0.1.jar,/home/Oscar/ofbiz/framework/base/lib/ws-commons-util-1.0.2.jar,/home/Oscar/ofbiz/framework/base/lib/xml-apis-2.9.1.jar,/home/Oscar/ofbiz/framework/base/lib/xml-apis-ext-1.3.04.jar,/home/Oscar/ofbiz/framework/base/lib/xmlgraphics-commons-1.5.jar,/home/Oscar/ofbiz/framework/base/lib/xmlrpc-client-3.1.2.jar,/home/Oscar/ofbiz/framework/base/lib/xmlrpc-common-3.1.2.jar,/home/Oscar/ofbiz/framework/base/lib/xmlrpc-server-3.1.2.jar,/home/Oscar/ofbiz/framework/base/lib/xstream-1.4.6.jar,/home/Oscar/ofbiz/framework/base/lib/xpp3-1.1.4c.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-beanutils-core-1.8.3.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-cli-1.2.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-codec-1.10.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-collections-3.2.1.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-compress-1.4.1.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-el-1.0.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-fileupload-1.3.1.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-io-2.4.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-lang-2.6.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-logging-1.2.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-net-3.3.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-pool2-2.3.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-validator-1.4.1.jar,/home/Oscar/ofbiz/framework/base/lib/commons/commons-csv-1.1.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/geronimo-activation_1.0.2_spec-1.0.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/geronimo-j2ee-connector_1.5_spec-2.0.0.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/geronimo-jaxr_1.0_spec-1.0.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/geronimo-jaxrpc_1.1_spec-1.0.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/geronimo-jms_1.1_spec-1.1.1.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/geronimo-jta_1.1_spec-1.1.1.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/geronimo-saaj_1.3_spec-1.1.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/annotations-api-3.0.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/el-api-2.2.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/jsp-api-2.2.jar,/home/Oscar/ofbiz/framework/base/lib/j2eespecs/servlet-api-3.0.jar,/home/Oscar/ofbiz/framework/base/lib/scripting/antlr-2.7.6.jar,/home/Oscar/ofbiz/framework/base/lib/scripting/asm-3.2.jar,/home/Oscar/ofbiz/framework/base/lib/scripting/bsf-2.4.0.jar,/home/Oscar/ofbiz/framework/base/lib/scripting/bsh-2.0b4.jar,/home/Oscar/ofbiz/framework/base/lib/scripting/groovy-all-2.2.1.jar,/home/Oscar/ofbiz/framework/base/lib/scripting/jakarta-oro-2.0.8.jar,/home/Oscar/ofbiz/framework/base/lib/scripting/jython-nooro.jar,/home/Oscar/ofbiz/framework/catalina/lib/ecj-4.4.jar,/home/Oscar/ofbiz/framework/catalina/lib/tomcat-7.0.59-catalina-ha.jar,/home/Oscar/ofbiz/framework/catalina/lib/tomcat-7.0.59-catalina-tribes.jar,/home/Oscar/ofbiz/framework/catalina/lib/tomcat-7.0.59-catalina.jar,/home/Oscar/ofbiz/framework/catalina/lib/tomcat-7.0.59-jasper.jar,/home/Oscar/ofbiz/framework/catalina/lib/tomcat-7.0.59-tomcat-api.jar,/home/Oscar/ofbiz/framework/catalina/lib/tomcat-7.0.59-tomcat-coyote.jar,/home/Oscar/ofbiz/framework/catalina/lib/tomcat-7.0.59-tomcat-util.jar,/home/Oscar/ofbiz/framework/catalina/lib/tomcat-extras-7.0.59-tomcat-juli.jar,/home/Oscar/ofbiz/framework/catalina/lib/tomcat-extras-7.0.59-tomcat-juli-adapters.jar,/home/Oscar/ofbiz/framework/entity/lib/commons-dbcp2-2.1.jar,/home/Oscar/ofbiz/framework/geronimo/lib/geronimo-transaction-3.1.1.jar,/home/Oscar/ofbiz/framework/service/lib/wsdl4j-1.6.2.jar,/home/Oscar/ofbiz/framework/service/lib/axiom-api-1.2.9.jar,/home/Oscar/ofbiz/framework/service/lib/axiom-impl-1.2.9.jar,/home/Oscar/ofbiz/framework/service/lib/axis2-kernel-1.5.2.jar,/home/Oscar/ofbiz/framework/service/lib/axis2-transport-http-1.5.2.jar,/home/Oscar/ofbiz/framework/service/lib/axis2-transport-local-1.5.2.jar,/home/Oscar/ofbiz/framework/service/lib/commons-httpclient-3.1.jar,/home/Oscar/ofbiz/framework/service/lib/neethi-2.0.4.jar,/home/Oscar/ofbiz/framework/service/lib/XmlSchema-1.4.3.jar,/home/Oscar/ofbiz/framework/testtools/lib/org.springframework.test-3.1.0.M2.jar,/home/Oscar/ofbiz/framework/testtools/lib/org.springframework.core-3.1.0.M2.jar,/home/Oscar/ofbiz/framework/webapp/lib/ezmorph-0.9.1.jar,/home/Oscar/ofbiz/framework/webapp/lib/iText-2.1.7.jar,/home/Oscar/ofbiz/framework/webapp/lib/rome-0.9.jar,/home/Oscar/ofbiz/specialpurpose/birt/lib/axis-1.4.jar,/home/Oscar/ofbiz/specialpurpose/birt/lib/axis-ant-1.4.jar,/home/Oscar/ofbiz/specialpurpose/birt/lib/commons-discovery-0.5.jar,/home/Oscar/ofbiz/specialpurpose/birt/lib/org.eclipse.birt.runtime_4.3.1.v20130918-1142.jar,/home/Oscar/ofbiz/specialpurpose/birt/lib/viewservlets.jar,/home/Oscar/ofbiz/specialpurpose/ebaystore/lib/attributes.jar,/home/Oscar/ofbiz/specialpurpose/ebaystore/lib/ebaycalls.jar,/home/Oscar/ofbiz/specialpurpose/ebaystore/lib/ebaysdkcore.jar,/home/Oscar/ofbiz/specialpurpose/ebaystore/lib/helper.jar,/home/Oscar/ofbiz/specialpurpose/googlecheckout/lib/checkout-sdk-0.8.8.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/com.sun.el-2.2.0.v201108011116.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/javax.servlet.jsp.jstl-1.2.0.v201105211821.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-ajp-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-continuation-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-http-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-io-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-security-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-server-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-servlet-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-util-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-webapp-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/jetty-xml-8.1.2.v20120308.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/org.apache.jasper.glassfish-2.2.2.v201112011158.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/org.apache.taglibs.standard.glassfish-1.2.0.v201112081803.jar,/home/Oscar/ofbiz/specialpurpose/jetty/lib/org.eclipse.jdt.core-3.7.1.jar,/home/Oscar/ofbiz/specialpurpose/ldap/lib/cas-server-core-3.3.jar,/home/Oscar/ofbiz/specialpurpose/pos/lib/XuiCoreSwing-v3.2rc2b.jar,/home/Oscar/ofbiz/specialpurpose/pos/lib/XuiOptional-v3.2rc2b.jar,/home/Oscar/ofbiz/specialpurpose/pos/lib/jcl.jar,/home/Oscar/ofbiz/specialpurpose/pos/lib/jpos18-controls.jar,/home/Oscar/ofbiz/specialpurpose/pos/lib/looks-2.0.2.jar,/home/Oscar/ofbiz/specialpurpose/lucene/lib/lucene-analyzers-common-4.9.0.jar,/home/Oscar/ofbiz/specialpurpose/lucene/lib/lucene-core-4.9.0.jar,/home/Oscar/ofbiz/specialpurpose/lucene/lib/lucene-queryparser-4.9.0.jar
export SOURCES_TGT_SYSTEM=/home/Oscar/ofbiz/applications/accounting/src,/home/Oscar/ofbiz/applications/content/src,/home/Oscar/ofbiz/applications/manufacturing/src,/home/Oscar/ofbiz/applications/marketing/src,/home/Oscar/ofbiz/applications/order/src,/home/Oscar/ofbiz/applications/party/src,/home/Oscar/ofbiz/applications/product/src,/home/Oscar/ofbiz/applications/securityext/src,/home/Oscar/ofbiz/applications/humanres/src,/home/Oscar/ofbiz/applications/workeffort/src,/home/Oscar/ofbiz/framework/base/src,/home/Oscar/ofbiz/framework/catalina/src,/home/Oscar/ofbiz/framework/common/src,/home/Oscar/ofbiz/framework/datafile/src,/home/Oscar/ofbiz/framework/entity/src,/home/Oscar/ofbiz/framework/entityext/src,/home/Oscar/ofbiz/framework/geronimo/src,/home/Oscar/ofbiz/framework/minilang/src,/home/Oscar/ofbiz/framework/security/src,/home/Oscar/ofbiz/framework/service/src,/home/Oscar/ofbiz/framework/start/src,/home/Oscar/ofbiz/framework/testtools/src,/home/Oscar/ofbiz/framework/webapp/src,/home/Oscar/ofbiz/framework/webtools/src,/home/Oscar/ofbiz/framework/widget/src,/home/Oscar/ofbiz/specialpurpose/assetmaint/src,/home/Oscar/ofbiz/specialpurpose/birt/src,/home/Oscar/ofbiz/specialpurpose/ebay/src,/home/Oscar/ofbiz/specialpurpose/ebaystore/src,/home/Oscar/ofbiz/specialpurpose/ecommerce/src,/home/Oscar/ofbiz/specialpurpose/example/src,/home/Oscar/ofbiz/specialpurpose/googlebase/src,/home/Oscar/ofbiz/specialpurpose/googlecheckout/src,/home/Oscar/ofbiz/specialpurpose/hhfacility/src,/home/Oscar/ofbiz/specialpurpose/jetty/src,/home/Oscar/ofbiz/specialpurpose/ldap/src,/home/Oscar/ofbiz/specialpurpose/oagis/src,/home/Oscar/ofbiz/specialpurpose/pos/src,/home/Oscar/ofbiz/specialpurpose/projectmgr/src,/home/Oscar/ofbiz/specialpurpose/scrum/src,/home/Oscar/ofbiz/specialpurpose/webpos/src,/home/Oscar/ofbiz/specialpurpose/lucene/src
export OUT_FILE=$OUT_FOLD/rules_ofbiz.csv

echo Executing the BRE program...
/home/Oscar/Documents/jdk1.8.0_25/bin/java $MAIN_CLASS_RULES $SOURCES_TGT_SYSTEM $CLASSPATH_TGT_SYSTEM $TERMS $OUT_FILE



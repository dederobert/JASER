package fr.lehtto.jaser.dns;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * Run cucumber tests.
 *
 * @author lehtto
 * @since 0.2.0
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("fr/lehtto/jaser/dns")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@SuppressWarnings("JUnitTestCaseWithNoTests")
class RunCucumberTest { // NOSONAR
  // This class is used to run cucumber tests.
}

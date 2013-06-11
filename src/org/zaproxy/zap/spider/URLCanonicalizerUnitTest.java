package org.zaproxy.zap.spider;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.junit.Test;
import org.zaproxy.zap.spider.SpiderParam.HandleParametersOption;

/**
 * This test ensure that nothing was broken in the handling of normal URLs during
 * the implementation of OData support as well as ensure that the OData support is correct.
 * 
 * It checks the canonicalization mechanism used to verify if a URL has already 
 * been visited before during the spider phase.
 */
public class URLCanonicalizerUnitTest {

	
	// Test of the legacy behavior
	
	@Test
	public void shouldCanonicalizeNormalURLWithoutParametersIn_USE_ALL_mode() throws URIException {
		URI uri = new URI("http", null,"host", 9001, "/myservlet");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, SpiderParam.HandleParametersOption.USE_ALL, false /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/myservlet"));
	}
	
	
	@Test
	public void shouldCanonicalizeNormalURLWithoutParametersIn_IGNORE_COMPLETELY_mode() throws URIException {
		URI uri = new URI("http", null,"host", 9001, "/myservlet");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, SpiderParam.HandleParametersOption.IGNORE_COMPLETELY, false /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/myservlet"));
	}
	
	@Test
	public void shouldCanonicalizeNormalURLWithoutParametersIn_IGNORE_VALUE_mode() throws URIException {
		URI uri = new URI("http", null,"host", 9001, "/myservlet");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, SpiderParam.HandleParametersOption.IGNORE_VALUE, false /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/myservlet"));
	}
	
	@Test
	public void shouldCanonicalizeNormalURLWithParametersIn_USE_ALL_mode() throws URIException {
		
		URI uri = new URI("http", null,"host", 9001, "/myservlet","p1=2&p2=myparam");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, SpiderParam.HandleParametersOption.USE_ALL, false /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/myservlet?p1=2&p2=myparam"));
	}
	
	@Test
	public void shouldCanonicalizeNormalURLWithParametersIn_IGNORE_COMPLETELY_mode() throws URIException {	
		URI uri = new URI("http", null,"host", 9001, "/myservlet","p1=2&p2=myparam");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, SpiderParam.HandleParametersOption.IGNORE_COMPLETELY, false /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/myservlet"));
	}
	
	@Test
	public void shouldCanonicalizeNormalURLWithParametersIn_IGNORE_VALUE_mode() throws URIException {
		URI uri = new URI("http", null,"host", 9001, "/myservlet","p1=2&p2=myparam");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, SpiderParam.HandleParametersOption.IGNORE_VALUE, false /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/myservlet?p1&p2"));
	}	
	
	
	// Test the OData behavior
	
	@Test
	public void shouldCanonicalizeODataIDSimpleIn_USE_ALL_mode() throws URIException {
		HandleParametersOption spiderOpion = SpiderParam.HandleParametersOption.USE_ALL;
		
		URI uri = new URI("http", null,"host", 9001, "/app.svc/Book(1)");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book(1)"));
		
		uri = new URI("http", null,"host", 9001, "/app.svc/Book(1)/Author");
		visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book(1)/Author"));
	}
	
	@Test
	public void shouldCanonicalizeODataIDSimpleIn_IGNORE_COMPLETELY_mode() throws URIException {
		HandleParametersOption spiderOpion = SpiderParam.HandleParametersOption.IGNORE_COMPLETELY;
		
		URI uri = new URI("http", null,"host", 9001, "/app.svc/Book(1)");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book()"));
		
		uri = new URI("http", null,"host", 9001, "/app.svc/Book(1)/Author");
		visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book()/Author"));
	}
	
	@Test
	public void shouldCanonicalizeODataIDSimpleIn_IGNORE_VALUE_mode() throws URIException {
		HandleParametersOption spiderOpion = SpiderParam.HandleParametersOption.IGNORE_VALUE;
		
		URI uri = new URI("http", null,"host", 9001, "/app.svc/Book(1)");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book()"));
		
		uri = new URI("http", null,"host", 9001, "/app.svc/Book(1)/Author");
		visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book()/Author"));
	}
	
	@Test
	public void shouldCanonicalizeODataIDMultipleIn_USE_ALL_mode() throws URIException {
		HandleParametersOption spiderOpion = SpiderParam.HandleParametersOption.USE_ALL;
			
		URI uri = new URI("http", null,"host", 9001, "/app.svc/Book(title='dummy',year=2012)");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book(title='dummy',year=2012)"));
		
		uri = new URI("http", null,"host", 9001, "/app.svc/Book(title='dummy',year=2012)/Author");
		visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book(title='dummy',year=2012)/Author"));
	}
	
	@Test
	public void shouldCanonicalizeODataIDMultipleIn_IGNORE_COMPLETELY_mode() throws URIException {
		HandleParametersOption spiderOpion = SpiderParam.HandleParametersOption.IGNORE_COMPLETELY;
		
		URI uri = new URI("http", null,"host", 9001, "/app.svc/Book(title='dummy',year=2012)");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book()"));
		
		uri = new URI("http", null,"host", 9001, "/app.svc/Book(title='dummy',year=2012)/Author");
		visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book()/Author"));
	}
	
	@Test
	public void shouldCanonicalizeODataIDMultipleIn_IGNORE_VALUE_mode() throws URIException {
		HandleParametersOption spiderOpion = SpiderParam.HandleParametersOption.IGNORE_VALUE;
		
		URI uri = new URI("http", null,"host", 9001, "/app.svc/Book(title='dummy',year=2012)");
		String 	visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book(title,year)"));

		uri = new URI("http", null,"host", 9001, "/app.svc/Book(title='dummy',year=2012)/Author");
		visitedURI = URLCanonicalizer.buildCleanedParametersURIRepresentation(uri, spiderOpion, true /* handleODataParametersVisited */);
		assertThat(visitedURI, is("http://host:9001/app.svc/Book(title,year)/Author"));
	}
	
}